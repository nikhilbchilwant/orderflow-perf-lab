@echo off
REM Run OrderFlow with JDK Flight Recorder enabled
REM Usage: run-with-jfr.bat [duration_seconds] [recording_name]
REM
REM Output: profiling/jfr-recordings/<recording_name>.jfr

cd /d "%~dp0\.."

set DURATION=%1
if "%DURATION%"=="" set DURATION=60

set RECORDING=%2
if "%RECORDING%"=="" set RECORDING=recording

set OUTPUT=profiling\jfr-recordings\%RECORDING%.jfr

echo Starting JFR recording for %DURATION% seconds...
echo Output: %OUTPUT%

REM Create output directory
if not exist "profiling\jfr-recordings" mkdir profiling\jfr-recordings

REM JFR flags explained:
REM   +FlightRecorder          - Enable Flight Recorder
REM   StartFlightRecording     - Start recording immediately
REM   duration                 - How long to record
REM   filename                 - Where to save
REM   settings=profile         - Use 'profile' preset (more detail, more overhead)
REM                              Use 'default' for less overhead in production

java -XX:+FlightRecorder ^
     -XX:StartFlightRecording=duration=%DURATION%s,filename=%OUTPUT%,settings=profile ^
     -jar orderflow-core\target\orderflow-core-1.0-SNAPSHOT.jar %3 %4 %5

echo.
echo Recording saved to: %OUTPUT%
echo Analyze with: jmc -open %OUTPUT%
echo Or: jfr print --summary %OUTPUT%
