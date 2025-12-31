package com.orderflow.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * In-memory order book for a single symbol.
 * Maintains buy and sell orders sorted by price-time priority.
 * 
 * PERF-HINT: The data structure choice here significantly impacts performance.
 * PERF-LAB: Lab E targets the synchronization patterns here.
 * 
 * Consider:
 * - What data structure gives O(1) best bid/ask lookup?
 * - How should concurrent access be handled?
 * - Is coarse-grained locking necessary for all operations?
 */
public class OrderBook {
    
    private final String symbol;
    
    // TODO: Choose appropriate data structures for buy and sell orders
    // PERF-HINT: Consider TreeMap, ConcurrentSkipListMap, or custom structures
    // PERF-HINT: Buy orders sorted descending by price, sell orders ascending
    
    public OrderBook(String symbol) {
        this.symbol = symbol;
        // TODO: Initialize order storage
        throw new UnsupportedOperationException("Implement constructor");
    }
    
    /**
     * Add an order to the book.
     * PERF-HINT: Is synchronized on the entire method necessary?
     * PERF-LAB: Lab E - measure lock contention here
     */
    public synchronized void addOrder(Order order) {
        // TODO: Add order to appropriate side
        // PERF-HINT: What happens under high concurrent load?
        throw new UnsupportedOperationException("Implement addOrder");
    }
    
    /**
     * Remove an order from the book.
     */
    public synchronized void removeOrder(String orderId) {
        // TODO: Find and remove order
        throw new UnsupportedOperationException("Implement removeOrder");
    }
    
    /**
     * Get the best (highest) buy price.
     * PERF-HINT: Should this lock the entire book?
     */
    public synchronized BigDecimal getBestBid() {
        // TODO: Return highest buy price or null
        throw new UnsupportedOperationException("Implement getBestBid");
    }
    
    /**
     * Get the best (lowest) sell price.
     */
    public synchronized BigDecimal getBestAsk() {
        // TODO: Return lowest sell price or null
        throw new UnsupportedOperationException("Implement getBestAsk");
    }
    
    /**
     * Get all orders at a specific price level.
     * PERF-HINT: Returning a copy vs view - what's the trade-off?
     */
    public synchronized List<Order> getOrdersAtPrice(Order.Side side, BigDecimal price) {
        // TODO: Return orders at price level
        // PERF-HINT: Are you creating unnecessary copies here?
        throw new UnsupportedOperationException("Implement getOrdersAtPrice");
    }
    
    /**
     * Get orders available for matching against an incoming order.
     * 
     * @param incomingOrder the order to match against
     * @return list of orders that could be matched
     */
    public synchronized List<Order> getMatchableOrders(Order incomingOrder) {
        // TODO: Return orders on opposite side with compatible price
        // For BUY order: return SELL orders with price <= order price
        // For SELL order: return BUY orders with price >= order price
        throw new UnsupportedOperationException("Implement getMatchableOrders");
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    /**
     * Get order count for monitoring.
     */
    public synchronized int getOrderCount() {
        throw new UnsupportedOperationException("Implement getOrderCount");
    }
}
