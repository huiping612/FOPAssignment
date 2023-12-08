package Application;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import Model.Item;
import Model.Premise;
import Model.PremiseWithPrice;
import Model.PriceCatcher;
import com.opencsv.exceptions.CsvValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PriceTrend {

    public static void main(String[]args){
        
        String filePath="C:\\Users\\USER\\Downloads\\Telegram Desktop\\merged_data.csv";
        
        //Create list to store data
        List <Item> items = new ArrayList<>();
        List <Premise> premises = new ArrayList<>();
        List <PriceCatcher> priceCatchers = new ArrayList<>();
          
        Scanner scanner = new Scanner (System.in);
        readDataFromCSV(filePath,items,premises,priceCatchers);
        PriceTrend priceTrend = new PriceTrend();
        priceTrend.getTrend(items, premises, priceCatchers,scanner);
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
                Logger.getLogger(PriceTrend.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
    //Get Trend Function - Param All the Excel File
    public void getTrend(List<Item> items, List<Premise> premises, List<PriceCatcher> priceCatchers, Scanner scanner) {
        
        //Get User Input (Item)
        String userInput = "";
        while (userInput.isEmpty()) {
            System.out.println("Which Item You Like To Get Trend?");
            userInput = scanner.nextLine();

            if (userInput.isEmpty()) {
                System.out.println("Please enter a non-empty string.");
            }
        }
        System.out.println("Press Enter To Continue");
        scanner.nextLine();//To Prevent System Select Empty For Us
        System.out.println("Please Waiting For Result of Item Search: " + userInput);

        String result = null;
        try {
            Thread.sleep(5000); // 5000 milliseconds = 5 seconds
            generateTrend(userInput, items, premises, priceCatchers, scanner);
        } catch (InterruptedException ex) {
            System.err.println("Thread sleep interrupted: " + ex.getMessage());
        } catch (PriceTrend.SearchException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }

    public void generateTrend(String userInput, List<Item> items, List<Premise> premises, List<PriceCatcher> priceCatchers, Scanner scanner) throws SearchException {

        if (userInput.isEmpty()) {
            throw new SearchException("Search query cannot be empty.");
        }

        // Query Item From Item List
        Item item = items.stream()
                .filter(x -> x.getItem().equals(userInput))
                .findFirst()
                .orElse(null);

        if (item == null) {
            throw new SearchException("Item Not Found.");
        }

        //Query PriceCatcher From PriceCatchers
        List<PriceCatcher> priceCatcherSelected = priceCatchers.stream()
                .filter(x -> x.getItem_Code() == item.item_Code)
                .sorted(Comparator.comparingDouble(PriceCatcher::getPrice))
                .collect(Collectors.toList());

        // Get Distinct PremiseCodes
        List<Integer> distinctPremiseCodes = priceCatcherSelected.stream()
                .map(PriceCatcher::getPremise_Code)
                .distinct()
                .collect(Collectors.toList());

        //query all premise code
        List<Premise> premiseSelected = premises.stream()
                .filter(x -> distinctPremiseCodes.contains(x.getPremise_Code()))
                .collect(Collectors.toList());

        List<PremiseWithPrice> premiseWithPrice = new ArrayList<>();
        HashSet<String> premiseName = new HashSet<>();
        for (PriceCatcher priceCatcher : priceCatcherSelected) {
            Premise aPremiseSelected = premiseSelected.stream()
                    .filter(x -> x.getPremise_Code() == priceCatcher.getPremise_Code())
                    .findFirst()
                    .orElse(null);

            if (aPremiseSelected != null) {
                PremiseWithPrice aPremiseWithPrice = new PremiseWithPrice();
                aPremiseWithPrice.premise = aPremiseSelected.premise;
                premiseName.add(aPremiseWithPrice.premise);
                aPremiseWithPrice.date = priceCatcher.getDate();
                aPremiseWithPrice.price = priceCatcher.getPrice();
                premiseWithPrice.add(aPremiseWithPrice);
            }
        }

        System.out.println("Which Permise You Like To Get Trend?\n");

        Iterator<String> iterator = premiseName.iterator();
        while (iterator.hasNext()) {
            String premise = iterator.next();
            System.out.print(premise + ",\t");
        }

        System.out.print("\n");
        String permiseName = "";
        while (permiseName.isEmpty()) {
            System.out.print("Select 1 of The Premise From The List");
            permiseName = scanner.nextLine();

            if (permiseName.isEmpty()) {
                System.out.println("Please enter a non-empty string.");
            }
        }
        System.out.println("Press Enter To Continue");
        scanner.nextLine();//To Prevent System Select Empty For Us
        System.out.println("Please Waiting For Result of Permise Search: " + permiseName);

        try {
            Thread.sleep(5000); // 5000 milliseconds = 5 seconds
            //FIlter the premise with price  by user input
            List<PremiseWithPrice> matchingPremises = searchByPremise(premiseWithPrice, permiseName);
            double scale = checkIntervalConsistency(matchingPremises);
            DisplayTrend(matchingPremises, scale);
        } catch (InterruptedException ex) {
            System.err.println("Thread sleep interrupted: " + ex.getMessage());
        } catch (PriceTrend.SearchException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        scanner.close();
    }

    public List<PremiseWithPrice> searchByPremise(List<PremiseWithPrice> premiseWithPriceList, String permiseName) throws SearchException {
        //The PremiseWithPriceList Contain A Huge List of Different Premise Name For That Particular Item So Now Need to Filter the List Based On the User Selected Premise Name
        List<PremiseWithPrice> returnList = premiseWithPriceList.stream()
                .filter(premiseWithPrice -> premiseWithPrice.getPremise().trim().equalsIgnoreCase(permiseName.trim()))
                .sorted(Comparator.comparing(PremiseWithPrice::getDate))
                .collect(Collectors.toList());

        if (returnList.size() <= 0) { //No Matching Result Found
            throw new SearchException("No Matching Premise Found");
        }
        return returnList;
    }

    public static void DisplayTrend(List<PremiseWithPrice> matchingPremises, double scale) {
        //Calculate the Scale & Display The Output
        double minValue = getMinValue(matchingPremises);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Price Trend Chart For " + matchingPremises.get(0).premise + "\n\n");
        stringBuilder.append(String.format("%-10s | %-10s\n", "Days", "Price"));
        stringBuilder.append("--------------------------\n");

        for (PremiseWithPrice aMatchingPremises : matchingPremises) {
            int numberOfDollars = (int) ((aMatchingPremises.price - minValue) / scale) + 3;
            String dollar = getDollarSigns(numberOfDollars);
            String formattedOutput = String.format("%-10s | %-10s\t(%s)\n", aMatchingPremises.getDate(), dollar, aMatchingPremises.getPrice());
            stringBuilder.append(formattedOutput);
        }

        stringBuilder.append("\nScale:\n");
        if (Double.isNaN(scale)) {
            stringBuilder.append(String.format("Inconsistent Interval "));
        } else {
            stringBuilder.append(String.format("$ = RM %.2f", scale));
        }
        System.out.print(stringBuilder.toString());
    }

    private static String getDollarSigns(int numberOfDollars) {
        StringBuilder dollarSigns = new StringBuilder();
        for (int i = 0; i < numberOfDollars; i++) {
            dollarSigns.append("$");
        }
        return dollarSigns.toString();
    }

    private static double getMinValue(List<PremiseWithPrice> matchingPremises) {
        if (matchingPremises == null || matchingPremises.isEmpty()) {
            throw new IllegalArgumentException("List is null or empty");
        }

        double minValue = matchingPremises.get(0).getPrice();

        for (int i = 1; i < matchingPremises.size(); i++) {
            double currentPrice = matchingPremises.get(i).getPrice();
            if (currentPrice < minValue) {
                minValue = currentPrice;
            }
        }

        return minValue;
    }

    private static double checkIntervalConsistency(List<PremiseWithPrice> matchingPremise) {
        double[] uniqueNumbers = matchingPremise.stream()
                .mapToDouble(PremiseWithPrice::getPrice)
                .distinct()
                .toArray();

        Arrays.sort(uniqueNumbers);

        if (uniqueNumbers.length < 2) {
            return 0.00; // Cannot calculate interval with less than 2 unique numbers
        }

        double firstNumber = uniqueNumbers[0];
        double interval = uniqueNumbers[1] - firstNumber;

        for (int i = 2; i < uniqueNumbers.length; i++) {
            double currentInterval = uniqueNumbers[i] - uniqueNumbers[i - 1];
            if (currentInterval != interval) {
                return Double.NaN; // Inconsistent intervals 
            }
        }

        return interval;
    }

    static class SearchException extends Exception {

        //Custom Exception
        public SearchException(String message) {
            super(message);
        }
    }
}
