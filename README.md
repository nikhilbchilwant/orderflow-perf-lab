# OrderFlow Performance Lab

A hands-on Java performance tuning learning project. Learn to identify and fix performance issues by measuring, hypothesizing, and iterating.

## 🎯 Learning Objectives

By completing this project, you will:
- Profile Java applications with JFR, async-profiler, and JMH
- Identify allocation hotspots and reduce GC pressure
- Diagnose and fix lock contention
- Tune JVM garbage collectors for different workloads
- Understand the gap between microbenchmarks and real-world performance

## 📋 Prerequisites

| Tool | Version | Installation |
|------|---------|--------------|
| JDK | 17+ (21 recommended) | [Adoptium](https://adoptium.net/) |
| Maven | 3.8+ | [Maven](https://maven.apache.org/download.cgi) |
| JDK Mission Control | 8.3+ | [JMC](https://www.oracle.com/java/technologies/jdk-mission-control.html) |

## 🚀 Quick Start

```bash
# Build the project
mvn clean package -DskipTests

# Generate test data (pure Java)
scripts\generate-data.bat 100000

# Run baseline (implement the skeletons first!)
scripts\run-baseline.bat

# Run benchmarks
scripts\run-benchmark.bat
```

## 📁 Project Structure

```
orderflow-perf-lab/
├── orderflow-core/          # Main application (hotspots here!)
├── orderflow-benchmark/     # JMH microbenchmarks
├── orderflow-loadgen/       # Load generator for testing
├── scripts/                 # Profiling run scripts
├── profiling/               # Profiler output directory
├── datasets/                # Test data files
└── results/                 # Your measurement results
```

## 🔬 Performance Labs

Start with **Lab A** and progress sequentially:

| Lab | Focus | Target Code |
|-----|-------|-------------|
| **A** | Baseline & Hypothesis | Full application |
| **B** | Allocation Reduction | `OrderParser`, `AnalyticsService` |
| **C** | GC Tuning | JVM flags |
| **D** | Thread Pool Sizing | `OrderExecutorPool` |
| **E** | Lock Contention | `MatchingEngine`, `OrderBook` |
| **F** | I/O & Batching | `PersistenceService`, `OrderFileWriter` |
| **G** | Caching | `CacheService` |
| **H** | Microbench vs Reality | All benchmarks |

See [LABS.md](LABS.md) for detailed instructions.

## 📊 Measurement Commands

### JDK Flight Recorder
```bash
scripts\run-with-jfr.bat 60 baseline
# Open with: jmc -open profiling\jfr-recordings\baseline.jfr
```

### GC Logging
```bash
scripts\run-with-gc-logs.bat G1 1g
# Analyze: Upload profiling\gc-logs\*.log to https://gceasy.io
```

### JMH Benchmarks
```bash
# All benchmarks
scripts\run-benchmark.bat

# Specific benchmark with GC profiling
scripts\run-benchmark.bat Allocation -prof gc

# Multi-threaded contention test
scripts\run-benchmark.bat Matching -t 4
```

## 📈 Success Metrics

| Metric | Baseline Goal | Optimized Goal |
|--------|---------------|----------------|
| Throughput | Establish | 3x baseline |
| p95 Latency | Measure | < 50ms |
| Allocation Rate | > 500MB/s | < 200MB/s |
| GC Pause Max | Measure | < 50ms |

## ⚠️ Key Principles

> **Don't guess, measure.** Your intuition about performance is often wrong.

> **Warmup matters.** JIT compilation changes everything. Let the JVM warm up.

> **Context matters.** A 10x microbenchmark win may be 1% in the real app.

> **One change at a time.** Otherwise you won't know what helped.

## 📚 Additional Resources

- [JVM Tuning Guide](JVM_TUNING.md) - JVM flags and GC tuning
- [Metrics Guide](METRICS_GUIDE.md) - How to read profiler outputs
- [Labs Guide](LABS.md) - Detailed lab instructions

## 🛠️ Implementing the Skeletons

This project contains intentionally incomplete code. Look for:

```java
// TODO: Implement
throw new UnsupportedOperationException("Implement this method");
```

And `PERF-HINT` comments that guide you toward the inefficiencies:

```java
// PERF-HINT: Pattern is compiled on EVERY call!
// PERF-LAB: Lab B targets this area
```

Implement the inefficient version first, measure, then optimize!
