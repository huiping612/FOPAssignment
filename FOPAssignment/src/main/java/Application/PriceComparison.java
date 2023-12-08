package Application;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import Model.Item;
import Model.Premise;
import Model.PremiseWithPrice;
import Model.PriceCatcher;
import com.opencsv.exceptions.CsvValidationException;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PriceComparison {

    public static void main(String[]args){
        String filePath="C:\\Users\\USER\\Downloads\\Telegram Desktop\\merged_data.csv";
        
        //Create list to store data
        List <Item> items = new ArrayList<>();
        List <Premise> premises = new ArrayList<>();
        List <PriceCatcher> priceCatchers = new ArrayList<>();
          
        Scanner scanner = new Scanner (System.in);
        readDataFromCSV(filePath,items,premises,priceCatchers);
        search(items,premises,priceCatchers,scanner);
            
    }
    
    private static void readDataFromCSV(String filePath, List<Item> items, List<Premise> premises, List<PriceCatcher> priceCatchers){
        
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            
            String [] nextLine;
            try {
                while ((nextLine = reader.readNext()) != null) {
                    Item item = new Item(Integer.parseInt(nextLine[0]),nextLine[1], nextLine[2], nextLine[3], nextLine[4]);
                    Premise premise = new Premise (Integer.parseInt(nextLine[0]), nextLine[1], nextLine[2], nextLine[3], nextLine[4], nextLine[5]);
                    PriceCatcher priceCatcher = new PriceCatcher (LocalDate.parse(nextLine[0]), Integer.parseInt(nextLine[1]), Integer.parseInt(nextLine[2]), Double.parseDouble(nextLine[3]));
                    
                    items.add(item);
                    premises.add(premise);
                    priceCatchers.add(priceCatcher);
                }
            } catch (CsvValidationException ex) {
                Logger.getLogger(PriceComparison.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static void search(List <Item> items, List <Premise> premises, List <PriceCatcher> priceCatchers, Scanner scanner) {

        String userInput = "";
        while (userInput.isEmpty()) {
            System.out.println("Which Item You Like To Search?");
            userInput = scanner.nextLine();

            if (userInput.isEmpty()) {
                System.out.println("Please enter a non-empty string.");
            }
        }
        System.out.println("Press Enter To Continue");
        scanner.nextLine();//To Prevent System Select Empty For Us
        System.out.println("Please Waiting For Result of Item Search: " + userInput);

        String result = null;
        // Pause the program for 5 seconds
        try {
            Thread.sleep(5000); // 5000 milliseconds = 5 seconds
            result = priceComparison(userInput, items, premises, priceCatchers);
        } catch (InterruptedException ex) {
            System.err.println("Thread sleep interrupted: " + ex.getMessage());
        } catch (SearchException ex) {
            throw new RuntimeException(ex.getMessage());
        }

        // Display the result after the delay
        System.out.println("Result is now available!");
        System.out.println(result);

    }

    public static String priceComparison(String userInput, List<Item> items, List<Premise> premises, List<PriceCatcher> priceCatchers) throws SearchException {
        if (userInput.isEmpty()) {
            throw new SearchException("Search query cannot be empty.");
        }
        
        //Query Item From Item List
        Item item = items.stream()
                .filter(x -> x.getItem().equals(userInput))  //Select the element that match a given condition
                .findFirst()
                .orElse(null);
        if (item == null) {
            throw new SearchException("Item Not Found.");
        }
        
        //Query PriceCatcher From PriceCatchers
        List<PriceCatcher> priceCatcherSelected = priceCatchers.stream()
            .filter(x -> x.getItem_Code() == item.getItem_Code())
            .sorted(Comparator.comparingDouble(PriceCatcher::getPrice))
            .collect(Collectors.toList());

        // Get Distinct PremiseCodes
        List<Integer> distinctPremiseCodes = priceCatcherSelected.stream()
                .map(PriceCatcher::getPremise_Code)
                .distinct()  //Remove the duplicate values from the stream
                .limit(5)  //Get the first five distinct premise code
                .collect(Collectors.toList());

        //Get Premise Codes map with Distinct Price
        Map<Integer, Double> PermiseMatchPrice = new HashMap<>();
        for (Integer premiseCode : distinctPremiseCodes) {
            Double price = priceCatcherSelected.stream()
                    .filter(x -> x.getPremise_Code() == premiseCode)
                    .map(PriceCatcher::getPrice)
                    .findFirst() //Get the first matching price
                    .orElse(null); //If not found, you can provide a default value or handle accordingly

            PermiseMatchPrice.put(premiseCode, price); 
        }
        List<Map.Entry<Integer, Double>> sortedPermiseMatchPrice = PermiseMatchPrice.entrySet().stream()
                .sorted(Map.Entry.comparingByValue()) //Sort the price in ascending 
                .collect(Collectors.toList());

        // Query premises based on premise codes
        List<Premise> premiseSelected = premises.stream()
                .filter(x -> distinctPremiseCodes.contains(x.getPremise_Code()))
                .collect(Collectors.toList());

        //Merge Premise With Price
        List<PremiseWithPrice> premiseWithPrice = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : sortedPermiseMatchPrice) {
            Premise matchingPremise = premiseSelected.stream()
                    .filter(x -> x.getPremise_Code() == entry.getKey())
                    .findFirst()
                    .orElse(null);
            PremiseWithPrice aPremiseWithPrice = new PremiseWithPrice(
                    matchingPremise.getPremise_Code(),
                    matchingPremise.getPremise(),
                    matchingPremise.getAddress(),
                    matchingPremise.getPremise_type(),
                    matchingPremise.getState(),
                    matchingPremise.getDistrict(),
                    entry.getValue()
            );
            premiseWithPrice.add(aPremiseWithPrice);
        }

        //Build String
        int numberOfRecordFound = premiseWithPrice.size();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Top 5 Cheapest Sellers for " + userInput + "\n");
        for (int i = 0; i < numberOfRecordFound; i++) {
            stringBuilder.append("\n");
            stringBuilder.append(i + 1 + "\tRetailer " + premiseWithPrice.get(i).premise + "\n");
            stringBuilder.append("\tPrice: $" + premiseWithPrice.get(i).price + "\n");
            stringBuilder.append("\tAddress: " + premiseWithPrice.get(i).address +premiseWithPrice.get(i).district+", "+premiseWithPrice.get(i).state+ "\n");
        }
        return stringBuilder.toString();
    }


    private String priceComparison(String userInput, Item item, Premise premise, PriceCatcher priceCatcher) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    static class SearchException extends Exception {

        public SearchException(String message) {
            super(message);
        }
    }
}
