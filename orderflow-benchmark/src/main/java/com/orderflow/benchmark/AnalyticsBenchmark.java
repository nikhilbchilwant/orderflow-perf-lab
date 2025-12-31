package com.orderflow.benchmark;

import com.orderflow.model.Order;
import com.orderflow.model.TradeResult;
import com.orderflow.service.AnalyticsService;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * JMH Benchmarks for analytics operations.
 * 
 * ============================================================
 * Lab B: Allocation Measurement (Stream Boxing)
 * ============================================================
 * 
 * Run with:
 *   java -jar benchmarks.jar AnalyticsBenchmark -prof gc -f 2
 *   
 * -prof gc shows GC allocation metrics!
 * Look at gc.alloc.rate.norm - this is bytes allocated per operation.
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms1G", "-Xmx1G"})
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
public class AnalyticsBenchmark {
    
    @Param({"100", "1000", "10000"})
    private int orderCount;
    
    private AnalyticsService analyticsService;
    private List<Order> orders;
    private List<TradeResult> trades;
    
    @Setup
    public void setup() {
        analyticsService = new AnalyticsService();
        
        // Generate test orders
        orders = new ArrayList<>(orderCount);
        trades = new ArrayList<>(orderCount);
        
        // TODO: Generate test data when Order/TradeResult constructors are ready
        // for (int i = 0; i < orderCount; i++) {
        //     orders.add(new Order(
        //         "ORD" + i,
        //         symbols[i % symbols.length],
        //         i % 2 == 0 ? Order.Side.BUY : Order.Side.SELL,
        //         new BigDecimal(100 + (i % 100)),
        //         100 + (i % 500)
        //     ));
        // }
    }
    
    /**
     * Benchmark average price calculation.
     * 
     * Look at gc.alloc.rate.norm - high values indicate boxing overhead.
     */
    @Benchmark
    public Double calculateAveragePrice(Blackhole bh) {
        // TODO: Implement when AnalyticsService is ready
        // return analyticsService.calculateAveragePrice(orders);
        throw new UnsupportedOperationException(
            "Implement AnalyticsService.calculateAveragePrice() first");
    }
    
    /**
     * Benchmark total value calculation.
     */
    @Benchmark
    public Double calculateTotalValue(Blackhole bh) {
        // TODO: Implement when AnalyticsService is ready
        // return analyticsService.calculateTotalValue(orders);
        throw new UnsupportedOperationException(
            "Implement AnalyticsService.calculateTotalValue() first");
    }
    
    /**
     * Benchmark price statistics.
     * 
     * This should show very high allocation due to multiple passes.
     */
    @Benchmark
    public Object calculatePriceStatistics(Blackhole bh) {
        // TODO: Implement
        // return analyticsService.calculatePriceStatistics(orders);
        throw new UnsupportedOperationException(
            "Implement AnalyticsService.calculatePriceStatistics() first");
    }
}
