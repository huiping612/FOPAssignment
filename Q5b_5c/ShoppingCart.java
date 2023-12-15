package Q5b_5c;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

class ShoppingCart {

    // Map to store items in the shopping cart along with their quantities
    private final Map<String, Integer> itemsInCart;
    
    // Constants for CSV headers
    private static final String ITEM_HEADER = "item";
    private static final String PRICE_HEADER = "price";
    private static final String ADDRESS_HEADER = "address";

    // Scanner for user input
    private static final Scanner scanner = new Scanner(System.in);

    // Constructor to initialize the shopping cart
    public ShoppingCart() {
        this.itemsInCart = new HashMap<>();
    }

    // Main method to run the program
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            case5_6(sc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to handle the main functionality of the shopping cart
    public static void case5_6(Scanner sc) {
        ShoppingCart cart = new ShoppingCart();

        do {
            System.out.print("Selected ");
            String itemKeyIn = sc.nextLine().toUpperCase();

            System.out.println("\nSelect actions: ");
            System.out.println("1. View item details");
            System.out.println("2. Modify item details");
            System.out.println("3. View cheapest price");
            System.out.println("4. View price trend");
            System.out.println("5. Add to shopping cart");
            System.out.println("6. View shopping cart");
            System.out.println("7. Exit\n");

            int userChoice;
            try {
                userChoice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (userChoice) {
                case 5:
                    cart.addItem(itemKeyIn);
                    break;
                case 6:
                    cart.viewCart();
                    break;
                case 7:
                    System.out.println("Exiting the program.");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }

            System.out.print("Do you want to perform another action? (yes/no): ");
        } while ("yes".equalsIgnoreCase(sc.nextLine()));
    }

    // Method to add an item to the shopping cart
    public void addItem(String item) {
        itemsInCart.merge(item, 1, Integer::sum);
        System.out.println("Please wait for a moment\n");
        CheapestSeller(item);
        System.out.println(item + " is added to the shopping cart.");
    }

    // Method to find the cheapest seller for a given item
    public static void CheapestSeller(String itemKeyIn) {
        try {
            // Path to the CSV file
            String filePath = "C:\\Users\\User\\Dropbox\\My PC (LAPTOP-SPVCRBCA)\\Documents\\NetBeansProjects\\PriceTracker\\src\\main\\java\\merged_data.csv";
            String searchKeyword = itemKeyIn.toUpperCase();

            // Map to store the cheapest price information for each item
            Map<String, CheapestPriceInfo> cheapestPricesByItem = new HashMap<>();

            // Read the CSV file and parse records
            try (Reader reader = new FileReader(filePath);
                 CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader)) {

                // Iterate through CSV records
                for (CSVRecord csvRecord : csvParser) {
                    String item = csvRecord.get(ITEM_HEADER);

                    // Check if the item contains the search keyword
                    if (item != null && item.toUpperCase().contains(searchKeyword)) {
                        double price = Double.parseDouble(csvRecord.get(PRICE_HEADER));
                        String address = csvRecord.get(ADDRESS_HEADER);

                        CheapestPriceInfo currentCheapest = cheapestPricesByItem.get(item);
                        if (currentCheapest == null || price < currentCheapest.getPrice()) {
                            cheapestPricesByItem.put(item, new CheapestPriceInfo(price, address));
                        }
                    }
                }
            }

            // Display the cheapest price information
            displayCheapestPrice(cheapestPricesByItem);

        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing a number: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    // Method to display the cheapest price information
    private static void displayCheapestPrice(Map<String, CheapestPriceInfo> cheapestPricesByItem) {
        cheapestPricesByItem.forEach((item, info) -> {
            System.out.println("Cheapest Seller for " + item + ":");
            System.out.println("Retailer A ");
            System.out.println("Price: RM" + info.getPrice());
            System.out.println("Address: " + info.getAddress());
            System.out.println();
        });
    }

    // Method to view the contents of the shopping cart
    public void viewCart() {
        if (itemsInCart.isEmpty()) {
            System.out.println("The shopping cart is empty.");
            return;
        }

        System.out.println("Shopping Cart:");
        for (Map.Entry<String, Integer> entry : itemsInCart.entrySet()) {
            System.out.println(entry.getKey() + ": Quantity = " + entry.getValue());
        }

        // Ask the user to select a district
        System.out.println("Which district would you like to select?");
        String district = scanner.nextLine();

        // Find minimum shops to visit for the cheapest prices
        findMinShopsForCheapestPrices(district);
    }

    // Method to find the minimum number of shops to visit for the cheapest prices
    private void findMinShopsForCheapestPrices(String district) {
        Map<String, Double> pricesByItem = new HashMap<>();

        // Iterate through items in the shopping cart
        for (String item : itemsInCart.keySet()) {
            // Check if the item is in the specified district
            if (isItemInDistrict(item, district)) {
                // Get the cheapest price for the item in the district
                double cheapestPrice = getCheapestPriceForItemInDistrict(item, district);
                pricesByItem.put(item, cheapestPrice);
            }
        }

        // Sort items by cheapest prices
        List<Map.Entry<String, Double>> sortedItems = pricesByItem.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());

        // Calculate and display the minimum number of shops to visit
        int minShopsToVisit = calculateMinShopsToVisit(sortedItems);

        // Display cheapest prices for each item
        if (!sortedItems.isEmpty()) {
            System.out.println("Minimal number of shops to visit in district " + district + ": " + minShopsToVisit);
            System.out.println("Cheapest prices for each item:");

            sortedItems.forEach(entry ->
                    System.out.println(entry.getKey() + ": RM" + entry.getValue()));

            // Ask if the user wants to remove any item from the shopping cart
            System.out.print("Do you want to remove any item from the shopping cart? (yes/no): ");
            String removeChoice = scanner.nextLine().toLowerCase();

            if ("yes".equals(removeChoice)) {
                System.out.print("Enter the name of the item to remove: ");
                String itemToRemove = scanner.nextLine().toUpperCase();

                if (itemsInCart.containsKey(itemToRemove)) {
                    itemsInCart.remove(itemToRemove);
                    System.out.println(itemToRemove + " has been removed from the shopping cart.");
                } else {
                    System.out.println(itemToRemove + " is not in the shopping cart.");
                }
            }
        } else {
            System.out.println("No matching items found in district " + district + ".");
        }
    }

    private int calculateMinShopsToVisit(List<Map.Entry<String, Double>> sortedItems) {
        // Get addresses for all items
        List<String> addresses = sortedItems.stream()
                .map(entry -> getCheapestPriceInfo(entry.getKey()).getAddress())
                .collect(Collectors.toList());

        // Find the most frequent address
        String mostFrequentAddress = findMostFrequentAddress(addresses);

        // Filter recommended items based on the most frequent address
        List<Map.Entry<String, Double>> recommendedItems = sortedItems.stream()
                .filter(entry -> getCheapestPriceInfo(entry.getKey()).getAddress().equals(mostFrequentAddress))
                .collect(Collectors.toList());

        // Display recommendation and return the number of shops to visit
        if (recommendedItems.size() == sortedItems.size()) {
            displayRecommendation(mostFrequentAddress, recommendedItems);
            return 1;
        } else {
            // Filter remaining items and find their most frequent address
            List<Map.Entry<String, Double>> remainingItems = sortedItems.stream()
                    .filter(entry -> !recommendedItems.contains(entry))
                    .collect(Collectors.toList());

            String remainingItemsAddress = findMostFrequentAddress(remainingItems.stream()
                    .map(entry -> getCheapestPriceInfo(entry.getKey()).getAddress())
                    .collect(Collectors.toList()));

            // Filter remaining items based on the most frequent address
            List<Map.Entry<String, Double>> remainingItemsAndPrices = sortedItems.stream()
                    .filter(entry -> getCheapestPriceInfo(entry.getKey()).getAddress().equals(remainingItemsAddress))
                    .collect(Collectors.toList());

            // Display recommendation and return the number of shops to visit
            displayRecommendation(remainingItemsAddress, remainingItemsAndPrices);
            return 2;
        }
    }
    
    // Method to get the cheapest price information for a given item
    private CheapestPriceInfo getCheapestPriceInfo(String itemKey) {
        try {
            // Path to the CSV file
            String filePath = "C:\\Users\\User\\Dropbox\\My PC (LAPTOP-SPVCRBCA)\\Documents\\NetBeansProjects\\PriceTracker\\src\\main\\java\\merged_data.csv";
            String searchKeyword = itemKey.toUpperCase();

            // Initialize variables to store the cheapest price information
            double cheapestPrice = Double.MAX_VALUE;
            String cheapestAddress = null;

            // Read the CSV file and parse records
            try (Reader reader = new FileReader(filePath);
                 CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader)) {

                // Iterate through CSV records
                for (CSVRecord csvRecord : csvParser) {
                    String item = csvRecord.get(ITEM_HEADER);

                    // Check if the item contains the search keyword
                    if (item != null && item.toUpperCase().contains(searchKeyword)) {
                        double price = Double.parseDouble(csvRecord.get(PRICE_HEADER));
                        String address = csvRecord.get(ADDRESS_HEADER);

                        // Update the cheapest price information if a lower price is found
                        if (price < cheapestPrice) {
                            cheapestPrice = price;
                            cheapestAddress = address;
                        }
                    }
                }
            }

            // Return the cheapest price information
            if (cheapestAddress != null) {
                return new CheapestPriceInfo(cheapestPrice, cheapestAddress);
            } else {
                System.out.println("Item is not available.");
                return null;
            }

        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
            return null;
        } catch (NumberFormatException e) {
            System.err.println("Error parsing a number: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            return null;
        }
    }

    private String findMostFrequentAddress(List<String> addresses) {
        // Map to store the count of each address
        Map<String, Integer> addressCount = new HashMap<>();

        // Count the occurrences of each address
        for (String address : addresses) {
            addressCount.put(address, addressCount.getOrDefault(address, 0) + 1);
        }

        // Find the address with the maximum count
        return Collections.max(addressCount.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    private void displayRecommendation(String address, List<Map.Entry<String, Double>> itemsAndPrices) {
        System.out.println("Recommendation:");
        System.out.println("Visit the shop at address: " + address);
        System.out.println("Items and Prices:");
        itemsAndPrices.forEach(entry ->
                System.out.println(entry.getKey() + ": RM" + entry.getValue()));
    }

    private boolean isItemInDistrict(String item, String district) {
        try {
            String filePath = "C:\\Users\\User\\Dropbox\\My PC (LAPTOP-SPVCRBCA)\\Documents\\NetBeansProjects\\PriceTracker\\src\\main\\java\\merged_data.csv";

            try (Reader reader = new FileReader(filePath);
                 CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader)) {

                for (CSVRecord csvRecord : csvParser) {
                    String recordItem = csvRecord.get(ITEM_HEADER);
                    String recordDistrict = csvRecord.get(ADDRESS_HEADER);

                    // Check if the record matches the item and district
                    if (recordItem != null && recordItem.equalsIgnoreCase(item) && recordDistrict != null && recordDistrict.equalsIgnoreCase(district)) {
                        return true;
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }

        return false;
    }

    private double getCheapestPriceForItemInDistrict(String item, String district) {
        try {
            // File path to the CSV data
            String filePath = "C:\\Users\\User\\Dropbox\\My PC (LAPTOP-SPVCRBCA)\\Documents\\NetBeansProjects\\PriceTracker\\src\\main\\java\\merged_data.csv";

            double cheapestPrice = Double.MAX_VALUE;
            String cheapestAddress = null;

            try (Reader reader = new FileReader(filePath);
                 CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader)) {

                for (CSVRecord csvRecord : csvParser) {
                    String recordItem = csvRecord.get(ITEM_HEADER);
                    String recordDistrict = csvRecord.get(ADDRESS_HEADER);

                    // Check if the record matches the specified item and district
                    if (recordItem != null && recordItem.equalsIgnoreCase(item) &&
                            recordDistrict != null && recordDistrict.equalsIgnoreCase(district)) {
                        
                        // Parse the price from the record
                        double price = Double.parseDouble(csvRecord.get(PRICE_HEADER));

                        if (price < cheapestPrice) {
                            cheapestPrice = price;
                            cheapestAddress = csvRecord.get(ADDRESS_HEADER);
                        }
                    }
                }
            }

            // Display the result and return the cheapest price or -1 if not available
            if (cheapestAddress != null) {
                System.out.println("Cheapest Price for " + item + " in district " + district + ": RM" + cheapestPrice);
                System.out.println("Address: " + cheapestAddress);
                return cheapestPrice;
            } else {
                System.out.println("Item is not available in district " + district);
                return -1;
            }

        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
            return -1;
        } catch (NumberFormatException e) {
            System.err.println("Error parsing a number: " + e.getMessage());
            return -1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            return -1;
        }
    }

    // Represents information about the cheapest price for an item.
    static class CheapestPriceInfo {
        private final double price;
        private final String address;

        // Constructs a CheapestPriceInfo object with the given price and address.
        public CheapestPriceInfo(double price, String address) {
            this.price = price;
            this.address = address;
        }

        // Gets the price of the item.
        public double getPrice() {
            return price;
        }

        // Gets the address of the shop selling the item at the cheapest price.
        public String getAddress() {
            return address;
        }
    }
}
