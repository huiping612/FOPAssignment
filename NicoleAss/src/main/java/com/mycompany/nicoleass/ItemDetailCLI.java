/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.nicoleass;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
public class ItemDetailCLI {

    private static final String CSV_FILE_PATH = "D:\\Backup\\Downloads\\Telegram Desktop\\merged_data (2).csv"; // Replace with your CSV file path

    public static void main(String[] args) throws CsvValidationException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter item code:");
        String itemCode = scanner.nextLine();

        List<String[]> items = readCSVFile(CSV_FILE_PATH);
        String[] selectedItem = findItemByCode(items, itemCode);

        if (selectedItem != null) {
            System.out.println("Selected Item:");
            System.out.println("Code: " + selectedItem[2]);
            System.out.println("Name: " + selectedItem[9]);
            System.out.println("Price: " + selectedItem[3]); // Assuming price is at index 3

            boolean exit = false;
            while (!exit) {
                System.out.println("\nSelect actions:");
                System.out.println("1. View item details");
                System.out.println("2. Modify item details");
                System.out.println("3. View top 5 cheapest sellers");
                System.out.println("4. View price trend");
                System.out.println("5. Add to shopping cart");
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
                    case 0:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            }
        } else {
            System.out.println("Item not found!");
        }
    }

    
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

