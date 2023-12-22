/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.register_login;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class trynew3 {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/fopassignment";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234CJY";
   
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

             Scanner scanner = new Scanner(System.in);
            boolean loggedIn = false;

            System.out.println("Welcome to User Registration and Login System");
          
            while (!loggedIn) {
                System.out.println("\n1. Register\n2. Login\n3. Exit");
                System.out.println("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (choice) {
                    case 1:
                        registerUser(scanner);
                        break;
                    case 2:
                        loggedIn = loginUser(scanner);
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
     private static void redirectToHomePage(String username) {
        // Here, you can define what should happen after successful login.
        // For example, you can display a welcome message or redirect to another page.
        System.out.println("Welcome, " + username + "! You have successfully logged in.");
        Scanner scanner = new Scanner(System.in);

        Map<String, Map<String, String>> categoriesDisplay = new HashMap<>();
        Map<String, List<String>> categories = new HashMap<>();
        Map<String, Map<String, String>> items = new HashMap<>();

        try {
            Scanner sc=new Scanner(System.in);
            readMySQLData(categoriesDisplay, categories, items);
            int choice = displayMainMenu(scanner, categories, items);
            handleUserChoice(choice, scanner, categories, items);
            scanner.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add your code to navigate to another page or perform specific actions.
        // This could be displaying a menu, redirecting to a different part of the application, etc.
    }
private static void readMySQLData(Map<String, Map<String, String>> categoriesDisplay,
                                Map<String, List<String>> categories,
                                Map<String, Map<String, String>> items) throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
            Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT item_group, item_category, item, item_code FROM importcsv");

            while (resultSet.next()) {
                String itemGroup = resultSet.getString("item_Group");
                String itemCategory = resultSet.getString("item_Category");
                String itemName = resultSet.getString("item");
                String itemCode = resultSet.getString("item_code");

                categoriesDisplay.computeIfAbsent(itemGroup, k -> new HashMap<>()).put(itemCategory, "");

                categories.computeIfAbsent(itemGroup, k -> new ArrayList<>()).add(itemCategory);

                items.computeIfAbsent(itemCategory, k -> new HashMap<>()).put(itemName, itemCode);
            }
        }
    }


    private static void displayCategories(Map<String, Map<String, String>> categoriesDisplay) {
        System.out.println("Available Item Groups:");
            for (String group : categoriesDisplay.keySet()) {
                System.out.println("\nGroup: " + group);
                System.out.println("Categories:");
                Map<String, String> subCategories = categoriesDisplay.get(group);
                subCategories.keySet().forEach(category -> System.out.println("- " + category));
            }
    }

    private static int displayMainMenu(Scanner scanner, Map<String, List<String>> categories, Map<String, Map<String, String>> items) {
        System.out.println();
        System.out.println("PriceTracker - Track Prices with Ease");
        System.out.println("Welcome to Product Search and Selection\n");
        System.out.println("1. Show Categories and Groups ");
        System.out.println("2. Browse by Categories");
        System.out.println("3. Search for a Product");//fuzzy search
        System.out.println("4. View Shopping Cart");
        System.out.println("5. Account Settings");
        System.out.println("6. Exit");
        System.out.println();

        System.out.println("Enter your choice (1/2/3/4/5/6) : ");
        return scanner.nextInt();
    }


private static void handleUserChoice(int choice, Scanner scanner, Map<String, List<String>> categories, Map<String, Map<String, String>> items) throws SQLException {
        Map<String, Map<String, String>> categoriesDisplay = new HashMap<>();
        while (choice != 6) {
            switch (choice) {
                case 1:
                        for (Map.Entry<String, List<String>> entry : categories.entrySet()) {
                            String itemGroup = entry.getKey();
                            List<String> itemCategories = entry.getValue();

                            for (String itemCategory : itemCategories) {
                                categoriesDisplay.computeIfAbsent(itemGroup, k -> new HashMap<>()).put(itemCategory, "");
                            }
                        }

                        displayCategories(categoriesDisplay);
                    break;
                case 2:
                    browseByCategories(scanner, categories, items);
                    break;
                case 3:
                    fuzzySearch();
                    break;
                case 4:
                    // View Shopping Cart logic
                    break;
                case 5:
                    modifyAccountSettings(scanner);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }
            if (choice != 6) {
                choice = displayMainMenu(scanner, categories, items);
            } else {
                System.out.println("Exiting...");
            }
        }
    }

    private static void fuzzySearch() {
    try {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Search a product:");
        String searchTerm = scanner.nextLine();

        // Establish connection
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT item, item_code, unit FROM importcsv WHERE item LIKE UPPER(?) OR item_code LIKE UPPER(?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, "%" + searchTerm + "%");
                statement.setString(2, "%" + searchTerm + "%");

                try (ResultSet resultSet = statement.executeQuery()) {
                    Map<String, String> uniqueItemsMap = new LinkedHashMap<>(); // Use a Map to store unique items based on item code
                    while (resultSet.next()) {
                        String itemName = resultSet.getString("item");
                        String itemCode = resultSet.getString("item_code");
                        String unit = resultSet.getString("unit");

                        // Store in map with item code as key to maintain uniqueness
                        if (!uniqueItemsMap.containsKey(itemCode)) {
                            uniqueItemsMap.put(itemCode, itemName + unit);
                            System.out.println(itemCode + ". " + itemName + " - "+ unit);
                        }
                    }

                    if (uniqueItemsMap.isEmpty()) {
                        System.out.println("No items found.");
                    } else {
                        // Check if the entered item code exists in the map
                            String selectedCode = uniqueItemsMap.keySet().iterator().next();
                            String selectedItem = uniqueItemsMap.get(selectedCode);
                            ItemDetailsCLI.main(new String[]{selectedItem, selectedCode}); // Call ItemDetailsCLI with selected item details
                        } 
                        
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    
}





    private static void browseByCategories(Scanner scanner, Map<String, List<String>> categories, Map<String, Map<String, String>> items) {
        while (true) {
            System.out.println("\nSelect a Category :");
            int index = 1;
            Map<Integer, String> indexToCategory = new HashMap<>();
            for (String itemGroup : categories.keySet()) {
                System.out.println(index + ". " + itemGroup);
                indexToCategory.put(index++, itemGroup);
            }
            System.out.println(index + ". Back to Main Menu\n");

            System.out.println("Enter your choice : ");
            int categoryChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            if (indexToCategory.containsKey(categoryChoice)) {
                String selectedCategory = indexToCategory.get(categoryChoice);
                boolean returnToMainMenu = displaySubcategories(scanner, selectedCategory, categories.get(selectedCategory), items);
                if (returnToMainMenu) {
                    break; 
                }
            } else if (categoryChoice == index) {
                System.out.println("Back to Main Menu");
                break; 
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }
    
    private static void modifyAccountSettings(Scanner scanner) throws SQLException {
        System.out.println("Choose an action:");
        System.out.println("1. Modify Username");
        System.out.println("2. Modify Password");
        System.out.println("Enter your choice (1 or 2): ");
        int way = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        switch (way) {
            case 1:
                modifyUsername(scanner);
                break;
            case 2:
                modifyPassword(scanner);
                break;
            default:
                System.out.println("Invalid choice. Please enter 1 or 2.");
        }
    }


    private static void modifyUsername(Scanner scanner) throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
                PreparedStatement statement = connection.prepareStatement("UPDATE users SET username = ? WHERE username = ?")) {

                System.out.println("\nModify Username");
                System.out.println("Enter your current username: ");
                String currentUsername = scanner.nextLine();

                System.out.println("Enter your new username: ");
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

    private static void modifyPassword(Scanner scanner) throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
                PreparedStatement statement = connection.prepareStatement("UPDATE users SET password = ? WHERE username = ?")) {

                System.out.println("\nModify Password");
                System.out.println("Enter your username: ");
                String username = scanner.nextLine();

                System.out.println("Enter your new password: ");
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


    private static boolean displaySubcategories(Scanner scanner, String category, List<String> subcategories, Map<String, Map<String, String>> items) {
        while (true) {
            System.out.println("\nSelect subcategory for " + category + " :");
            Set<String> uniqueSubcategories = new HashSet<>(subcategories);
            int index = 1;
            Map<Integer, String> indexToSubcategory = new HashMap<>();
            for (String subcategory : uniqueSubcategories) {
                System.out.println(index + ". " + subcategory);
                indexToSubcategory.put(index++, subcategory);
            }
            System.out.println(index + ". Back to Main Menu\n");

            System.out.println("Enter your choice : ");
            int subCategoryChoice = scanner.nextInt();
            scanner.nextLine(); 

            if (indexToSubcategory.containsKey(subCategoryChoice)) {
                String selectedSubcategory = indexToSubcategory.get(subCategoryChoice);
                boolean returnToMainMenu = displayItemsForSubcategory(scanner, selectedSubcategory, items.get(selectedSubcategory));
                if (returnToMainMenu) {
                    return true; 
                }
            } else if (subCategoryChoice == index) {
                System.out.println("Back to Main Menu");
                return true; 
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static boolean displayItemsForSubcategory(Scanner scanner, String category, Map<String, String> itemsForCategory) {
        if (itemsForCategory != null) {
            boolean returnToCategoryMenu = false;
            while (!returnToCategoryMenu) {
                System.out.println("\nItems for " + category + ":");
                int index = 1;
                Map<Integer, String> indexToItem = new HashMap<>();
                for (Map.Entry<String, String> entry : itemsForCategory.entrySet()) {
                    System.out.println(index + ". Item name: " + entry.getKey());
                    indexToItem.put(index++, entry.getKey());
                }
                System.out.println(index + ". Back to Category Menu\n");

                System.out.println("Enter your choice : ");
                int choice;
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    continue;
                }


            if (indexToItem.containsKey(choice)) {
                    String selectedItem = indexToItem.get(choice);
                    System.out.println("Selected Item: " + selectedItem);
                    System.out.println("Code: " + itemsForCategory.get(selectedItem));
                    System.out.println();
                   
                    try {//singleton
                        ItemDetailsCLI.main(new String[]{itemsForCategory.get(selectedItem)});
                    } catch (SQLException ex) {
                        Logger.getLogger(trynew3.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else if (choice == index) {
                    System.out.println("Back to Category Menu");
                    returnToCategoryMenu = true; // Signal to return to the category menu
                } else {
                    System.out.println("Invalid choice. Please enter a valid option.");
                }
            }
        } else {
            System.out.println("No items found for the category.");
        }
        return false;
    }


    private static boolean loginUser(Scanner scanner) {
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
                        redirectToHomePage(username);
                        return true;
                    } else {
                        System.out.println("Invalid login credentials. Please try again.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("An error occurred during login. Please try again.");
        }
        return false;
    }

    
    }





