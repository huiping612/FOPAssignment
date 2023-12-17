/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.register_login;
package com.mycompany.register_login;
import java.sql.*;
public class readdata {

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/fopassignment";
        String username = "root";
        String password = "1234CJY";

        return DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) {
        try {
            // Establish connection
            Connection conn = getConnection();

            // Create statement
            Statement stmt = conn.createStatement();

            // Execute query
            ResultSet rs = stmt.executeQuery("SELECT * FROM importcsv");

            // Close resources
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


