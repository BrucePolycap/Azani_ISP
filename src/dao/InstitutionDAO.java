package dao;

import db.DBConnection;
import models.Institution;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InstitutionDAO {

    // 1. INSERT a new institution and return the auto-generated ID
    public int addInstitution(Institution inst) {
        String sql = "INSERT INTO institution " +
                     "(name, type, location, is_ready, is_disconnected, date_registered) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        int generatedId = -1;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, inst.getName());
            ps.setString(2, inst.getType());
            ps.setString(3, inst.getLocation());
            ps.setBoolean(4, inst.isReady());
            ps.setBoolean(5, inst.isDisconnected());
            ps.setDate(6, Date.valueOf(
                    inst.getDateRegistered() != null ? inst.getDateRegistered() : LocalDate.now()));

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) generatedId = rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error adding institution: " + e.getMessage());
        }
        return generatedId;
    }

    // 2. SELECT all institutions (for Task 3a)
    public List<Institution> getAllInstitutions() {
        List<Institution> list = new ArrayList<>();
        String sql = "SELECT * FROM institution";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Institution inst = new Institution();
                inst.setInstitutionId(rs.getInt("institution_id"));
                inst.setName(rs.getString("name"));
                inst.setType(rs.getString("type"));
                inst.setLocation(rs.getString("location"));
                inst.setReady(rs.getBoolean("is_ready"));
                inst.setDisconnected(rs.getBoolean("is_disconnected"));

                Date d = rs.getDate("date_registered");
                if (d != null) inst.setDateRegistered(d.toLocalDate());

                list.add(inst);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching institutions: " + e.getMessage());
        }
        return list;
    }
}