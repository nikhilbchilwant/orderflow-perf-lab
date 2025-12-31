package com.orderflow.util;

import com.orderflow.model.Order;
import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * Parses order data from CSV/string format.
 * 
 * ============================================================
 * HOTSPOT #1: String Parsing & Regex
 * ============================================================
 * 
 * PERF-HINT: This class has multiple allocation problems:
 *   1. Regex Pattern is compiled on EVERY call
 *   2. String.split() creates intermediate arrays
 *   3. String concatenation in validation messages
 *   4. Substring operations create new String objects (pre-Java 7u6 concern, 
 *      but string operations still allocate)
 * 
 * PERF-LAB: Lab B - Profile with async-profiler -e alloc
 * 
 * Expected symptoms:
 *   - High char[] allocation in flame graph
 *   - Pattern compilation appearing in CPU profile
 *   - String[] from split() visible in allocation profile
 * 
 * Questions to answer:
 *   - What's the allocation rate per order parsed?
 *   - What percentage of CPU is spent in regex?
 */
public class OrderParser {
    
    // PERF-HINT: Is this the right place for the pattern?
    // What happens when you compile a Pattern inside a method called 1M times?
    
    /**
     * Parse a single CSV line into an Order.
     * Format: orderId,symbol,side,price,quantity
     * Example: "ORD001,AAPL,BUY,150.50,100"
     * 
     * PERF-HINT: Count the allocations in this method!
     */
    public Order parse(String line) {
        // TODO: Implement parsing
        // 
        // INTENTIONAL INEFFICIENCY (implement this way first, then optimize):
        // 1. Compile Pattern.compile(",") inside this method
        // 2. Use pattern.split(line) 
        // 3. Validate each field with string concatenation for error messages
        // 4. Parse price with new BigDecimal(parts[3])
        // 5. Create and return new Order
        //
        // Example inefficient validation:
        //   if (parts.length != 5) {
        //       throw new IllegalArgumentException("Invalid line: " + line + 
        //           " expected 5 fields but got " + parts.length);
        //   }
        //
        // After profiling, consider:
        // - Pre-compiled static Pattern
        // - indexOf/substring instead of split
        // - Lazy error message creation
        
        throw new UnsupportedOperationException("Implement parse method");
    }
    
    /**
     * Parse multiple lines.
     * 
     * PERF-HINT: What's the cost of creating a new ArrayList for return value?
     */
    public java.util.List<Order> parseAll(java.util.List<String> lines) {
        // TODO: Implement batch parsing
        // PERF-HINT: Is there a way to avoid ArrayList resize operations?
        throw new UnsupportedOperationException("Implement parseAll method");
    }
    
    /**
     * Validate order ID format.
     * 
     * PERF-HINT: Another regex! Is it compiled once?
     */
    public boolean isValidOrderId(String orderId) {
        // TODO: Validate format like "ORD" followed by digits
        // INTENTIONAL: Use Pattern.matches() which compiles pattern each time
        throw new UnsupportedOperationException("Implement validation");
    }
}
