package com.orderflow.benchmark;

import com.orderflow.model.Order;
import com.orderflow.util.OrderParser;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import java.util.concurrent.TimeUnit;

/**
 * JMH Benchmarks for order parsing operations.
 * 
 * ============================================================
 * Lab H: Microbenchmark vs Real-World Comparison
 * ============================================================
 * 
 * Run with:
 *   java -jar benchmarks.jar OrderParsingBenchmark -f 2 -wi 3 -i 5
 * 
 * Key metrics to observe:
 *   - ops/ms - throughput
 *   - gc.alloc.rate.norm - bytes allocated per operation
 *   
 * Compare:
 *   1. Run benchmark, note ops/ms
 *   2. Optimize based on results
 *   3. Run benchmark again
 *   4. Run full application - does improvement match?
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms1G", "-Xmx1G"})
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
public class OrderParsingBenchmark {
    
    // Test data
    private String singleLine;
    private java.util.List<String> batchLines;
    private OrderParser parser;
    
    @Setup
    public void setup() {
        parser = new OrderParser();
        singleLine = "ORD00001,AAPL,BUY,150.50,100";
        
        // Generate batch of lines
        batchLines = new java.util.ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            batchLines.add(String.format("ORD%05d,AAPL,BUY,%.2f,%d", 
                i, 100.0 + (i % 100), 100 + (i % 50)));
        }
    }
    
    /**
     * Benchmark single line parsing.
     * 
     * This measures the baseline performance of parsing one order.
     * Look at gc.alloc.rate.norm to see allocation per call.
     */
    @Benchmark
    public Order parseSingleLine(Blackhole bh) {
        // TODO: Implement when OrderParser.parse() is implemented
        // return parser.parse(singleLine);
        throw new UnsupportedOperationException(
            "Implement OrderParser.parse() first, then uncomment benchmark");
    }
    
    /**
     * Benchmark batch parsing.
     * 
     * Measures throughput of parsing many lines.
     */
    @Benchmark
    public java.util.List<Order> parseBatch(Blackhole bh) {
        // TODO: Implement when OrderParser.parseAll() is implemented
        // return parser.parseAll(batchLines);
        throw new UnsupportedOperationException(
            "Implement OrderParser.parseAll() first, then uncomment benchmark");
    }
    
    /**
     * Benchmark order ID validation.
     * 
     * Tests regex performance separately.
     */
    @Benchmark
    public boolean validateOrderId() {
        // TODO: Implement when OrderParser.isValidOrderId() is implemented
        // return parser.isValidOrderId("ORD00001");
        throw new UnsupportedOperationException(
            "Implement OrderParser.isValidOrderId() first, then uncomment benchmark");
    }
}
