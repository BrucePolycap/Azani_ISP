package dao;

import db.DBConnection;
import models.Infrastructure;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InfrastructureDAO {

    // 1. INSERT infrastructure details for an institution
    public void addInfrastructure(Infrastructure infra) {
        String sql = "INSERT INTO infrastructure " +
                     "(institution_id, num_users, num_computers, num_lan_nodes, has_lan) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, infra.getInstitutionId());
            ps.setInt(2, infra.getNumUsers());
            ps.setInt(3, infra.getNumComputers());
            ps.setInt(4, infra.getNumLanNodes());
            ps.setBoolean(5, infra.isHasLan());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Infrastructure recorded for institution " + infra.getInstitutionId());
            }

        } catch (SQLException e) {
            System.out.println("Error adding infrastructure: " + e.getMessage());
        }
    }

    // 2. SELECT infrastructure for one institution
    public Infrastructure getInfrastructureByInstitution(int institutionId) {
        String sql = "SELECT * FROM infrastructure WHERE institution_id = ?";
        Infrastructure infra = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, institutionId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                infra = new Infrastructure();
                infra.setInfrastructureId(rs.getInt("infra_id"));
                infra.setInstitutionId(rs.getInt("institution_id"));
                infra.setNumUsers(rs.getInt("num_users"));
                infra.setNumComputers(rs.getInt("num_computers"));
                infra.setNumLanNodes(rs.getInt("num_lan_nodes"));
                infra.setHasLan(rs.getBoolean("has_lan"));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching infrastructure: " + e.getMessage());
        }
        return infra;
    }

    // 3. SELECT all infrastructure (used in Task 3d)
    public List<Infrastructure> getAllInfrastructure() {
        List<Infrastructure> list = new ArrayList<>();
        String sql = "SELECT * FROM infrastructure";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Infrastructure infra = new Infrastructure();
                infra.setInfrastructureId(rs.getInt("infra_id"));
                infra.setInstitutionId(rs.getInt("institution_id"));
                infra.setNumUsers(rs.getInt("num_users"));
                infra.setNumComputers(rs.getInt("num_computers"));
                infra.setNumLanNodes(rs.getInt("num_lan_nodes"));
                infra.setHasLan(rs.getBoolean("has_lan"));
                list.add(infra);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching all infrastructure: " + e.getMessage());
        }
        return list;
    }
}