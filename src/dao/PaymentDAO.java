package dao;

import db.DBConnection;
import models.Payment;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    // Insert a registration payment (8,500)
    public void addRegistrationPayment(int institutionId, String method) {
        String sql = "INSERT INTO registration_payment " +
                     "(institution_id, amount, payment_method, payment_date) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, institutionId);
            ps.setDouble(2, 8500.00);           // Fixed registration fee
            ps.setString(3, method);
            ps.setDate(4, Date.valueOf(LocalDate.now()));

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Registration payment recorded: KSh 8,500 via " + method);
            }

        } catch (SQLException e) {
            System.out.println("Error adding registration payment: " + e.getMessage());
        }
    }

    // Fetch all registration payments for one institution
    public List<Payment> getRegistrationPayments(int institutionId) {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM registration_payment WHERE institution_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, institutionId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Payment p = new Payment();
                p.setPaymentId(rs.getInt("reg_id"));
                p.setInstitutionId(rs.getInt("institution_id"));
                p.setAmount(rs.getDouble("amount"));
                p.setPaymentMethod(rs.getString("payment_method"));

                Date d = rs.getDate("payment_date");
                if (d != null) p.setPaymentDate(d.toLocalDate());

                list.add(p);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching registration payments: " + e.getMessage());
        }
        return list;
    }

    // Insert an installation payment (10,000)
    public void addInstallationPayment(int institutionId, String method) {
        String sql = "INSERT INTO installation_payment " +
                     "(institution_id, amount, payment_method, payment_date) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, institutionId);
            ps.setDouble(2, 10000.00);          // Fixed installation fee
            ps.setString(3, method);
            ps.setDate(4, Date.valueOf(LocalDate.now()));

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Installation payment recorded: KSh 10,000 via " + method);
            }

        } catch (SQLException e) {
            System.out.println("Error adding installation payment: " + e.getMessage());
        }
    }

    // Fetch all installation payments for one institution
    public List<Payment> getInstallationPayments(int institutionId) {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM installation_payment WHERE institution_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, institutionId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Payment p = new Payment();
                p.setPaymentId(rs.getInt("install_id"));
                p.setInstitutionId(rs.getInt("institution_id"));
                p.setAmount(rs.getDouble("amount"));
                p.setPaymentMethod(rs.getString("payment_method"));

                Date d = rs.getDate("payment_date");
                if (d != null) p.setPaymentDate(d.toLocalDate());

                list.add(p);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching installation payments: " + e.getMessage());
        }
        return list;
    }
}