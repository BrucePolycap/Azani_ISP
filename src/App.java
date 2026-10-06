import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        // 1. Database credentials
        String url = "jdbc:mysql://localhost:3306/azani_isp?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String user = "root";
        String password = "p01ycap";

        // 2. Try to connect
        System.out.println("Attempting to connect to the Azani database...");
        
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("SUCCESS! Connected to the database.");
            System.out.println("Database Name: " + conn.getCatalog());
        } catch (SQLException e) {
            System.out.println("FAILED to connect.");
            System.out.println("Error: " + e.getMessage());
        }
    }
}