/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.register_login;

/**
 *
 * @author User
 */

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;



class ItemDetailsCLI {
        
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/fopassignment";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234CJY";
        
        public static void main(String[] args) throws SQLException {
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.println("Enter item code:");
                String itemCode = scanner.nextLine();

                List<String[]> databaseItems = readDataFromDatabase();
                String[] selectedItem = findItemByCode(databaseItems, itemCode);

                if (selectedItem != null) {
                    boolean exit = false;
                    boolean returnToSubcategoryMenu = false;
                    while (!exit) {
                        displayMenu();

                        int choice = getUserChoice(scanner);

                        switch (choice) {
                            case 1:
                                displayItemDetails(selectedItem);
                                break;
                            case 2:
                                modifyItemDetails(scanner, selectedItem);
                                break;
                            case 3:
                                viewTopCheapestSellers(selectedItem);
                                break;
                            case 4:
                                viewPriceTrend(selectedItem);
                                break;
                            case 5:
                                addToShoppingCart(selectedItem);
                                break;
                            case 0:
                                exit = true;
                                System.out.println("Exiting Price Tracker...");
                                System.exit(0);
                                break;
                            default:
                                System.out.println("Invalid choice. Please enter a valid option.");
                        }
                    }
                } else {
                    System.out.println("Item not found!");
                }
            }
        }

        private static void displayMenu() {
            System.out.println("\nSelect actions:");
            System.out.println("1. View item details");
            System.out.println("2. Modify item details");
            System.out.println("3. View top 5 cheapest sellers");
            System.out.println("4. View price trend");
            System.out.println("5. Add to shopping cart");
            System.out.println("0. Exit");
        }


        private static int getUserChoice(Scanner scanner) {
                    int choice;
                    while (true) {
                        System.out.println("Enter your choice:");
                        try {
                            choice = Integer.parseInt(scanner.nextLine());
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a number.");
                        }
                    }
                    return choice;
                }


        
        private static List<String[]> readDataFromDatabase() throws SQLException {
            List<String[]> data = new ArrayList<>();
            String query = "SELECT item_code, item, premise, price, unit, address FROM importcsv"; 

            try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    String itemCode = resultSet.getString("item_code");
                    String itemName = resultSet.getString("item");
                    String premise = resultSet.getString("premise");
                    String price = resultSet.getString("price");
                    String unit = resultSet.getString("unit");
                    String address = resultSet.getString("address");

                    String[] itemArray = {itemCode, itemName, premise, price, unit, address};
                    data.add(itemArray);
                }
            }
            return data;
        }



    private static String[] findItemByCode(List<String[]> databaseItems, String code) {
        for (String[] item : databaseItems) {
            if (item[0].equalsIgnoreCase(code.trim())) {
                return item;
            }
        }
        return null;
    }

    private static void displayItemDetails(String[] item) {
        System.out.println();
        System.out.println("Item details:");
        System.out.println("Code: " + item[0]);
        System.out.println("Name: " + item[1]);
        System.out.println("Unit: " + item[4]); 
        System.out.println("Price: " + item[3]);
        System.out.println("Premise: "+ item[2]);
        System.out.println("Address of Premise: "+ item[5]);

    }


    private static void modifyItemDetails(Scanner scanner, String[] selectedItem) {
        System.out.println("\nModifying selected item:");
        System.out.println("1. Modify Name");
        System.out.println("2. Modify Premise");
        System.out.println("3. Modify Price");
        System.out.println("0. Go Back");

        int choice = scanner.nextInt();
        scanner.nextLine();


        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            switch (choice) {
                case 1:
                    System.out.println("Enter new name:");
                    String newName = scanner.nextLine();
                    selectedItem[1] = newName; 
                    System.out.println("Name updated successfully.");
                    updateDatabase(connection, selectedItem, "item", newName); 
                    break;
                case 2:
                    System.out.println("Enter new premise:");
                    String newPremise = scanner.nextLine();
                    selectedItem[2] = newPremise; 
                    System.out.println("Premise updated successfully.");
                    updateDatabase(connection, selectedItem, "premise", newPremise); 
                    break;
                case 3:
                    System.out.println("Enter new price:");
                    String newPrice = scanner.nextLine();
                    selectedItem[3] = newPrice; 
                    System.out.println("Price updated successfully.");
                    updateDatabase(connection, selectedItem, "price", newPrice); 
                    break;
                case 0:
                    System.out.println("Going back...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
    }
    
    private static void updateDatabase(Connection connection, String[] selectedItem, String columnName, String newValue) throws SQLException {
        String updateQuery = "UPDATE importcsv " + columnName + " = ? WHERE item_code = ?";

        try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            statement.setString(1, newValue); 
            statement.setString(2, selectedItem[2]); 

            statement.executeUpdate(); 
        }
    }


    private static void viewTopCheapestSellers(String[] item) {
        // Implement logic to display top 5 cheapest sellers for the item
    }

    private static void viewPriceTrend(String[] item) {
        // Implement logic to display price trend for the item
    }

    private static void addToShoppingCart(String[] item) {
        // Implement logic to add the item to the shopping cart
    }

    }
