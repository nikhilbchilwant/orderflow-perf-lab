@echo off
REM Run OrderFlow with default settings
REM Usage: run-baseline.bat [args]

cd /d "%~dp0\.."

REM Build if needed
if not exist "orderflow-core\target\orderflow-core-1.0-SNAPSHOT.jar" (
    echo Building project...
    call mvn clean package -DskipTests -q
)

echo Running OrderFlow baseline...
java -jar orderflow-core\target\orderflow-core-1.0-SNAPSHOT.jar %*
