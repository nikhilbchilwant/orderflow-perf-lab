package com.orderflow.loadgen;

import com.orderflow.OrderFlowApplication;
import com.orderflow.model.Order;
import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Load generator for driving the OrderFlow application.
 * 
 * ============================================================
 * Usage Examples
 * ============================================================
 * 
 * # Light load (for debugging)
 * java -jar loadgen.jar --profile light
 * 
 * # Medium load (for baseline)
 * java -jar loadgen.jar --profile medium
 * 
 * # Heavy load (for stress testing)
 * java -jar loadgen.jar --profile heavy
 * 
 * # Custom load
 * java -jar loadgen.jar --orders 50000 --rate 5000 --threads 4
 * 
 * # With output file for results
 * java -jar loadgen.jar --profile medium --output results.txt
 */
public class LoadGenerator {
    
    private final OrderFlowApplication app;
    private final WorkloadProfile profile;
    private final LatencyRecorder latencyRecorder;
    private final AtomicLong ordersProcessed;
    private final AtomicLong ordersFailed;
    private final Random random;
    
    public LoadGenerator(OrderFlowApplication app, WorkloadProfile profile) {
        this.app = app;
        this.profile = profile;
        this.latencyRecorder = new LatencyRecorder();
        this.ordersProcessed = new AtomicLong();
        this.ordersFailed = new AtomicLong();
        this.random = new Random(42); // Fixed seed for reproducibility
    }
    
    /**
     * Run the load test.
     */
    public void run() {
        System.out.println("=== Load Generator ===");
        System.out.println("Profile: " + profile.getName());
        System.out.println("Orders: " + profile.getOrderCount());
        System.out.println("Target rate: " + profile.getTargetOpsPerSecond() + " ops/sec");
        System.out.println("Threads: " + profile.getThreadCount());
        System.out.println();
        
        long startTime = System.nanoTime();
        
        // TODO: Implement load generation
        //
        // Steps:
        // 1. Create thread pool with profile.getThreadCount() threads
        // 2. Submit profile.getOrderCount() order tasks
        // 3. Rate limit to target ops/sec if needed
        // 4. For each order:
        //    a. Record start time
        //    b. Generate random order string
        //    c. Submit to app.getIngestionService().processOrder()
        //    d. Record latency
        //    e. Increment success/failure counters
        // 5. Wait for completion
        // 6. Print results
        //
        throw new UnsupportedOperationException("Implement run method");
    }
    
    /**
     * Generate a random order string.
     */
    private String generateRandomOrder(long orderId) {
        // TODO: Implement
        //
        // Format: orderId,symbol,side,price,quantity
        // Example: "ORD00001,AAPL,BUY,150.50,100"
        //
        // Use profile.getSymbols() for symbol selection
        // Use profile.getBuyRatio() for buy/sell distribution
        //
        throw new UnsupportedOperationException("Implement generateRandomOrder");
    }
    
    /**
     * Print final report.
     */
    private void printReport(long elapsedNanos) {
        double elapsedSeconds = elapsedNanos / 1_000_000_000.0;
        double throughput = ordersProcessed.get() / elapsedSeconds;
        
        System.out.println("\n=== Load Test Results ===");
        System.out.printf("Total orders:    %,d%n", ordersProcessed.get());
        System.out.printf("Failed orders:   %,d%n", ordersFailed.get());
        System.out.printf("Elapsed time:    %.2f seconds%n", elapsedSeconds);
        System.out.printf("Throughput:      %.2f orders/sec%n", throughput);
        
        latencyRecorder.printReport();
    }
    
    /**
     * Main entry point.
     */
    public static void main(String[] args) {
        // TODO: Parse command line arguments
        // TODO: Select or build workload profile
        // TODO: Create application and load generator
        // TODO: Run load test
        
        // Default to medium profile
        WorkloadProfile profile = WorkloadProfile.medium();
        
        // Parse args
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--profile":
                    String profileName = args[++i];
                    profile = switch (profileName) {
                        case "light" -> WorkloadProfile.light();
                        case "medium" -> WorkloadProfile.medium();
                        case "heavy" -> WorkloadProfile.heavy();
                        case "burst" -> WorkloadProfile.burst();
                        case "ramp" -> WorkloadProfile.rampUp();
                        default -> {
                            System.err.println("Unknown profile: " + profileName);
                            yield WorkloadProfile.medium();
                        }
                    };
                    break;
                case "--orders":
                    // TODO: Custom order count
                    break;
                case "--rate":
                    // TODO: Custom ops/sec
                    break;
                case "--threads":
                    // TODO: Custom thread count
                    break;
                case "--help":
                    printUsage();
                    return;
            }
        }
        
        OrderFlowApplication app = new OrderFlowApplication();
        LoadGenerator generator = new LoadGenerator(app, profile);
        
        try {
            generator.run();
        } finally {
            app.shutdown();
        }
    }
    
    private static void printUsage() {
        System.out.println("Usage: java -jar loadgen.jar [options]");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  --profile <name>    Use predefined profile (light, medium, heavy, burst, ramp)");
        System.out.println("  --orders <n>        Number of orders to process");
        System.out.println("  --rate <n>          Target operations per second");
        System.out.println("  --threads <n>       Number of worker threads");
        System.out.println("  --output <file>     Save results to file");
        System.out.println("  --help              Show this help");
    }
}
