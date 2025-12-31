package com.orderflow.benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * JMH Benchmarks specifically for allocation patterns.
 * 
 * ============================================================
 * Lab B: Allocation Patterns Comparison
 * ============================================================
 * 
 * Run with:
 *   java -jar benchmarks.jar AllocationBenchmark -prof gc -f 2
 *   
 * This benchmark demonstrates common allocation anti-patterns
 * and their better alternatives. Use to understand the cost.
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms1G", "-Xmx1G"})
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
public class AllocationBenchmark {
    
    // Pre-compiled pattern for comparison
    private static final Pattern PRE_COMPILED = Pattern.compile(",");
    
    private String csvLine;
    private List<Integer> numbers;
    
    @Setup
    public void setup() {
        csvLine = "ORD00001,AAPL,BUY,150.50,100";
        numbers = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            numbers.add(i);
        }
    }
    
    // ============================================================
    // Pattern Compilation: Inside method vs static
    // ============================================================
    
    /**
     * BAD: Compiles pattern on every call.
     */
    @Benchmark
    public String[] patternCompileInMethod() {
        Pattern pattern = Pattern.compile(",");  // Allocates Pattern!
        return pattern.split(csvLine);
    }
    
    /**
     * GOOD: Uses pre-compiled static pattern.
     */
    @Benchmark
    public String[] patternPreCompiled() {
        return PRE_COMPILED.split(csvLine);
    }
    
    // ============================================================
    // String concatenation: + operator vs StringBuilder
    // ============================================================
    
    /**
     * BAD: String concatenation in loop.
     */
    @Benchmark
    public String stringConcatInLoop() {
        String result = "";
        for (int i = 0; i < 100; i++) {
            result = result + "item" + i + ",";  // New String each iteration!
        }
        return result;
    }
    
    /**
     * GOOD: StringBuilder with initial capacity.
     */
    @Benchmark
    public String stringBuilderWithCapacity() {
        StringBuilder sb = new StringBuilder(800);  // Estimated capacity
        for (int i = 0; i < 100; i++) {
            sb.append("item").append(i).append(",");
        }
        return sb.toString();
    }
    
    // ============================================================
    // ArrayList: No capacity vs pre-sized
    // ============================================================
    
    /**
     * BAD: ArrayList without initial capacity - resizes many times.
     */
    @Benchmark
    public List<String> arrayListNoCapacity() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add("item" + i);
        }
        return list;
    }
    
    /**
     * GOOD: ArrayList with known capacity.
     */
    @Benchmark
    public List<String> arrayListWithCapacity() {
        List<String> list = new ArrayList<>(1000);
        for (int i = 0; i < 1000; i++) {
            list.add("item" + i);
        }
        return list;
    }
    
    // ============================================================
    // Stream: Boxed vs primitive
    // ============================================================
    
    /**
     * BAD: Stream with boxing.
     */
    @Benchmark
    public double streamBoxed() {
        return numbers.stream()
            .map(n -> n * 2.0)        // Returns Stream<Double>!
            .mapToDouble(d -> d)       // Unbox
            .average()
            .orElse(0);
    }
    
    /**
     * GOOD: Primitive stream.
     */
    @Benchmark
    public double streamPrimitive() {
        return numbers.stream()
            .mapToDouble(n -> n * 2.0)  // Direct to DoubleStream
            .average()
            .orElse(0);
    }
    
    // ============================================================
    // BigDecimal: new vs valueOf
    // ============================================================
    
    /**
     * Note: BigDecimal.valueOf() caches small values.
     * For large/unique values, allocation is similar.
     */
    @Benchmark
    public BigDecimal bigDecimalNew() {
        return new BigDecimal("150.50");
    }
    
    @Benchmark
    public BigDecimal bigDecimalValueOf() {
        return BigDecimal.valueOf(150.50);
    }
}
