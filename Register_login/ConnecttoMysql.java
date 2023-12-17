/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.register_login;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnecttoMysql {


    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/fopassignment";
        String username = "root";
        String password = "1234CJY";
        
        return DriverManager.getConnection(url, username, password);
    }


    public static void main(String[] args) {
        try {
            // Establish connection
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fopassignment", "root", "1234CJY");
            
            // Create statement
            Statement stmt = conn.createStatement();
            
            // Execute query
            ResultSet rs = stmt.executeQuery("SELECT * FROM fopassignment.importcsv");

            // Print data
            while (rs.next()) {
                // Retrieve data by column name
                String date = rs.getString("date");
                int premiseCode = rs.getInt("premise_code");
                int itemCode = rs.getInt("item_code");
                double price = rs.getDouble("price");
                String premise = rs.getString("premise");
                String address = rs.getString("address");
                String premiseType = rs.getString("premise_type");
                String state = rs.getString("state");
                String district = rs.getString("district");
                String item = rs.getString("item");
                String unit = rs.getString("unit");
                String itemGroup = rs.getString("item_group");
                String itemCategory = rs.getString("item_category");
                
                // Print or process the retrieved data
                System.out.println("Date: " + date +
                                   ", Premise Code: " + premiseCode +
                                   ", Item Code: " + itemCode +
                                   ", Price: " + price +
                                   ", Premise: " + premise +
                                   ", Address: " + address +
                                   ", Premise Type: " + premiseType +
                                   ", State: " + state +
                                   ", District: " + district +
                                   ", Item: " + item +
                                   ", Unit: " + unit +
                                   ", Item Group: " + itemGroup +
                                   ", Item Category: " + itemCategory);
                // Print other rows similarly
            }

            // Close resources
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

  


