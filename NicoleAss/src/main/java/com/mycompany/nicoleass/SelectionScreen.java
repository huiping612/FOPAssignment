/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.nicoleass;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



public class SelectionScreen {
    public static void main(String[] args) throws CsvValidationException {
        String filePath = "D:\\Backup\\Downloads\\Telegram Desktop\\merged_data (2).csv";
        Map<String, Map<String, String>> categories = new HashMap<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] header = reader.readNext(); // Read the header to skip it

            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length >= 13) {
                    String itemGroup = line[11].trim();
                    String itemCategory = line[12].trim();

                    categories
                            .computeIfAbsent(itemGroup, k -> new HashMap<>())
                            .put(itemCategory, "");
                }
            }

            // Display item groups and their categories
            displayCategories(categories);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void displayCategories(Map<String, Map<String, String>> categories) {
        System.out.println("Available Item Groups:");
        for (String group : categories.keySet()) {
            System.out.println("\nGroup: " + group);
            System.out.println("Categories:");
            Map<String, String> subCategories = categories.get(group);
            subCategories.keySet().forEach(category -> System.out.println("- " + category));
        }
    }
}
