package com.orderflow.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Thread pool for processing orders asynchronously.
 * 
 * ============================================================
 * HOTSPOT #7: Thread Starvation & Pool Sizing
 * ============================================================
 * 
 * PERF-HINT: This executor has configuration problems:
 *   1. Fixed pool of only 2 threads (starvation!)
 *   2. Unbounded queue (memory leak potential)
 *   3. No monitoring of queue depth
 *   4. No rejection handling
 * 
 * PERF-LAB: Lab D - Threading & Executor Tuning
 * 
 * Observable symptoms:
 *   - Queue grows under load
 *   - Low CPU utilization despite pending work
 *   - Eventually OutOfMemoryError from queue
 *   - High latency for queued items
 * 
 * Questions to answer:
 *   - What's the optimal thread count for CPU-bound work?
 *   - For I/O-bound work?
 *   - What queue size prevents memory issues?
 *   - What happens when queue is full?
 */
public class OrderExecutorPool {
    
    // PERF-HINT: Only 2 threads! On an 8-core machine, 75% capacity wasted!
    private static final int THREAD_POOL_SIZE = 2;
    
    // PERF-HINT: Unbounded queue - can grow to exhaust memory
    private final BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
    
    private final ExecutorService executor;
    
    // Monitoring counters
    private long tasksSubmitted = 0;
    private long tasksCompleted = 0;
    private long tasksRejected = 0;
    
    public OrderExecutorPool() {
        // PERF-HINT: Fixed thread pool ignores actual CPU count
        // Consider: Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
        // Or: new ThreadPoolExecutor with bounded queue
        this.executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }
    
    /**
     * Submit a task for execution.
     * 
     * PERF-HINT: No backpressure - just keeps accepting!
     */
    public void submit(Runnable task) {
        // TODO: Implement
        //
        // INTENTIONAL INEFFICIENCY:
        // executor.submit(task);
        // tasksSubmitted++;  // Not thread-safe!
        //
        // Problems:
        // - Unbounded queue acceptance
        // - No backpressure when overloaded
        // - Counter updates not atomic
        //
        // After measuring, consider:
        // - Bounded queue with proper rejection policy
        // - CallerRunsPolicy for backpressure
        // - Proper sizing based on workload type
        //
        throw new UnsupportedOperationException("Implement submit");
    }
    
    /**
     * Submit with timeout - gives up if queue is full.
     */
    public boolean submitWithTimeout(Runnable task, long timeout, TimeUnit unit) {
        // TODO: Implement with bounded queue offer
        throw new UnsupportedOperationException("Implement submitWithTimeout");
    }
    
    /**
     * Get current queue depth for monitoring.
     */
    public int getQueueDepth() {
        // TODO: Return pending task count
        throw new UnsupportedOperationException("Implement getQueueDepth");
    }
    
    /**
     * Get executor statistics.
     */
    public ExecutorStats getStats() {
        // TODO: Implement
        throw new UnsupportedOperationException("Implement getStats");
    }
    
    /**
     * Graceful shutdown.
     */
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Statistics holder.
     */
    public static class ExecutorStats {
        public final long submitted;
        public final long completed;
        public final long rejected;
        public final int queueDepth;
        public final int activeThreads;
        
        public ExecutorStats(long submitted, long completed, long rejected, 
                            int queueDepth, int activeThreads) {
            this.submitted = submitted;
            this.completed = completed;
            this.rejected = rejected;
            this.queueDepth = queueDepth;
            this.activeThreads = activeThreads;
        }
    }
}
