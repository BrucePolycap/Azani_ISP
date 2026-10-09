package dao;

import db.DBConnection;
import models.MonthlyPayment;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MonthlyPaymentDAO {

    // Business constants
    public static final double FINE_RATE = 0.15;
    public static final double RECONNECTION_FEE = 1000.00;
    public static final double UPGRADE_DISCOUNT_RATE = 0.10;


    // 1. INSERT a new monthly bill
    public int addMonthlyPayment(MonthlyPayment mp) {
        String sql = "INSERT INTO monthly_payment " +
                     "(institution_id, bandwidth, amount, discount, fine, " +
                     " reconnection_fee, month, year, payment_method, " +
                     " is_paid, is_upgrade, payment_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int generatedId = -1;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, mp.getInstitutionId());
            ps.setInt(2, mp.getBandwidth());
            ps.setDouble(3, mp.getAmount());
            ps.setDouble(4, mp.getDiscount());
            ps.setDouble(5, mp.getFine());
            ps.setDouble(6, mp.getReconnectionFee());
            ps.setString(7, mp.getMonth());
            ps.setInt(8, mp.getYear());
            ps.setString(9, mp.getPaymentMethod());
            ps.setBoolean(10, mp.isPaid());
            ps.setBoolean(11, mp.isUpgrade());
            ps.setDate(12, mp.getPaymentDate() != null ?
                    Date.valueOf(mp.getPaymentDate()) : null);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) generatedId = rs.getInt(1);
                System.out.println(" Monthly bill saved (ID: " + generatedId +
                        ", KSh " + mp.getAmount() + " for " + mp.getMonth() + " " + mp.getYear() + ")");
            }

        } catch (SQLException e) {
            System.out.println("Error adding monthly payment: " + e.getMessage());
        }
        return generatedId;
    }


    // 2. Mark a bill as paid
    public void markAsPaid(int monthlyId, String method) {
        String sql = "UPDATE monthly_payment SET is_paid = TRUE, " +
                     "payment_method = ?, payment_date = ? WHERE monthly_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, method);
            ps.setDate(2, Date.valueOf(LocalDate.now()));
            ps.setInt(3, monthlyId);

            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println(" Bill " + monthlyId + " marked as paid via " + method);

        } catch (SQLException e) {
            System.out.println("Error marking bill as paid: " + e.getMessage());
        }
    }


    // 3. Apply 15% fine on a bill
    public void applyFine(int monthlyId) {
        String sql = "UPDATE monthly_payment SET fine = amount * ? " +
                     "WHERE monthly_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, FINE_RATE);
            ps.setInt(2, monthlyId);

            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println(" 15% fine applied to bill " + monthlyId);

        } catch (SQLException e) {
            System.out.println("Error applying fine: " + e.getMessage());
        }
    }


    // 4. Apply KSh 1,000 reconnection fee
    public void applyReconnectionFee(int monthlyId) {
        String sql = "UPDATE monthly_payment SET reconnection_fee = ? " +
                     "WHERE monthly_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, RECONNECTION_FEE);
            ps.setInt(2, monthlyId);

            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println(" KSh 1,000 reconnection fee applied to bill " + monthlyId);

        } catch (SQLException e) {
            System.out.println("Error applying reconnection fee: " + e.getMessage());
        }
    }

        

    // Sync the institution.is_disconnected flag with reality:
    // Any institution with at least one unpaid bill is flagged.
    // Any institution with no unpaid bills is cleared.
    public void syncDisconnectionFlags() {
        String markSql = "UPDATE institution SET is_disconnected = TRUE " +
                         "WHERE institution_id IN (" +
                         "  SELECT DISTINCT institution_id FROM monthly_payment " +
                         "  WHERE is_paid = FALSE" +
                         ")";

        String clearSql = "UPDATE institution SET is_disconnected = FALSE " +
                          "WHERE institution_id NOT IN (" +
                          "  SELECT DISTINCT institution_id FROM monthly_payment " +
                          "  WHERE is_paid = FALSE" +
                          ")";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement()) {

            int marked  = st.executeUpdate(markSql);
            int cleared = st.executeUpdate(clearSql);

            System.out.println(" Disconnection flags synced: " +
                               marked + " flagged, " + cleared + " cleared.");

        } catch (SQLException e) {
            System.out.println("Error syncing disconnection flags: " + e.getMessage());
        }
    }

    
    /**
     * Change the plan on a bill. Affects:
     *   - The selected bill: bandwidth, amount, discount, is_upgrade.
     *   - All FUTURE bills for the same institution (strictly later dates):
     *     bandwidth and amount are updated, but WITHOUT a discount.
     *   - Past bills: untouched.
     *
     * Returns a message describing the change, or null on failure.
     */
    public String changePlan(int monthlyId, int newBandwidth) {

        // --- Step 1: load the selected bill (bandwidth, institution, month, year) ---
        String selectSql =
            "SELECT institution_id, bandwidth, month, year FROM monthly_payment " +
            "WHERE monthly_id = ?";
        int institutionId = -1;
        int currentBandwidth = -1;
        String month = null;
        int year = 0;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(selectSql)) {

            ps.setInt(1, monthlyId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                institutionId    = rs.getInt("institution_id");
                currentBandwidth = rs.getInt("bandwidth");
                month            = rs.getString("month");
                year             = rs.getInt("year");
            }

        } catch (SQLException e) {
            System.out.println("Error reading bill: " + e.getMessage());
            return null;
        }

        if (institutionId == -1) {
            System.out.println("Bill not found.");
            return null;
        }

        // --- Step 2: validate new plan ---
        double newAmount = getBandwidthCost(newBandwidth);
        if (newAmount == 0) {
            System.out.println("Invalid bandwidth: " + newBandwidth);
            return null;
        }

        // --- Step 3: determine if this is upgrade / downgrade / same ---
        double newDiscount = 0;
        boolean isUpgrade  = false;
        String  type;

        if (newBandwidth > currentBandwidth) {
            newDiscount = newAmount * UPGRADE_DISCOUNT_RATE;
            isUpgrade   = true;
            type        = "UPGRADE";
        } else if (newBandwidth < currentBandwidth) {
            type        = "DOWNGRADE";
        } else {
            type        = "SAME PLAN";
        }

        // --- Step 4: update the SELECTED bill (with discount if upgrade) ---
        String updateSelected =
            "UPDATE monthly_payment SET " +
            "bandwidth = ?, amount = ?, discount = ?, is_upgrade = ? " +
            "WHERE monthly_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(updateSelected)) {

            ps.setInt(1, newBandwidth);
            ps.setDouble(2, newAmount);
            ps.setDouble(3, newDiscount);
            ps.setBoolean(4, isUpgrade);
            ps.setInt(5, monthlyId);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating selected bill: " + e.getMessage());
            return null;
        }

        // --- Step 5: update FUTURE bills for the same institution (no discount) ---
        int monthNum = monthNameToNumber(month);
        if (monthNum == 0) {
            System.out.println("Unknown month — future bills not updated.");
        } else {
            String updateFuture =
                "UPDATE monthly_payment SET bandwidth = ?, amount = ?, " +
                "discount = 0, is_upgrade = FALSE " +
                "WHERE institution_id = ? " +
                "AND monthly_id <> ? " +
                "AND (year > ? OR (year = ? AND " +
                "     FIELD(LOWER(month), 'january','february','march','april'," +
                "                        'may','june','july','august'," +
                "                        'september','october','november','december') > ?))";

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(updateFuture)) {

                ps.setInt(1, newBandwidth);
                ps.setDouble(2, newAmount);
                ps.setInt(3, institutionId);
                ps.setInt(4, monthlyId);
                ps.setInt(5, year);
                ps.setInt(6, year);
                ps.setInt(7, monthNum);

                int futureUpdated = ps.executeUpdate();

                String msg = "Bill " + monthlyId + " (" + type + "): " +
                             currentBandwidth + " → " + newBandwidth + " MBPS. " +
                             "New amount: KSh " + newAmount +
                             (isUpgrade ? ", discount: KSh " + newDiscount
                                        : ", no discount") +
                             ". Future bills updated: " + futureUpdated + ".";
                System.out.println(msg);
                return msg;

            } catch (SQLException e) {
                System.out.println("Error updating future bills: " + e.getMessage());
                return "Selected bill updated, but future bills failed: " + e.getMessage();
            }
        }

        return "Bill " + monthlyId + " (" + type + "): " +
               currentBandwidth + " → " + newBandwidth + " MBPS. " +
               "New amount: KSh " + newAmount +
               (isUpgrade ? ", discount: KSh " + newDiscount : ", no discount");
    }

    // Helper: month name → number (1–12), 0 on failure
    private int monthNameToNumber(String m) {
        if (m == null) return 0;
        switch (m.toLowerCase()) {
            case "january":   return 1;
            case "february":  return 2;
            case "march":     return 3;
            case "april":     return 4;
            case "may":       return 5;
            case "june":      return 6;
            case "july":      return 7;
            case "august":    return 8;
            case "september": return 9;
            case "october":   return 10;
            case "november":  return 11;
            case "december":  return 12;
            default:          return 0;
        }
    }
    
    // 6. AUTO-APPLY FINE to any unpaid bill past its deadline
    public int autoApplyFinesToOverdueBills() {
        int applied = 0;
        String sql = "SELECT * FROM monthly_payment WHERE is_paid = FALSE AND fine = 0";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            List<Integer> toFine = new ArrayList<>();
            while (rs.next()) {
                MonthlyPayment mp = mapRow(rs);
                if (mp.isFineApplicable()) {
                    toFine.add(mp.getMonthlyId());
                }
            }

            for (int id : toFine) {
                applyFine(id);
                applied++;
            }

        } catch (SQLException e) {
            System.out.println("Error auto-applying fines: " + e.getMessage());
        }
        return applied;
    }

    
    // 7. Get all unpaid bills (defaulters)
    public List<MonthlyPayment> getDefaulters() {
        List<MonthlyPayment> list = new ArrayList<>();
        String sql = "SELECT * FROM monthly_payment WHERE is_paid = FALSE";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            System.out.println("Error fetching defaulters: " + e.getMessage());
        }
        return list;
    }

    
    // 8. Get disconnection-eligible bills
    public List<MonthlyPayment> getDisconnectionIssues() {
        List<MonthlyPayment> all = new ArrayList<>();
        String sql = "SELECT * FROM monthly_payment WHERE is_paid = FALSE";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                MonthlyPayment mp = mapRow(rs);
                if (mp.isDisconnectable()) all.add(mp);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching disconnection issues: " + e.getMessage());
        }
        return all;
    }

    
    // 9. Get all bills for one institution
    public List<MonthlyPayment> getByInstitution(int institutionId) {
        List<MonthlyPayment> list = new ArrayList<>();
        String sql = "SELECT * FROM monthly_payment WHERE institution_id = ? " +
                     "ORDER BY year DESC, month";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, institutionId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            System.out.println("Error fetching monthly payments: " + e.getMessage());
        }
        return list;
    }


    // Table 1 lookup — used by upgradeBill
    private double getBandwidthCost(int mbps) {
        switch (mbps) {
            case 4:  return 1200;
            case 10: return 2000;
            case 20: return 3500;
            case 25: return 4000;
            case 50: return 7000;
            default: return 0;
        }
    }

    
    // Helper: map ResultSet row → MonthlyPayment
    private MonthlyPayment mapRow(ResultSet rs) throws SQLException {
        MonthlyPayment mp = new MonthlyPayment();
        mp.setMonthlyId(rs.getInt("monthly_id"));
        mp.setInstitutionId(rs.getInt("institution_id"));
        mp.setBandwidth(rs.getInt("bandwidth"));
        mp.setAmount(rs.getDouble("amount"));
        mp.setDiscount(rs.getDouble("discount"));
        mp.setFine(rs.getDouble("fine"));
        mp.setReconnectionFee(rs.getDouble("reconnection_fee"));
        mp.setMonth(rs.getString("month"));
        mp.setYear(rs.getInt("year"));
        mp.setPaymentMethod(rs.getString("payment_method"));
        mp.setPaid(rs.getBoolean("is_paid"));
        mp.setUpgrade(rs.getBoolean("is_upgrade"));

        Date d = rs.getDate("payment_date");
        if (d != null) mp.setPaymentDate(d.toLocalDate());

        return mp;
    }
}