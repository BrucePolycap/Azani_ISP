package dao;

import db.DBConnection;
import models.ContactPerson;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactPersonDAO {

    // 1. INSERT a contact person linked to an institution
    public void addContactPerson(ContactPerson cp) {
        String sql = "INSERT INTO contact_person " +
                     "(institution_id, full_name, phone, email, id_number) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cp.getInstitutionId());
            ps.setString(2, cp.getFullName());
            ps.setString(3, cp.getPhone());
            ps.setString(4, cp.getEmail());
            ps.setString(5, cp.getIdNumber());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Contact person added successfully!");
            } else {
                System.out.println("No rows inserted.");
            }

        } catch (SQLException e) {
            System.out.println("Error adding contact person: " + e.getMessage());
        }
    }

    // 2. SELECT all contact persons for one institution
    public List<ContactPerson> getContactsByInstitution(int institutionId) {
        List<ContactPerson> list = new ArrayList<>();
        String sql = "SELECT * FROM contact_person WHERE institution_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, institutionId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ContactPerson cp = new ContactPerson();
                cp.setContactId(rs.getInt("contact_id"));
                cp.setInstitutionId(rs.getInt("institution_id"));
                cp.setFullName(rs.getString("full_name"));
                cp.setPhone(rs.getString("phone"));
                cp.setEmail(rs.getString("email"));
                cp.setIdNumber(rs.getString("id_number"));
                list.add(cp);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching contacts: " + e.getMessage());
        }
        return list;
    }
}