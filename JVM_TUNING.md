# JVM & GC Tuning Reference

Quick reference for JVM flags used in performance tuning.

---

## Garbage Collectors

### G1GC (Default in JDK 9+)
Best for: General purpose, balanced latency/throughput

```bash
java -XX:+UseG1GC \
     -Xmx2g \
     -XX:MaxGCPauseMillis=200 \
     -XX:G1HeapRegionSize=16m \
     -jar app.jar
```

| Flag | Description |
|------|-------------|
| `-XX:MaxGCPauseMillis=N` | Target pause time (ms) |
| `-XX:G1HeapRegionSize=N` | Region size (1-32MB, power of 2) |
| `-XX:InitiatingHeapOccupancyPercent=N` | When to start concurrent marking |

### ZGC (JDK 15+)
Best for: Ultra-low latency (sub-ms pauses)

```bash
java -XX:+UseZGC \
     -Xmx4g \
     -XX:+ZUncommit \
     -jar app.jar
```

| Flag | Description |
|------|-------------|
| `-XX:+ZUncommit` | Return unused memory to OS |
| `-XX:SoftMaxHeapSize=N` | Soft limit (ZGC will try to stay under) |

### Parallel GC
Best for: Batch processing, max throughput

```bash
java -XX:+UseParallelGC \
     -Xmx8g \
     -XX:ParallelGCThreads=8 \
     -jar app.jar
```

### Serial GC
Best for: Single-core, small heaps (<100MB)

```bash
java -XX:+UseSerialGC \
     -Xmx64m \
     -jar app.jar
```

---

## Heap Sizing

```bash
# Fixed heap (recommended for stable workloads)
-Xms2g -Xmx2g

# Variable heap (JVM grows/shrinks)
-Xms512m -Xmx2g

# Large heap with explicit regions
-Xmx8g -XX:G1HeapRegionSize=32m
```

**Rule of thumb:**
- Batch jobs: Larger heap, less GC frequency
- Latency-sensitive: Sized for <30% occupancy after full GC

---

## Logging & Diagnostics

### GC Logging (JDK 9+)
```bash
-Xlog:gc*:file=gc.log:time,uptime,level,tags:filecount=5,filesize=10m
```

### JFR Recording
```bash
-XX:+FlightRecorder
-XX:StartFlightRecording=duration=60s,filename=app.jfr
```

### Heap Dumps on OOM
```bash
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/path/to/dumps
```

---

## Common Tuning Scenarios

### Scenario 1: High Allocation Rate
**Symptom:** GC runs frequently, short pauses but many of them

**Solutions:**
1. Reduce allocations in code (Lab B)
2. Increase young generation: `-XX:NewRatio=1`
3. Increase heap if allocations are necessary

### Scenario 2: Long GC Pauses
**Symptom:** Occasional pauses > 500ms

**Solutions:**
1. Switch to ZGC or Shenandoah
2. For G1: Lower `-XX:MaxGCPauseMillis`
3. Reduce heap size (smaller = faster to scan)

### Scenario 3: Memory Growing Unbounded
**Symptom:** Heap usage climbs over time, eventual OOM

**Check:**
1. Memory leak in code (Lab G - cache)
2. `jmap -histo:live <pid>` to see live objects
3. Take heap dump and analyze with MAT/VisualVM

### Scenario 4: Container Memory Limits
**Symptom:** Killed by OOM killer in container

**Solutions:**
```bash
# Respect container limits (default in JDK 10+)
-XX:+UseContainerSupport

# Set max heap to 75% of container memory
-XX:MaxRAMPercentage=75.0
```

---

## JIT Compilation

### Tiered Compilation (Default)
```bash
-XX:+TieredCompilation  # On by default
```

### Disable C2 (Faster startup, less peak perf)
```bash
-XX:TieredStopAtLevel=1
```

### Print Compilation
```bash
-XX:+PrintCompilation
```

---

## Quick Copy-Paste Configs

### Low-Latency Trading
```bash
java -Xmx4g -XX:+UseZGC -XX:+ZUncommit -jar app.jar
```

### Batch Processing
```bash
java -Xmx8g -XX:+UseParallelGC -XX:ParallelGCThreads=8 -jar app.jar
```

### Memory-Constrained Container
```bash
java -Xmx384m -XX:+UseSerialGC -XX:+UseContainerSupport -jar app.jar
```

### Debugging/Profiling
```bash
java -Xmx2g -XX:+UseG1GC \
     -XX:+FlightRecorder \
     -XX:StartFlightRecording=duration=60s,filename=debug.jfr \
     -Xlog:gc*:file=gc.log:time,level,tags \
     -jar app.jar
```
