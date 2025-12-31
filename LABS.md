# Performance Labs

Step-by-step exercises for Java performance tuning. Complete these in order.

---

## Lab A: Baseline Collection & Hypothesis Formation

### Objective
Establish baseline metrics and form optimization hypotheses.

### Steps

1. **Implement the basic skeletons** — Complete the `TODO` items in:
   - `Order.java` (constructor, getters)
   - `OrderParser.java` (implement parse with inefficient patterns)
   - `OrderIngestionService.java` (wire together)
   - At minimum, enough to process orders

2. **Build and generate test data**
   ```bash
   mvn clean package -DskipTests
   python datasets/generate-orders.py --count 100000 --output datasets/orders-100k.csv
   ```

3. **Run baseline with JFR**
   ```bash
   scripts\run-with-jfr.bat 60 baseline
   ```

4. **Collect GC logs**
   ```bash
   scripts\run-with-gc-logs.bat G1 512m
   ```

5. **Record baseline metrics**

   | Metric | Value | Notes |
   |--------|-------|-------|
   | Total time | ___ s | |
   | Throughput | ___ orders/sec | |
   | p95 latency | ___ ms | |
   | GC pause max | ___ ms | |
   | Alloc rate | ___ MB/s | |

6. **Analyze and hypothesize**
   - Open JFR in JMC: `jmc -open profiling\jfr-recordings\baseline.jfr`
   - Look at **Hot Methods** tab
   - Look at **Memory > Allocations by Class**
   - Write down your top 3 hypotheses

### Reflection Questions
- Which 3 methods consume the most CPU?
- What are the top 3 allocation sites?
- Is GC a significant factor (>10% of time)?

---

## Lab B: Allocation Reduction

### Objective
Reduce garbage generation by 50%+.

### Target Files
- `OrderParser.java` — String handling
- `OrderIngestionService.java` — Collection creation  
- `AnalyticsService.java` — Stream boxing

### Investigation Steps

1. **Profile allocations**
   ```bash
   scripts\run-benchmark.bat Allocation -prof gc
   ```

2. **Look for these patterns** in async-profiler/JFR allocation view:
   - `char[]` — String concatenation
   - `Integer`, `Double` — Boxing
   - `ArrayList` — Collection growth
   - `Pattern` — Regex compilation

3. **In OrderParser.java**, find:
   - Pattern compiled inside method (hint: line ~40)
   - String.split() intermediate arrays
   - String concatenation for error messages

4. **In AnalyticsService.java**, find:
   - `Stream<Double>` instead of `DoubleStream`
   - Multiple passes over same data

### Metrics to Compare
| Before | After | Change |
|--------|-------|--------|
| gc.alloc.rate.norm = ___ bytes/op | ___ bytes/op | -__% |
| Throughput = ___ ops/ms | ___ ops/ms | +__% |

### Expected Improvement
- Allocation rate: 60-80% reduction
- Throughput: 10-30% improvement

### Hints (Not Solutions)
- Pre-compile static `Pattern`
- `indexOf` + `substring` instead of `split`
- `mapToDouble()` instead of `map().boxed()`
- `ArrayList` with initial capacity

---

## Lab C: GC Tuning & Heap Sizing

### Objective
Minimize GC pause times while maintaining throughput.

### Experiments

Run each configuration and record metrics:

```bash
# Experiment 1: Default heap
scripts\run-with-gc-logs.bat G1 512m

# Experiment 2: Larger heap
scripts\run-with-gc-logs.bat G1 2g

# Experiment 3: ZGC (JDK 17+)
scripts\run-with-gc-logs.bat ZGC 2g

# Experiment 4: Parallel GC (throughput focused)
scripts\run-with-gc-logs.bat Parallel 2g
```

### Metrics to Record
| Config | Throughput | p99 Latency | Max Pause | GC Time % |
|--------|-----------|-------------|-----------|-----------|
| G1 512m | | | | |
| G1 2g | | | | |
| ZGC 2g | | | | |
| Parallel 2g | | | | |

### Questions to Answer
- Does doubling heap size halve GC frequency?
- Which GC has lowest pause times?
- Which GC has highest throughput?
- What's the memory vs latency trade-off?

---

## Lab D: Threading & Executor Tuning

### Objective
Eliminate thread starvation, optimize pool sizes.

### Target Files
- `OrderExecutorPool.java`

### Current Problem
The pool uses only **2 threads** regardless of CPU count.

### Experiments

1. **Observe starvation**
   - Run load test
   - Monitor queue depth growth
   - Note CPU utilization (should be low!)

2. **Try different pool sizes**
   - Modify `THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors()`
   - Try `availableProcessors() * 2` for I/O-bound work

3. **Bounded queue experiment**
   - Replace `LinkedBlockingQueue` with `ArrayBlockingQueue(1000)`
   - Implement rejection handling

