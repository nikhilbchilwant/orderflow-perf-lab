package com.orderflow.model;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Order entity representing a trading order.
 * 
 * PERF-HINT: This class is intentionally mutable with defensive copies.
 * PERF-LAB: Lab B targets the allocation patterns here.
 * 
 * Consider:
 * - Are defensive copies always necessary?
 * - Could this be an immutable record?
 * - What's the cost of creating new Instant/BigDecimal objects?
 */
public class Order {
    
    public enum Side { BUY, SELL }
    public enum Status { NEW, FILLED, PARTIALLY_FILLED, CANCELLED }
    
    private String orderId;
    private String symbol;
    private Side side;
    private BigDecimal price;
    private int quantity;
    private int filledQuantity;
    private Status status;
    private Instant createdAt;
    private Instant updatedAt;
    
    // TODO: Implement constructor
    // PERF-HINT: Creating new Instant.now() for every order is expensive
    public Order(String orderId, String symbol, Side side, BigDecimal price, int quantity) {
        // YOUR IMPLEMENTATION HERE
        throw new UnsupportedOperationException("Implement this constructor");
    }
    
    // TODO: Implement copy constructor (used for "safe" returns)
    // PERF-HINT: Is this copy really necessary? Profile to find out.
    public Order(Order other) {
        // YOUR IMPLEMENTATION HERE - creates a defensive copy
        throw new UnsupportedOperationException("Implement copy constructor");
    }
    
    // TODO: Implement getters and setters
    // PERF-HINT: Consider if setters should create new Instant for updatedAt
    
    public String getOrderId() {
        throw new UnsupportedOperationException("Implement getter");
    }
    
    public String getSymbol() {
        throw new UnsupportedOperationException("Implement getter");
    }
    
    public Side getSide() {
        throw new UnsupportedOperationException("Implement getter");
    }
    
    public BigDecimal getPrice() {
        // PERF-HINT: Should this return a copy of BigDecimal?
        throw new UnsupportedOperationException("Implement getter");
    }
    
    public int getQuantity() {
        throw new UnsupportedOperationException("Implement getter");
    }
    
    public int getFilledQuantity() {
        throw new UnsupportedOperationException("Implement getter");
    }
    
    public Status getStatus() {
        throw new UnsupportedOperationException("Implement getter");
    }
    
    public Instant getCreatedAt() {
        throw new UnsupportedOperationException("Implement getter");
    }
    
    public Instant getUpdatedAt() {
        throw new UnsupportedOperationException("Implement getter");
    }
    
    public void fill(int quantity) {
        // TODO: Update filledQuantity, status, and updatedAt
        throw new UnsupportedOperationException("Implement fill logic");
    }
    
    public void cancel() {
        // TODO: Update status and updatedAt
        throw new UnsupportedOperationException("Implement cancel logic");
    }
    
    @Override
    public String toString() {
        // PERF-HINT: String concatenation in toString can be expensive if called frequently
        // Consider: Is this method called in hot paths?
        throw new UnsupportedOperationException("Implement toString");
    }
}
