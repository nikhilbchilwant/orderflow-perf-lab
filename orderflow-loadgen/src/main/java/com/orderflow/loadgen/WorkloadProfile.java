package com.orderflow.loadgen;

/**
 * Defines workload profiles for load testing.
 * 
 * Different workload patterns stress different parts of the system.
 */
public class WorkloadProfile {
    
    public enum Pattern {
        CONSTANT,      // Steady rate
        RAMP_UP,       // Gradually increasing
        BURST,         // Periodic bursts
        RANDOM         // Random intervals
    }
    
    private final String name;
    private final int orderCount;
    private final int targetOpsPerSecond;
    private final Pattern pattern;
    private final int threadCount;
    private final String[] symbols;
    private final double buyRatio;  // Percentage of buy orders (0.0 - 1.0)
    
    private WorkloadProfile(Builder builder) {
        this.name = builder.name;
        this.orderCount = builder.orderCount;
        this.targetOpsPerSecond = builder.targetOpsPerSecond;
        this.pattern = builder.pattern;
        this.threadCount = builder.threadCount;
        this.symbols = builder.symbols;
        this.buyRatio = builder.buyRatio;
    }
    
    // Getters
    public String getName() { return name; }
    public int getOrderCount() { return orderCount; }
    public int getTargetOpsPerSecond() { return targetOpsPerSecond; }
    public Pattern getPattern() { return pattern; }
    public int getThreadCount() { return threadCount; }
    public String[] getSymbols() { return symbols; }
    public double getBuyRatio() { return buyRatio; }
    
    // Pre-defined profiles
    
    /**
     * Light load for debugging.
     */
    public static WorkloadProfile light() {
        return new Builder("light")
            .orderCount(1000)
            .targetOpsPerSecond(100)
            .pattern(Pattern.CONSTANT)
            .threadCount(1)
            .build();
    }
    
    /**
     * Medium load for baseline measurement.
     */
    public static WorkloadProfile medium() {
        return new Builder("medium")
            .orderCount(10_000)
            .targetOpsPerSecond(1000)
            .pattern(Pattern.CONSTANT)
            .threadCount(4)
            .build();
    }
    
    /**
     * Heavy load for stress testing.
     */
    public static WorkloadProfile heavy() {
        return new Builder("heavy")
            .orderCount(100_000)
            .targetOpsPerSecond(10_000)
            .pattern(Pattern.CONSTANT)
            .threadCount(8)
            .build();
    }
    
    /**
     * Burst pattern to stress buffering.
     */
    public static WorkloadProfile burst() {
        return new Builder("burst")
            .orderCount(50_000)
            .targetOpsPerSecond(5000)
            .pattern(Pattern.BURST)
            .threadCount(4)
            .build();
    }
    
    /**
     * Ramp-up to find breaking point.
     */
    public static WorkloadProfile rampUp() {
        return new Builder("ramp-up")
            .orderCount(100_000)
            .targetOpsPerSecond(20_000)  // Target at end of ramp
            .pattern(Pattern.RAMP_UP)
            .threadCount(8)
            .build();
    }
    
    public static class Builder {
        private String name;
        private int orderCount = 10_000;
        private int targetOpsPerSecond = 1000;
        private Pattern pattern = Pattern.CONSTANT;
        private int threadCount = 4;
        private String[] symbols = {"AAPL", "GOOG", "MSFT", "AMZN", "META"};
        private double buyRatio = 0.5;
        
        public Builder(String name) {
            this.name = name;
        }
        
        public Builder orderCount(int count) {
            this.orderCount = count;
            return this;
        }
        
        public Builder targetOpsPerSecond(int ops) {
            this.targetOpsPerSecond = ops;
            return this;
        }
        
        public Builder pattern(Pattern pattern) {
            this.pattern = pattern;
            return this;
        }
        
        public Builder threadCount(int threads) {
            this.threadCount = threads;
            return this;
        }
        
        public Builder symbols(String... symbols) {
            this.symbols = symbols;
            return this;
        }
        
        public Builder buyRatio(double ratio) {
            this.buyRatio = ratio;
            return this;
        }
        
        public WorkloadProfile build() {
            return new WorkloadProfile(this);
        }
    }
}
