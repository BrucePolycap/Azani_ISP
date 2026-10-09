package dao;

import db.DBConnection;
import models.LanPurchase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LanPurchaseDAO {

    // 1. INSERT a LAN purchase — total cost is auto-calculated via band table
    public void addPurchase(LanPurchase lp) {
        String sql = "INSERT INTO lan_purchase " +
                     "(institution_id, num_nodes, total_cost) " +
                     "VALUES (?, ?, ?)";

        double totalCost = LanPurchase.calculateLanCost(lp.getNumNodes());

        if (totalCost == 0) {
            System.out.println("Invalid node count: " + lp.getNumNodes() +
                               ". Must be between 2 and 100.");
            return;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, lp.getInstitutionId());
            ps.setInt(2, lp.getNumNodes());
            ps.setDouble(3, totalCost);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("LAN purchase recorded: " +
                        lp.getNumNodes() + " nodes → KSh " + totalCost);
            }

        } catch (SQLException e) {
            System.out.println("Error adding LAN purchase: " + e.getMessage());
        }
    }

    // 2. SELECT LAN purchases for one institution
    public List<LanPurchase> getByInstitution(int institutionId) {
        List<LanPurchase> list = new ArrayList<>();
        String sql = "SELECT * FROM lan_purchase WHERE institution_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, institutionId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                LanPurchase lp = new LanPurchase();
                lp.setLanId(rs.getInt("lan_id"));
                lp.setInstitutionId(rs.getInt("institution_id"));
                lp.setNumNodes(rs.getInt("num_nodes"));
                lp.setTotalCost(rs.getDouble("total_cost"));
                list.add(lp);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching LAN purchases: " + e.getMessage());
        }
        return list;
    }

    // 3. Total LAN cost for one institution (used in Task 4a/4b)
    public double getTotalLanCostByInstitution(int institutionId) {
        String sql = "SELECT IFNULL(SUM(total_cost), 0) AS total " +
                     "FROM lan_purchase WHERE institution_id = ?";
        double total = 0;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, institutionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) total = rs.getDouble("total");

        } catch (SQLException e) {
            System.out.println("Error summing LAN costs: " + e.getMessage());
        }
        return total;
    }
}