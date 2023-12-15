/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.nicoleass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
public class ModifyAcc {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/loginandregister";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234CJY";

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Choose an action:");
            System.out.println("1. Modify Username");
            System.out.println("2. Modify Password");
            System.out.print("Enter your choice (1 or 2): ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    modifyUsername();
                    break;
                case 2:
                    modifyPassword();
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1 or 2.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void modifyUsername() throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement("UPDATE users SET username = ? WHERE username = ?")) {

            Scanner scanner = new Scanner(System.in);
            System.out.println("\nModify Username");
            System.out.print("Enter your current username: ");
            String currentUsername = scanner.nextLine();

            System.out.print("Enter your new username: ");
            String newUsername = scanner.nextLine();

            statement.setString(1, newUsername);
            statement.setString(2, currentUsername);
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Username updated successfully to " + newUsername + "!");
            } else {
                System.out.println("Failed to update username. Please try again.");
            }
        }
    }

    private static void modifyPassword() throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement("UPDATE users SET password = ? WHERE username = ?")) {

            Scanner scanner = new Scanner(System.in);
            System.out.println("\nModify Password");
            System.out.print("Enter your username: ");
            String username = scanner.nextLine();

            System.out.print("Enter your new password: ");
            String newPassword = scanner.nextLine();

            
            statement.setString(1, newPassword);
            statement.setString(2, username);
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Password updated successfully for user " + username + "!");
            } else {
                System.out.println("Failed to update password. Please try again.");
            }
        }
    }
}


