import db.DBConnection;
import java.sql.Connection;

public class DBTest {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("DBConnection works!");
            System.out.println("Connected to: " + conn.getCatalog());
        } catch (Exception e) {
            System.out.println("Failed: " + e.getMessage());
        }
    }
}