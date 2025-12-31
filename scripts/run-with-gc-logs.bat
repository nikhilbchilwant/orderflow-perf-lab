@echo off
REM Run OrderFlow with detailed GC logging enabled
REM Usage: run-with-gc-logs.bat [gc_type] [heap_size]
REM
REM gc_type: G1 (default), Parallel, ZGC, Serial
REM heap_size: e.g., 512m, 1g, 2g (default: 512m)
REM
REM Output: profiling/gc-logs/gc-<gc_type>-<timestamp>.log

cd /d "%~dp0\.."

set GC_TYPE=%1
if "%GC_TYPE%"=="" set GC_TYPE=G1

set HEAP_SIZE=%2
if "%HEAP_SIZE%"=="" set HEAP_SIZE=512m

REM Generate timestamp for log file
for /f "tokens=2-4 delims=/ " %%a in ('date /t') do (set DATE=%%c%%a%%b)
for /f "tokens=1-2 delims=: " %%a in ('time /t') do (set TIME=%%a%%b)
set TIMESTAMP=%DATE%_%TIME%

set LOG_FILE=profiling\gc-logs\gc-%GC_TYPE%-%TIMESTAMP%.log

echo Running with %GC_TYPE% GC, heap size: %HEAP_SIZE%
echo GC log: %LOG_FILE%

REM Create output directory
if not exist "profiling\gc-logs" mkdir profiling\gc-logs

REM Select GC flags
if "%GC_TYPE%"=="G1" (
    set GC_FLAGS=-XX:+UseG1GC
) else if "%GC_TYPE%"=="Parallel" (
    set GC_FLAGS=-XX:+UseParallelGC
) else if "%GC_TYPE%"=="ZGC" (
    set GC_FLAGS=-XX:+UseZGC
) else if "%GC_TYPE%"=="Serial" (
    set GC_FLAGS=-XX:+UseSerialGC
) else (
    echo Unknown GC type: %GC_TYPE%
    set GC_FLAGS=-XX:+UseG1GC
)

REM GC logging flags explained:
REM   gc*                      - Log all GC-related events
REM   file=...                 - Log file path
REM   time,uptime,level,tags   - What to include in each log line
REM   filecount=5,filesize=10m - Rotate logs (5 files, 10MB each)

java %GC_FLAGS% ^
     -Xms%HEAP_SIZE% -Xmx%HEAP_SIZE% ^
     -Xlog:gc*:file=%LOG_FILE%:time,uptime,level,tags:filecount=5,filesize=10m ^
     -jar orderflow-core\target\orderflow-core-1.0-SNAPSHOT.jar %3 %4 %5

echo.
echo GC log saved to: %LOG_FILE%
echo.
echo Analyze with:
echo   GCViewer: java -jar gcviewer.jar %LOG_FILE%
echo   GCEasy: Upload to https://gceasy.io
