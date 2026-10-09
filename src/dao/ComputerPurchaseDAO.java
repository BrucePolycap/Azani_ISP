package dao;

import db.DBConnection;
import models.ComputerPurchase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComputerPurchaseDAO {

    private static final double UNIT_PRICE = 40000.00;  // From PDF

    // 1. INSERT a computer purchase (auto-calculates total)
    public void addPurchase(ComputerPurchase cp) {
        String sql = "INSERT INTO computer_purchase " +
                     "(institution_id, quantity, unit_price, total_cost) " +
                     "VALUES (?, ?, ?, ?)";

        double totalCost = cp.getQuantity() * UNIT_PRICE;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cp.getInstitutionId());
            ps.setInt(2, cp.getQuantity());
            ps.setDouble(3, UNIT_PRICE);
            ps.setDouble(4, totalCost);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Computer purchase recorded: " +
                        cp.getQuantity() + " × KSh " + UNIT_PRICE +
                        " = KSh " + totalCost);
            }

        } catch (SQLException e) {
            System.out.println("Error adding computer purchase: " + e.getMessage());
        }
    }

    // 2. SELECT purchases for one institution
    public List<ComputerPurchase> getByInstitution(int institutionId) {
        List<ComputerPurchase> list = new ArrayList<>();
        String sql = "SELECT * FROM computer_purchase WHERE institution_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, institutionId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ComputerPurchase cp = new ComputerPurchase();
                cp.setPurchaseId(rs.getInt("purchase_id"));
                cp.setInstitutionId(rs.getInt("institution_id"));
                cp.setQuantity(rs.getInt("quantity"));
                cp.setUnitPrice(rs.getDouble("unit_price"));
                cp.setTotalPrice(rs.getDouble("total_cost"));
                list.add(cp);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching computer purchases: " + e.getMessage());
        }
        return list;
    }

    // 3. Total computer cost for one institution (used in Task 4a/4b)
    public double getTotalComputerCostByInstitution(int institutionId) {
        String sql = "SELECT IFNULL(SUM(total_cost), 0) AS total " +
                     "FROM computer_purchase WHERE institution_id = ?";
        double total = 0;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, institutionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) total = rs.getDouble("total");

        } catch (SQLException e) {
            System.out.println("Error summing computer costs: " + e.getMessage());
        }
        return total;
    }
}