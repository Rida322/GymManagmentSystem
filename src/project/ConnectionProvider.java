package project;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionProvider {

    public static Connection con() {
        Connection connection = null;
        try {
            // Replace with your actual database info:
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/gymmanagmentsystem"
, // your DB name
                "root",                              // your MySQL username
                "123456"                                   // your MySQL password (if any)
            );
        } catch (Exception e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
        return connection;
    }
}
