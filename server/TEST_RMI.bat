@echo off
echo ========================================
echo Testing RMI Connection
echo ========================================
echo.

cd /d "%~dp0"

echo Checking if RMI classes are compiled...
if not exist "target\classes\rmi\VegetableComputeTaskRegistry.class" (
    echo RMI classes not found! Compiling...
    if not exist "target\classes" mkdir target\classes
    javac -d target\classes -encoding UTF-8 src\main\java\rmi\*.java src\main\java\rmi\tasks\*.java src\main\java\server\RMIServer.java
    if errorlevel 1 (
        echo Compilation failed!
        pause
        exit /b 1
    )
)

echo.
echo Testing connection to RMI registry on localhost:1099...
javac -cp target\classes -d target\classes -encoding UTF-8 src\main\java\rmi\TestRMIConnection.java
java -cp target\classes rmi.TestRMIConnection

if errorlevel 1 (
    echo.
    echo ERROR: Could not connect to RMI registry!
    echo.
    echo Make sure RMIServer is running:
    echo   1. Open a NEW terminal window
    echo   2. Run: cd server
    echo   3. Run: java -cp target\classes server.RMIServer
    echo   4. You should see: "RMI Registry started on port 1099"
    echo.
) else (
    echo.
    echo SUCCESS: RMI connection test passed!
)

pause
