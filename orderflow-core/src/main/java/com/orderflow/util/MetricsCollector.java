package com.orderflow.util;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Collects and reports performance metrics.
 * 
 * Use this to instrument your code and measure improvements.
 */
public class MetricsCollector {
    
    private static final MetricsCollector INSTANCE = new MetricsCollector();
    
    private final Map<String, AtomicLong> counters = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> timers = new ConcurrentHashMap<>();
    private volatile long startTime;
    
    private MetricsCollector() {
        this.startTime = System.nanoTime();
    }
    
    public static MetricsCollector getInstance() {
        return INSTANCE;
    }
    
    /**
     * Increment a counter.
     */
    public void increment(String name) {
        counters.computeIfAbsent(name, k -> new AtomicLong()).incrementAndGet();
    }
    
    /**
     * Increment a counter by delta.
     */
    public void incrementBy(String name, long delta) {
        counters.computeIfAbsent(name, k -> new AtomicLong()).addAndGet(delta);
    }
    
    /**
     * Record elapsed time for an operation.
     */
    public void recordTime(String name, long nanos) {
        timers.computeIfAbsent(name, k -> new AtomicLong()).addAndGet(nanos);
    }
    
    /**
     * Get counter value.
     */
    public long getCount(String name) {
        AtomicLong counter = counters.get(name);
        return counter != null ? counter.get() : 0;
    }
    
    /**
     * Get total time in milliseconds.
     */
    public long getTimeMs(String name) {
        AtomicLong timer = timers.get(name);
        return timer != null ? timer.get() / 1_000_000 : 0;
    }
    
    /**
     * Get elapsed time since start in milliseconds.
     */
    public long getElapsedMs() {
        return (System.nanoTime() - startTime) / 1_000_000;
    }
    
    /**
     * Calculate throughput (operations per second).
     */
    public double getThroughput(String counterName) {
        long count = getCount(counterName);
        long elapsedMs = getElapsedMs();
        if (elapsedMs == 0) return 0;
        return (count * 1000.0) / elapsedMs;
    }
    
    /**
     * Reset all metrics.
     */
    public void reset() {
        counters.clear();
        timers.clear();
        startTime = System.nanoTime();
    }
    
    /**
     * Print summary to console.
     */
    public void printSummary() {
        System.out.println("\n=== Performance Metrics ===");
        System.out.printf("Elapsed time: %d ms%n", getElapsedMs());
        System.out.println("\nCounters:");
        counters.forEach((name, value) -> 
            System.out.printf("  %s: %d (%.2f/sec)%n", 
                name, value.get(), getThroughput(name)));
        System.out.println("\nTimers (ms):");
        timers.forEach((name, value) -> 
            System.out.printf("  %s: %d ms%n", name, value.get() / 1_000_000));
    }
    
    /**
     * Helper for timing a block of code.
     */
    public static class Timer implements AutoCloseable {
        private final String name;
        private final long start;
        
        public Timer(String name) {
            this.name = name;
            this.start = System.nanoTime();
        }
        
        @Override
        public void close() {
            long elapsed = System.nanoTime() - start;
            MetricsCollector.getInstance().recordTime(name, elapsed);
        }
    }
    
    /**
     * Start a timer - use with try-with-resources.
     * 
     * Example:
     *   try (var timer = MetricsCollector.getInstance().time("parsing")) {
     *       // code to measure
     *   }
     */
    public Timer time(String name) {
        return new Timer(name);
    }
}
