@echo off
REM Run async-profiler for CPU or allocation profiling
REM Usage: run-async-profiler.bat <cpu|alloc> <duration_seconds> <output_name>
REM
REM Prerequisites: 
REM   - Download async-profiler from https://github.com/async-profiler/async-profiler
REM   - Extract to profiling\async-profiler\
REM   - On Windows, async-profiler requires Windows-specific build or WSL

cd /d "%~dp0\.."

set EVENT=%1
if "%EVENT%"=="" set EVENT=cpu

set DURATION=%2
if "%DURATION%"=="" set DURATION=30

set OUTPUT=%3
if "%OUTPUT%"=="" set OUTPUT=profile

echo.
echo =====================================================
echo IMPORTANT: async-profiler on Windows
echo =====================================================
echo.
echo async-profiler works best on Linux/macOS.
echo For Windows, you have two options:
echo.
echo 1. Use WSL (Windows Subsystem for Linux)
echo    - Run your Java app in WSL
echo    - Use async-profiler Linux build
echo.
echo 2. Use JFR instead (built into JDK)
echo    - scripts\run-with-jfr.bat %DURATION% %OUTPUT%
echo    - Analyze with JDK Mission Control
echo.
echo 3. Use VisualVM
echo    - Download from https://visualvm.github.io/
echo    - Attach to running Java process
echo    - Use CPU or Memory profiler
echo.

echo For reference, here's how you would run async-profiler on Linux:
echo.
echo # CPU profiling:
echo ./profiling/async-profiler/profiler.sh -d %DURATION% -f profiling/flamegraphs/%OUTPUT%-cpu.html ^<PID^>
echo.
echo # Allocation profiling:
echo ./profiling/async-profiler/profiler.sh -e alloc -d %DURATION% -f profiling/flamegraphs/%OUTPUT%-alloc.html ^<PID^>
echo.
echo # Wall-clock profiling (includes waiting time):
echo ./profiling/async-profiler/profiler.sh -e wall -d %DURATION% -f profiling/flamegraphs/%OUTPUT%-wall.html ^<PID^>

pause
