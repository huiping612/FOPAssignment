package com.mycompany.register_login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class register_login {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/loginandregister";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234CJY";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Scanner scanner = new Scanner(System.in);

            System.out.println("Welcome to User Registration and Login System");

            while (true) {
                System.out.println("\n1. Register\n2. Login\n3. Exit");
                System.out.println("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (choice) {
                    case 1:
                        registerUser(scanner);
                        break;
                    case 2:
                        loginUser(scanner);
                        break;
                    case 3:
                        System.out.println("Exiting User Registration and Login System. Goodbye!");
                        scanner.close(); // Close the Scanner
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error: MySQL JDBC Driver not found");
        }
    }

    private static void registerUser(Scanner scanner) {
        System.out.println("\nUser Registration");

        System.out.println("Enter your username: ");
        String username = scanner.nextLine();

        // Check if the username already exists
        if (isUsernameExists(username)) {
            System.out.println("Username already exists. Please choose a different username.");
            return; // Exit the method without attempting registration
        }

        System.out.println("Enter your password: ");
        String password = scanner.nextLine();

        System.out.println("Enter your email address: ");
        String email = scanner.nextLine();

        // Input validation
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            System.out.println("Invalid input. Please provide a non-empty username, password, and email.");
            return; // Exit the method without attempting registration
        }

        // Additional information
        System.out.println("\nEnter additional details:");

        System.out.println("Enter your full name: ");
        String fullName = scanner.nextLine();

        System.out.println("Enter your date of birth (YYYY-MM-DD): ");
        String dateOfBirth = scanner.nextLine();

        System.out.println("Enter your address: ");
        String address = scanner.nextLine();

        System.out.println("Enter your phone number: ");
        String phoneNumber = scanner.nextLine();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO users (username, password, email, full_name, date_of_birth, address, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, password); // Consider hashing the password here
                statement.setString(3, email);
                statement.setString(4, fullName);
                statement.setString(5, dateOfBirth);
                statement.setString(6, address);
                statement.setString(7, phoneNumber);
                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Registration successful! Welcome, " + username + "!");
                } else {
                    System.out.println("Registration failed. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("An error occurred during registration. Please try again.");
        }
    }

    private static boolean isUsernameExists(String username) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("An error occurred while checking username existence.");
            return false;
        }
    }


    private static void loginUser(Scanner scanner) {
        System.out.println("\nUser Login");

        System.out.println("Enter your username: ");
        String username = scanner.nextLine();

        System.out.println("Enter your password: ");
        String password = scanner.nextLine();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, password); // Consider hashing the password here
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        System.out.println("Login successful! Welcome, " + username + "!");
                    } else {
                        System.out.println("Invalid login credentials. Please try again.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("An error occurred during login. Please try again.");
        }
    }

    
    }



