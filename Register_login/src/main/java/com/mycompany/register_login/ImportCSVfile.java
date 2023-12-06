package com.mycompany.register_login;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;

public class ImportCSVfile {

    public static void main(String[] args) throws CsvValidationException {
        String filePath = "src/main/resources/merged_data.csv"; 
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                // Process each line here
                for (String value : nextLine) {
                    System.out.print(value + " ");
                }
                System.out.println(); // Move to the next line
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
}
