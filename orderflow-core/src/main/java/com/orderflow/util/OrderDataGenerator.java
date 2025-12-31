package com.orderflow.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Generates synthetic order data for performance testing.
 * 
 * Usage:
 *   java -cp orderflow-core.jar com.orderflow.util.OrderDataGenerator --count 100000 --output orders-100k.csv
 */
public class OrderDataGenerator {
    
    private static final String[] SYMBOLS = {
        "AAPL", "GOOG", "MSFT", "AMZN", "META", 
        "NVDA", "TSLA", "JPM", "V", "WMT"
    };
    
    private static final String[] SIDES = {"BUY", "SELL"};
    
    // Price ranges per symbol (for realistic data)
    private static final double[][] PRICE_RANGES = {
        {170, 200},  // AAPL
        {130, 160},  // GOOG
        {350, 420},  // MSFT
        {140, 180},  // AMZN
        {350, 450},  // META
        {450, 550},  // NVDA
        {180, 280},  // TSLA
        {150, 200},  // JPM
        {250, 300},  // V
        {150, 180}   // WMT
    };
    
    private final Random random;
    
    public OrderDataGenerator(long seed) {
        this.random = new Random(seed);
    }
    
    /**
     * Generate a single order CSV line.
     */
    public String generateOrder(long orderId) {
        int symbolIndex = random.nextInt(SYMBOLS.length);
        String symbol = SYMBOLS[symbolIndex];
        String side = SIDES[random.nextInt(2)];
        double[] priceRange = PRICE_RANGES[symbolIndex];
        double price = priceRange[0] + random.nextDouble() * (priceRange[1] - priceRange[0]);
        int quantity = 10 + random.nextInt(991); // 10 to 1000
        
        return String.format("ORD%08d,%s,%s,%.2f,%d", 
            orderId, symbol, side, price, quantity);
    }
    
    /**
     * Generate orders to a file.
     */
    public void generateToFile(String filename, int count) throws IOException {
        long startTime = System.currentTimeMillis();
        System.out.printf("Generating %,d orders...%n", count);
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename), 65536)) {
            // Write header
            writer.write("orderId,symbol,side,price,quantity");
            writer.newLine();
            
            for (int i = 1; i <= count; i++) {
                writer.write(generateOrder(i));
                writer.newLine();
                
                if (i % 100_000 == 0) {
                    System.out.printf("  Generated %,d orders...%n", i);
                }
            }
        }
        
        long elapsed = System.currentTimeMillis() - startTime;
        System.out.printf("Done! Generated %,d orders in %.2fs%n", count, elapsed / 1000.0);
        System.out.printf("Output: %s%n", filename);
    }
    
    public static void main(String[] args) {
        int count = 10_000;
        String output = "datasets/orders.csv";
        long seed = 42;
        
        // Parse arguments
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--count", "-c" -> count = Integer.parseInt(args[++i]);
                case "--output", "-o" -> output = args[++i];
                case "--seed", "-s" -> seed = Long.parseLong(args[++i]);
                case "--help", "-h" -> {
                    printUsage();
                    return;
                }
            }
        }
        
        try {
            OrderDataGenerator generator = new OrderDataGenerator(seed);
            generator.generateToFile(output, count);
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
            System.exit(1);
        }
    }
    
    private static void printUsage() {
        System.out.println("Usage: java -cp orderflow-core.jar com.orderflow.util.OrderDataGenerator [options]");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  --count, -c <n>     Number of orders to generate (default: 10000)");
        System.out.println("  --output, -o <file> Output file path (default: datasets/orders.csv)");
        System.out.println("  --seed, -s <n>      Random seed for reproducibility (default: 42)");
        System.out.println("  --help, -h          Show this help");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  java -cp orderflow-core.jar com.orderflow.util.OrderDataGenerator -c 100000 -o orders-100k.csv");
    }
}
