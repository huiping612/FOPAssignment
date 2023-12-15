/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author User
 */

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PriceTracker {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/loginandregister";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234CJY";
    private static final String CSV_FILE_PATH = "D:\\Backup\\Downloads\\Telegram Desktop\\merged_data.csv"; // Replace with your CSV file path

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Map<String, Map<String, String>> categoriesDisplay = new HashMap<>();
        Map<String, List<String>> categories = new HashMap<>();
        Map<String, Map<String, String>> items = new HashMap<>();

        try {
            readCSVData(categoriesDisplay, categories, items);
            displayCategories(categoriesDisplay);

            int choice = displayMainMenu(scanner, categories, items);
            handleUserChoice(choice, scanner, categories, items);

        } catch (IOException | CsvValidationException | SQLException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    // All PriceTracker methods are here...
    private static void readCSVData(Map<String, Map<String, String>> categoriesDisplay,
                                    Map<String, List<String>> categories,
                                    Map<String, Map<String, String>> items) throws IOException, CsvValidationException {
        try (CSVReader reader = new CSVReader(new FileReader(CSV_FILE_PATH))) {
            String[] header = reader.readNext();

            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length >= 13) {
                    String itemGroup = line[11].trim();
                    String itemCategory = line[12].trim();
                    String itemName = line[9].trim();
                    String itemCode = line[2].trim();

                    categoriesDisplay.computeIfAbsent(itemGroup, k -> new HashMap<>()).put(itemCategory, "");

                    categories.computeIfAbsent(itemGroup, k -> new ArrayList<>()).add(itemCategory);

                    items.computeIfAbsent(itemCategory, k -> new HashMap<>()).put(itemName, itemCode);
                    
                }
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
        System.out.println("1. Import a data");
        System.out.println("2. Browse by Categories");
        System.out.println("3. Search for a Product");
        System.out.println("4. View Shopping Cart");
        System.out.println("5. Account Settings");
        System.out.println("6. Exit");
        System.out.println();

        System.out.println("Enter your choice (1/2/3/4/5/6) : ");
        return scanner.nextInt();
    }

    private static void handleUserChoice(int choice, Scanner scanner, Map<String, List<String>> categories, Map<String, Map<String, String>> items) throws SQLException {
        while (choice != 6) {
            switch (choice) {
                case 1:
                    // Import data logic
                    break;
                case 2:
                    browseByCategories(scanner, categories, items);
                    break;
                case 3:
                    // Search for a product logic
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
                    break; // Exit the loop and return to the main menu
                }
            } else if (categoryChoice == index) {
                System.out.println("Back to Main Menu");
                break; // Exit the loop and return to the main menu
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }
    
    private static void modifyAccountSettings(Scanner scanner) throws SQLException {
        System.out.println("Choose an action:");
        System.out.println("1. Modify Username");
        System.out.println("2. Modify Password");
        System.out.print("Enter your choice (1 or 2): ");
        int way = scanner.nextInt();

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

    private static void modifyPassword(Scanner scanner) throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
                PreparedStatement statement = connection.prepareStatement("UPDATE users SET password = ? WHERE username = ?")) {

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
            scanner.nextLine(); // Consume newline character

            if (indexToSubcategory.containsKey(subCategoryChoice)) {
                String selectedSubcategory = indexToSubcategory.get(subCategoryChoice);
                boolean returnToMainMenu = displayItemsForSubcategory(scanner, selectedSubcategory, items.get(selectedSubcategory));
                if (returnToMainMenu) {
                    return true; // Signal to return to the main menu
                }
            } else if (subCategoryChoice == index) {
                System.out.println("Back to Main Menu");
                return true; // Signal to return to the main menu
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

                    // Call ItemDetailsCLI
                    try {
                        ItemDetailsCLI.main(new String[]{itemsForCategory.get(selectedItem)});
                    } catch (CsvValidationException ex) {
                        Logger.getLogger(PriceTracker.class.getName()).log(Level.SEVERE, null, ex);
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



    static class ItemDetailsCLI {
        
        public static void main(String[] args) throws CsvValidationException {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter item code:");
            String itemCode = scanner.nextLine();

            List<String[]> items = readCSVFile(CSV_FILE_PATH);
            String[] selectedItem = findItemByCode(items, itemCode);

            if (selectedItem != null) {
                boolean exit = false;
                while (!exit) {
                    System.out.println("\nSelect actions:");
                    System.out.println("1. View item details");
                    System.out.println("2. Modify item details");
                    System.out.println("3. View top 5 cheapest sellers");
                    System.out.println("4. View price trend");
                    System.out.println("5. Add to shopping cart");
                    System.out.println("6. Return to subcategory menu");
                    System.out.println("0. Exit");

                    int choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    switch (choice) {
                        case 1:
                            displayItemDetails(selectedItem);
                            break;
                        case 2:
                            modifyItemDetails(scanner, selectedItem, items);
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
                        case 6:
                            System.out.println("Returning to the subcategory menu...");
                            return;
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

            scanner.close();
        }


        // All methods from ItemDetailsCLI class remain unchanged...
        private static List<String[]> readCSVFile(String filePath) throws CsvValidationException {
        List<String[]> data = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

     private static String[] findItemByCode(List<String[]> items, String code) {
        for (String[] item : items) {
            if (item[2].equalsIgnoreCase(code)) {
                return item;
            }
        }
        return null;
    }


    private static void displayItemDetails(String[] item) {
        System.out.println();
        System.out.println("Item details:");
        System.out.println("Code: " + item[2]);
        System.out.println("Name: " + item[9]);
        System.out.println("Premise: " + item[4]); // Assuming premise is at index 2
        System.out.println("Price: " + item[3]); // Assuming price is at index 3
        // Display other details as needed
    }


    private static void modifyItemDetails(Scanner scanner, String[] selectedItem, List<String[]> items) {
        System.out.println("\nModifying selected item:");
        System.out.println("1. Modify Name");
        System.out.println("2. Modify Premise");
        System.out.println("3. Modify Price");
        System.out.println("0. Go Back");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                System.out.println("Enter new name:");
                String newName = scanner.nextLine();
                selectedItem[1] = newName; // Assuming name is at index 1
                System.out.println("Name updated successfully.");
                break;
            case 2:
                System.out.println("Enter new premise:");
                String newPremise = scanner.nextLine();
                selectedItem[2] = newPremise; // Assuming premise is at index 2
                System.out.println("Premise updated successfully.");
                break;
            case 3:
                System.out.println("Enter new price:");
                String newPrice = scanner.nextLine();
                selectedItem[3] = newPrice; // Assuming price is at index 3
                System.out.println("Price updated successfully.");
                break;
            case 0:
                System.out.println("Going back...");
                break;
            default:
                System.out.println("Invalid choice. Please enter a valid option.");
        }

        // Update the CSV file with the modified information
        writeCSVFile(CSV_FILE_PATH, items);
    }

    // Method to write data to CSV file
    private static void writeCSVFile(String filePath, List<String[]> items) {
        // Write the updated items list back to the CSV file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] item : items) {
                bw.write(String.join(",", item));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Other methods like viewTopCheapestSellers, viewPriceTrend, addToShoppingCart
    // These methods can be implemented based on your requirements

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
}
