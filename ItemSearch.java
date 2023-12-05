/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.assignment_new;

/**
 *
 * @author User
 */


import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ItemSearch {

    public static void main(String[] args) throws CsvValidationException {
        Map<String, List<String>> categories = new HashMap<>();
        Map<String, Map<String, String>> items = new HashMap<>();
        Scanner scanner = new Scanner(System.in);
        
        String filePath = "D:\\Backup\\Downloads\\Telegram Desktop\\merged_data (2).csv";
        
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] header = reader.readNext(); // Read the header to skip it

            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length >= 13) {
                    String itemGroup = line[11].trim();
                    String itemCategory = line[12].trim();
                    String itemName = line[9].trim(); // Item name column
                    String itemCode = line[2].trim();

                    categories
                        .computeIfAbsent(itemGroup, k -> new ArrayList<>())
                        .add(itemCategory);

                    items
                        .computeIfAbsent(itemCategory, k -> new HashMap<>())
                        .put(itemName, itemCode);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int choice;
        do {
            choice = displayMainMenu(scanner, categories, items);
            if (choice == 2) {
                browseByCategories(scanner, categories, items);
            } 
            
        } while (choice != 6);

        scanner.close();
    }

    private static int displayMainMenu(Scanner scanner, Map<String, List<String>> categories, Map<String, Map<String, String>> items) {
        System.out.println("PriceTracker - Track Prices with Ease");
        System.out.println("Welcome to Product Search and Selection\n");

        System.out.println("1. Import Data");
        System.out.println("2. Browse by Categories");
        System.out.println("3. Search for a Product");
        System.out.println("4. View Shopping Cart");
        System.out.println("5. Account Settings");
        System.out.println("6. Exit");
        System.out.println();

        System.out.println("Enter your choice (1/2/3/4/5/6) : ");
        return scanner.nextInt();
        
    }

    private static void browseByCategories(Scanner scanner, Map<String, List<String>> categories, Map<String, Map<String, String>> items) {
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
            displaySubcategories(scanner, selectedCategory, categories.get(selectedCategory), items);
        } else if (categoryChoice == index) {
            System.out.println("Back to Main Menu");
        } else {
            System.out.println("Invalid choice. Please enter a valid option.");
        }
    }

    private static void displaySubcategories(Scanner scanner, String category, List<String> subcategories, Map<String, Map<String, String>> items) {
        System.out.println("\nSelect sub category for " + category + " :");
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
            displayItemsForSubcategory(scanner, selectedSubcategory, items);
        } else if (subCategoryChoice == index) {
            System.out.println("Back to Main Menu");
        } else {
            System.out.println("Invalid choice. Please enter a valid option.");
        }
    }

    private static void displayItemsForSubcategory(Scanner scanner, String subcategory, Map<String, Map<String, String>> items) {
    System.out.println("\nItems for " + subcategory + ":");
    int index = 1;
    Map<Integer, String> indexToItem = new HashMap<>();
    for (Map.Entry<String, String> entry : items.get(subcategory).entrySet()) {
        System.out.println(index + ". Code: " + entry.getValue() + " Item name: " + entry.getKey());
        indexToItem.put(index++, entry.getKey());
    }
    System.out.println(index + ". Back to Main Menu\n");

    System.out.println("Enter your choice : ");
    int choice = scanner.nextInt();
    scanner.nextLine(); // Consume newline character

    if (indexToItem.containsKey(choice)) {
        String selectedItem = indexToItem.get(choice);
        System.out.println("Selected Item: " + selectedItem);
        System.out.println("Item name: " + selectedItem + " Code: " + items.get(subcategory).get(selectedItem));
        System.out.println();

    } else if (choice == index) {
        System.out.println("Back to Main Menu");
    } else {
        System.out.println("Invalid choice. Please enter a valid option.");
    }
}

}