### Metrics
| Pool Size | Queue Depth | CPU % | Throughput |
|-----------|-------------|-------|------------|
| 2 | | | |
| N (CPU count) | | | |
| 2N | | | |

### Questions
- Is the workload CPU-bound or I/O-bound?
- What happens when queue is bounded and fills up?

---

## Lab E: Lock Contention Mitigation

### Objective
Reduce lock contention by 80%+.

### Target Files
- `MatchingEngine.java` — coarse-grained `synchronized(this)`
- `OrderBook.java` — data structure access

### Current Problem
All order submissions lock on the same object, even for different symbols!

### Investigation

1. **Run multi-threaded benchmark**
   ```bash
   scripts\run-benchmark.bat Matching -t 4
   ```

2. **Compare single vs multi-thread throughput**
   - If 4 threads ≠ ~4x throughput, you have contention

3. **Use JFR lock analysis**
   - Look at Threading > Lock Contention Events

### Optimization Approaches (Hints)
- Lock per symbol (finer granularity)
- `ConcurrentHashMap` for order books
- `ReadWriteLock` for read-heavy operations
- Lock-free structures for simple operations

### Metrics
| Config | 1 Thread | 4 Threads | Scaling |
|--------|----------|-----------|---------|
| Before | ___ ops/ms | ___ ops/ms | ___x |
| After | ___ ops/ms | ___ ops/ms | ___x |

---

## Lab F: I/O & Batching Optimization

### Objective
Improve I/O throughput 5x+.

### Target Files
- `PersistenceService.java` — N+1 database pattern
- `OrderFileWriter.java` — unbuffered writes

### Current Problems
1. New database connection per order
2. Single INSERT per order (no batching)
3. File open/close per write
4. No buffering

### Experiments

1. **Measure current I/O overhead**
   - Profile with JFR, look at I/O time

2. **Add connection pooling** (or reuse single connection)

3. **Implement JDBC batching**
   ```java
   // Hint structure (implement yourself)
   conn.setAutoCommit(false);
   for (Order order : batch) {
       stmt.addBatch();
   }
   stmt.executeBatch();
   conn.commit();
   ```

4. **Test different batch sizes**
   - 1, 10, 100, 1000

### Metrics
| Batch Size | Orders/sec | I/O Time % |
|------------|-----------|------------|
| 1 (current) | | |
| 10 | | |
| 100 | | |
| 1000 | | |

---

## Lab G: Caching Strategy Experiment

### Objective
Implement effective caching with proper eviction.

### Target Files
- `CacheService.java`

### Current Problems
1. No size limit (memory leak)
2. `HashMap` not thread-safe
3. No eviction policy

### Experiments

1. **Measure memory growth**
   - Process 100k orders, check heap size
   - Process 1M orders, check heap size

2. **Add thread safety**
   - Replace with `ConcurrentHashMap`

3. **Add size-based eviction**
   - Implement simple LRU or use Caffeine

4. **Compare cache libraries**
   - Your implementation vs Caffeine

### Metrics
| Implementation | Memory at 100k | Memory at 1M | Hit Rate | Lookup Time |
|---------------|----------------|--------------|----------|-------------|
| HashMap (current) | | OOM | N/A | |
| ConcurrentHashMap + LRU | | | | |
| Caffeine | | | | |

---

## Lab H: Microbenchmark vs Real-World Comparison

### Objective
Understand when microbenchmarks mislead.

### Experiment

1. **Pick an optimization from Lab B** (e.g., pre-compiled Pattern)

2. **Measure microbenchmark improvement**
   ```bash
   scripts\run-benchmark.bat Parsing -prof gc
   ```
   Record ops/ms before and after

3. **Measure full application improvement**
   - Run complete load test before and after
   - Record throughput

4. **Compare the deltas**

| Change | JMH Improvement | App Improvement |
|--------|-----------------|-----------------|
| Pre-compiled Pattern | ___% faster | ___% faster |
| StringBuilder | ___% faster | ___% faster |
| Primitive streams | ___% faster | ___% faster |

### Reflection Questions
- Did microbenchmark gains translate to app gains?
- What factors cause the difference? (JIT warmup, GC, cache effects)
- When should you trust microbenchmarks?
- When should you prefer end-to-end testing?

---

## Results Template

Copy this for each lab:

```markdown
## Lab [X] Results

### Before
- JVM flags: ___
- Throughput: ___ ops/sec
- p95 latency: ___ ms
- Allocation rate: ___ MB/s
- Key observation: ___

### Changes Made
1. ___
2. ___

### After
- Throughput: ___ ops/sec (Δ __%)
- p95 latency: ___ ms (Δ __%)
- Allocation rate: ___ MB/s (Δ __%)

### Lessons Learned
- ___
```
