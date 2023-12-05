import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ImportCSVfile {
    public static void main(String[] args) {
        String filePath = "..\\..\\..\\Downloads\\merged_data.csv"; // Replace with your file path

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Splitting the line by comma (CSV format)
                String[] values = line.split(",");
                
                // Process the values or do something with each line
                for (String value : values) {
                    System.out.print(value + "\t"); // Print each value (tab-separated)
                }
                System.out.println(); // Move to the next line for the next row
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
