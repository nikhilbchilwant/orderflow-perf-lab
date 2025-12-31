package com.orderflow.io;

import com.orderflow.model.Order;
import com.orderflow.model.TradeResult;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Writes orders and trades to files for audit purposes.
 * 
 * ============================================================
 * HOTSPOT #8: Unbuffered I/O
 * ============================================================
 * 
 * PERF-HINT: This class has severe I/O inefficiencies:
 *   1. New FileWriter per write (file open/close overhead)
 *   2. No buffering
 *   3. Individual writes per record
 *   4. Sync writes (no async I/O)
 * 
 * PERF-LAB: Lab F - I/O & Batching Optimization
 * 
 * Observable symptoms:
 *   - High I/O wait time
 *   - Low throughput for persistence
 *   - Disk I/O spikes
 * 
 * Questions to answer:
 *   - What's the overhead of file open/close?
 *   - How does buffer size affect throughput?
 *   - When should you use async I/O?
 */
public class OrderFileWriter {
    
    private final String baseDirectory;
    
    public OrderFileWriter(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }
    
    /**
     * Write a single order to file.
     * 
     * PERF-HINT: Opens, writes, closes file for EACH order!
     */
    public void writeOrder(Order order) {
        // TODO: Implement
        //
        // INTENTIONAL INEFFICIENCY:
        // String filename = baseDirectory + "/orders.csv";
        // try (FileWriter writer = new FileWriter(filename, true)) {  // Append mode
        //     writer.write(orderToCsv(order));
        //     writer.write("\n");
        // }  // Unbuffered, syncs to disk on close
        //
        // Problems:
        // - File open/close per order
        // - No buffering
        // - No batching
        //
        // After measuring, consider:
        // - BufferedWriter
        // - Batch writes
        // - Keep file handle open
        // - FileChannel for better control
        //
        throw new UnsupportedOperationException("Implement writeOrder");
    }
    
    /**
     * Write multiple orders.
     * 
     * PERF-HINT: Still opens/closes per order!
     */
    public void writeOrders(List<Order> orders) {
        // TODO: Implement
        //
        // INTENTIONAL INEFFICIENCY:
        // for (Order order : orders) {
        //     writeOrder(order);  // Each call opens/closes file!
        // }
        //
        throw new UnsupportedOperationException("Implement writeOrders");
    }
    
    /**
     * Write a trade to file.
     */
    public void writeTrade(TradeResult trade) {
        // TODO: Implement with same pattern
        throw new UnsupportedOperationException("Implement writeTrade");
    }
    
    /**
     * Write multiple trades.
     */
    public void writeTrades(List<TradeResult> trades) {
        // TODO: Implement with same inefficiency
        throw new UnsupportedOperationException("Implement writeTrades");
    }
    
    /**
     * Convert order to CSV format.
     * 
     * PERF-HINT: String concatenation allocations!
     */
    private String orderToCsv(Order order) {
        // TODO: Implement
        //
        // INTENTIONAL INEFFICIENCY:
        // return order.getOrderId() + "," + 
        //        order.getSymbol() + "," +
        //        order.getSide() + "," +
        //        order.getPrice() + "," +
        //        order.getQuantity() + "," +
        //        order.getStatus() + "," +
        //        order.getCreatedAt();
        //
        // Better: StringBuilder with initial capacity
        //
        throw new UnsupportedOperationException("Implement orderToCsv");
    }
    
    /**
     * Convert trade to CSV format.
     */
    private String tradeToCsv(TradeResult trade) {
        // TODO: Implement with same pattern
        throw new UnsupportedOperationException("Implement tradeToCsv");
    }
}
