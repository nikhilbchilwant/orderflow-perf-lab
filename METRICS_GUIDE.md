# Metrics Guide

How to read and interpret profiler outputs.

---

## JMH Output

Example output:
```
Benchmark                          Mode  Cnt    Score    Error  Units
OrderParsingBenchmark.parseLine   thrpt    5  125.432 ± 12.345  ops/ms
```

| Column | Meaning |
|--------|---------|
| Mode | `thrpt` = throughput, `avgt` = average time |
| Cnt | Number of measurement iterations |
| Score | Result (higher = better for thrpt) |
| Error | 99.9% confidence interval |
| Units | ops/ms, ms/op, etc. |

### With GC Profiler (`-prof gc`)
```
gc.alloc.rate.norm: 1024 bytes/op
gc.count: 5
gc.time: 120 ms
```

| Metric | Good | Bad | Action |
|--------|------|-----|--------|
| gc.alloc.rate.norm | < 100 B/op | > 1KB/op | Reduce allocations |
| gc.count | Low | High | Object pooling or fewer allocs |

---

## JFR / Mission Control

### Hot Methods Tab
- **Self time** — Time in this method only
- **Total time** — Time including callees
- Look for: Methods taking >10% of samples

### Memory > Allocations
- Sort by **Total Allocation**
- Look for: `char[]`, `ArrayList`, boxed types

### Threads > Lock Contention
- **Monitor blocked** — Time waiting for locks
- Look for: Any method with >1s blocked time

### GC > Pauses
- **Pause time distribution** — Histogram of pause durations
- Look for: Pauses >100ms

---

## GC Log Analysis

### Key Metrics in GC Log
```
[2024-01-15T10:30:00.123+0000][gc] GC(42) Pause Young (Normal) 512M->256M(1024M) 25.123ms
```

| Field | Meaning |
|-------|---------|
| GC(42) | 42nd GC event |
| Pause Young | Young generation collection |
| 512M->256M | Heap before->after |
| (1024M) | Max heap |
| 25.123ms | Pause duration |

### Good vs Bad Signals

| Metric | Good | Warning | Critical |
|--------|------|---------|----------|
| Young GC pause | <50ms | 50-200ms | >200ms |
| Full GC frequency | Rare | Hourly | Every few minutes |
| Heap after GC | <50% max | 50-80% | >80% |

---

## async-profiler Flame Graphs

### Reading Flame Graphs
- **X-axis** = Proportion of samples (NOT time order!)
- **Y-axis** = Stack depth (bottom = entry point)
- **Width** = Time spent (wider = more time)

### What to Look For
1. **Wide plateaus at top** — Direct hotspots
2. **Wide sections anywhere** — Significant callers
3. **GC frames** — `GCTaskThread`, `G1ParScanThreadState`

### CPU vs Allocation Profiles
- **CPU profile** — Where is time spent?
- **Alloc profile** — Where are objects created?

---

## HDR Histogram Output

```
       Value     Percentile TotalCount
       1.234      0.500000      1000
       5.678      0.900000      1800
      12.345      0.950000      1900
      45.678      0.990000      1980
     123.456      0.999000      1998
```

| Percentile | Meaning |
|------------|---------|
| p50 | Median — half of requests faster |
| p90 | 90% faster, 10% slower |
| p95 | Typical SLA target |
| p99 | Tail latency |
| p99.9 | Extreme outliers |

### Interpreting Latencies

| Percentile | Good | Acceptable | Problem |
|------------|------|------------|---------|
| p50 | <10ms | <50ms | >100ms |
| p95 | <50ms | <200ms | >500ms |
| p99 | <100ms | <500ms | >1s |

---

## Quick Diagnostics

### High CPU, Low Throughput
→ Look at: Lock contention, inefficient algorithms

### High Allocation Rate
→ Look at: Object creation in loops, boxing

### Increasing Heap Over Time
→ Look at: Memory leaks, unbounded caches

### Long GC Pauses
→ Look at: Heap too large, many long-lived objects

### High p99 but Low p50
→ Look at: GC pauses, lock contention, I/O stalls
