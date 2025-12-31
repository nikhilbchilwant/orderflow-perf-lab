@echo off
REM Run JMH microbenchmarks
REM Usage: run-benchmark.bat [benchmark_pattern] [extra_jmh_args]
REM
REM Examples:
REM   run-benchmark.bat                        # Run all benchmarks
REM   run-benchmark.bat OrderParsing           # Run OrderParsingBenchmark
REM   run-benchmark.bat Allocation -prof gc    # Run AllocationBenchmark with GC profiler
REM   run-benchmark.bat Matching -t 4          # Run MatchingEngine with 4 threads

cd /d "%~dp0\.."

set PATTERN=%1
if "%PATTERN%"=="" set PATTERN=.*

REM Build benchmark jar if needed
if not exist "orderflow-benchmark\target\benchmarks.jar" (
    echo Building benchmarks...
    call mvn clean package -DskipTests -q
)

echo.
echo Running benchmarks matching: %PATTERN%
echo.
echo JMH Options used:
echo   -f 2   : 2 forks (separate JVM invocations)
echo   -wi 3  : 3 warmup iterations
echo   -i 5   : 5 measurement iterations
echo   -tu ms : Time unit is milliseconds
echo.

REM Shift to get remaining arguments
shift
set JMH_ARGS=

:argloop
if "%1"=="" goto endargs
set JMH_ARGS=%JMH_ARGS% %1
shift
goto argloop
:endargs

java -jar orderflow-benchmark\target\benchmarks.jar %PATTERN% -f 2 -wi 3 -i 5 -tu ms %JMH_ARGS%

echo.
echo Benchmark complete.
echo.
echo Tips:
echo   - Add -prof gc to see allocation rates
echo   - Add -prof stack to see hotspots  
echo   - Add -t N to run with N threads
echo   - Add -p paramName=value to set @Param values
