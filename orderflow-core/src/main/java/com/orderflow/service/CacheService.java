package com.orderflow.service;

import com.orderflow.model.Order;
import java.util.HashMap;
import java.util.Map;

/**
 * In-memory cache for frequently accessed orders.
 * 
 * ============================================================
 * HOTSPOT #6: Naive Caching
 * ============================================================
 * 
 * PERF-HINT: This cache implementation has problems:
 *   1. No eviction policy (memory leak!)
 *   2. HashMap not thread-safe (race conditions)
 *   3. No size bounds
 *   4. Cache key creation allocations
 * 
 * PERF-LAB: Lab G - Caching Strategy Experiment
 * 
 * Observable symptoms:
 *   - Heap grows unbounded over time
 *   - ConcurrentModificationException under load
 *   - Eventually OutOfMemoryError
 * 
 * Questions to answer:
 *   - What's the memory footprint after 1M orders?
 *   - What happens with concurrent access?
 *   - What's the optimal cache size?
 */
public class CacheService {
    
    // PERF-HINT: HashMap is NOT thread-safe!
    // PERF-HINT: No maximum size - will grow forever!
    private final Map<String, Order> orderCache = new HashMap<>();
    private final Map<String, Object> genericCache = new HashMap<>();
    
    // Simple hit/miss counters
    private long hits = 0;
    private long misses = 0;
    
    /**
     * Cache an order by ID.
     * 
     * PERF-HINT: No eviction = memory leak!
     */
    public void cacheOrder(Order order) {
        // TODO: Implement
        //
        // INTENTIONAL INEFFICIENCY:
        // 1. Simply put into HashMap
        // 2. No size check
        // 3. No eviction
        // 4. Not thread-safe
        //
        // After profiling, consider:
        // - ConcurrentHashMap for thread safety
        // - LRU eviction policy
        // - Size bounds
        // - Caffeine cache library
        //
        throw new UnsupportedOperationException("Implement cacheOrder");
    }
    
    /**
     * Retrieve cached order.
     * 
     * PERF-HINT: Race condition - hits/misses update not atomic!
     */
    public Order getOrder(String orderId) {
        // TODO: Implement
        //
        // INTENTIONAL INEFFICIENCY:
        // Order order = orderCache.get(orderId);
        // if (order != null) {
        //     hits++;  // NOT thread-safe!
        // } else {
        //     misses++;  // NOT thread-safe!
        // }
        // return order;
        //
        throw new UnsupportedOperationException("Implement getOrder");
    }
    
    /**
     * Check if order is cached.
     */
    public boolean containsOrder(String orderId) {
        throw new UnsupportedOperationException("Implement containsOrder");
    }
    
    /**
     * Remove order from cache.
     */
    public void evictOrder(String orderId) {
        throw new UnsupportedOperationException("Implement evictOrder");
    }
    
    /**
     * Generic cache put.
     * 
     * PERF-HINT: String concatenation for compound keys!
     */
    public void put(String namespace, String key, Object value) {
        // TODO: Implement
        //
        // INTENTIONAL INEFFICIENCY:
        // String cacheKey = namespace + ":" + key;  // Allocates new String!
        // genericCache.put(cacheKey, value);
        //
        throw new UnsupportedOperationException("Implement put");
    }
    
    /**
     * Generic cache get.
     */
    public Object get(String namespace, String key) {
        // TODO: Implement with same key creation pattern
        throw new UnsupportedOperationException("Implement get");
    }
    
    /**
     * Get cache statistics.
     */
    public Map<String, Object> getStats() {
        // TODO: Return hits, misses, hit rate, size
        throw new UnsupportedOperationException("Implement getStats");
    }
    
    /**
     * Get current cache size.
     */
    public int getSize() {
        return orderCache.size() + genericCache.size();
    }
    
    /**
     * Clear entire cache.
     */
    public void clear() {
        orderCache.clear();
        genericCache.clear();
        hits = 0;
        misses = 0;
    }
}
