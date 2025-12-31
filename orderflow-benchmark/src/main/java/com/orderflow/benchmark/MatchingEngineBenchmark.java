package com.orderflow.benchmark;

import com.orderflow.model.Order;
import com.orderflow.model.OrderBook;
import com.orderflow.service.MatchingEngine;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

/**
 * JMH Benchmarks for matching engine operations.
 * 
 * ============================================================
 * Lab E: Lock Contention Measurement
 * ============================================================
 * 
 * Run with:
 *   java -jar benchmarks.jar MatchingEngineBenchmark -t 4 -f 2
 *   
 * -t 4 runs with 4 threads to expose contention!
 * 
 * Compare single-threaded vs multi-threaded throughput.
 * If multi-threaded is barely faster, you have contention.
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms1G", "-Xmx1G"})
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
public class MatchingEngineBenchmark {
    
    private MatchingEngine engine;
    private Order buyOrder;
    private Order sellOrder;
    
    // Counter for unique order IDs
    private long orderIdCounter = 0;
    
    @Setup(Level.Trial)
    public void setupTrial() {
        engine = new MatchingEngine();
    }
    
    @Setup(Level.Invocation)
    public void setupInvocation() {
        // Create fresh orders for each invocation
        // TODO: Implement when Order constructor is ready
        // buyOrder = new Order("ORD" + (orderIdCounter++), "AAPL", 
        //     Order.Side.BUY, new BigDecimal("150.00"), 100);
        // sellOrder = new Order("ORD" + (orderIdCounter++), "AAPL",
        //     Order.Side.SELL, new BigDecimal("150.00"), 100);
    }
    
    /**
     * Single-threaded order submission.
     * Run with -t 1 to establish baseline.
     */
    @Benchmark
    @Threads(1)
    public void submitOrderSingleThread(Blackhole bh) {
        // TODO: Implement when MatchingEngine.submitOrder() is ready
        // bh.consume(engine.submitOrder(buyOrder));
        throw new UnsupportedOperationException(
            "Implement MatchingEngine.submitOrder() first");
    }
    
    /**
     * Multi-threaded order submission.
     * Run with -t 4 to see contention effects.
     * 
     * If this is NOT 4x faster than single-threaded,
     * you have lock contention!
     */
    @Benchmark
    @Threads(4)
    public void submitOrderMultiThread(Blackhole bh) {
        // TODO: Same implementation as single-threaded
        // bh.consume(engine.submitOrder(buyOrder));
        throw new UnsupportedOperationException(
            "Implement MatchingEngine.submitOrder() first");
    }
    
    /**
     * Multi-threaded with different symbols.
     * 
     * If single-lock design, this should be similar to same-symbol.
     * With fine-grained locking, this should scale better!
     */
    @Benchmark
    @Threads(4)
    @Group("multiSymbol")
    @GroupThreads(1)
    public void submitOrderAAPL(Blackhole bh) {
        // TODO: Submit AAPL orders
        throw new UnsupportedOperationException("Implement");
    }
    
    @Benchmark
    @Threads(4)
    @Group("multiSymbol")
    @GroupThreads(1)
    public void submitOrderGOOG(Blackhole bh) {
        // TODO: Submit GOOG orders
        throw new UnsupportedOperationException("Implement");
    }
    
    @Benchmark
    @Threads(4)
    @Group("multiSymbol")
    @GroupThreads(1)
    public void submitOrderMSFT(Blackhole bh) {
        // TODO: Submit MSFT orders
        throw new UnsupportedOperationException("Implement");
    }
    
    @Benchmark
    @Threads(4)
    @Group("multiSymbol")
    @GroupThreads(1)
    public void submitOrderAMZN(Blackhole bh) {
        // TODO: Submit AMZN orders
        throw new UnsupportedOperationException("Implement");
    }
}
