# Profiling Output Directory

This directory stores profiler output files:

- `jfr-recordings/` — JDK Flight Recorder files (.jfr)
- `gc-logs/` — GC log files
- `flamegraphs/` — Flame graph HTML files from async-profiler
- `async-profiler/` — async-profiler installation (download separately)

## Setup

Download async-profiler (Linux/macOS only):
```bash
wget https://github.com/async-profiler/async-profiler/releases/download/v2.9/async-profiler-2.9-linux-x64.tar.gz
tar -xzf async-profiler-2.9-linux-x64.tar.gz
```

For Windows, use JFR instead (built into JDK).
