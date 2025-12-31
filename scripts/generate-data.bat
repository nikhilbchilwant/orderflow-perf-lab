@echo off
REM Generate test order data
REM Usage: generate-data.bat [count] [output_file]
REM
REM Examples:
REM   generate-data.bat                    # 10k orders to datasets/orders.csv
REM   generate-data.bat 100000             # 100k orders
REM   generate-data.bat 1000000 orders-1m.csv

cd /d "%~dp0\.."

set COUNT=%1
if "%COUNT%"=="" set COUNT=10000

set OUTPUT=%2
if "%OUTPUT%"=="" set OUTPUT=datasets\orders-%COUNT%.csv

REM Build if needed
if not exist "orderflow-core\target\classes" (
    echo Building project...
    call mvn compile -pl orderflow-core -q
)

echo Generating %COUNT% orders to %OUTPUT%...
java -cp orderflow-core\target\classes com.orderflow.util.OrderDataGenerator --count %COUNT% --output %OUTPUT%
