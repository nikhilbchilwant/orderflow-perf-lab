package com.orderflow.loadgen;

import org.hdrhistogram.Histogram;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Records and reports latency using HDR Histogram.
 * 
 * HDR Histogram provides accurate latency percentiles
 * without significant memory overhead.
 * 
 * Usage:
 *   LatencyRecorder recorder = new LatencyRecorder();
 *   
 *   // In hot path
 *   long start = System.nanoTime();
 *   // ... operation ...
 *   recorder.recordLatency(System.nanoTime() - start);
 *   
 *   // After test
 *   recorder.printReport();
 */
public class LatencyRecorder {
    
    // Record latencies from 1 microsecond to 1 hour with 3 significant digits
    private final Histogram histogram;
    
    public LatencyRecorder() {
        this.histogram = new Histogram(
            1,                      // Lowest trackable (1 ns)
            3_600_000_000_000L,     // Highest trackable (1 hour in ns)
            3                       // Significant digits
        );
    }
    
    /**
     * Record a latency value in nanoseconds.
     */
    public void recordLatency(long nanos) {
        histogram.recordValue(nanos);
    }
    
    /**
     * Record latency in milliseconds.
     */
    public void recordLatencyMs(long millis) {
        histogram.recordValue(millis * 1_000_000);
    }
    
    /**
     * Get percentile value in milliseconds.
     */
    public double getPercentileMs(double percentile) {
        return histogram.getValueAtPercentile(percentile) / 1_000_000.0;
    }
    
    /**
     * Get total count of recorded values.
     */
    public long getCount() {
        return histogram.getTotalCount();
    }
    
    /**
     * Get mean latency in milliseconds.
     */
    public double getMeanMs() {
        return histogram.getMean() / 1_000_000.0;
    }
    
    /**
     * Get max latency in milliseconds.
     */
    public double getMaxMs() {
        return histogram.getMaxValue() / 1_000_000.0;
    }
    
    /**
     * Print report to console.
     */
    public void printReport() {
        printReport(System.out);
    }
    
    /**
     * Print report to stream.
     */
    public void printReport(PrintStream out) {
        out.println("\n=== Latency Report ===");
        out.printf("Total operations: %,d%n", getCount());
        out.printf("Mean latency: %.3f ms%n", getMeanMs());
        out.printf("p50 latency:  %.3f ms%n", getPercentileMs(50));
        out.printf("p90 latency:  %.3f ms%n", getPercentileMs(90));
        out.printf("p95 latency:  %.3f ms%n", getPercentileMs(95));
        out.printf("p99 latency:  %.3f ms%n", getPercentileMs(99));
        out.printf("p99.9 latency: %.3f ms%n", getPercentileMs(99.9));
        out.printf("Max latency:  %.3f ms%n", getMaxMs());
    }
    
    /**
     * Save histogram to file for later analysis.
     */
    public void saveToFile(String filename) {
        try (PrintStream out = new PrintStream(new FileOutputStream(filename))) {
            histogram.outputPercentileDistribution(out, 1_000_000.0); // Scale to ms
        } catch (Exception e) {
            System.err.println("Failed to save histogram: " + e.getMessage());
        }
    }
    
    /**
     * Reset the histogram.
     */
    public void reset() {
        histogram.reset();
    }
    
    /**
     * Merge another recorder's data into this one.
     */
    public void merge(LatencyRecorder other) {
        histogram.add(other.histogram);
    }
}
