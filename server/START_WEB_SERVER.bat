@echo off
echo ========================================
echo Starting Web Server (Port 8080)
echo ========================================
echo.
echo Make sure RMI Server is running first!
echo.

cd /d "%~dp0"

echo Checking if RMI classes are compiled...
if not exist "target\classes\rmi\VegetableComputeTaskRegistry.class" (
    echo RMI classes not found! Compiling RMI classes first...
    javac -d target\classes -encoding UTF-8 src\main\java\rmi\*.java src\main\java\rmi\tasks\*.java
    if errorlevel 1 (
        echo RMI compilation failed!
        pause
        exit /b 1
    )
)

echo Compiling web server...
REM Include already-compiled classes on classpath so imports resolve
javac -cp target\classes -d target\classes -encoding UTF-8 src\main\java\server\SimpleWebServer.java

if errorlevel 1 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo.
echo Starting web server...
echo Keep this window open!
echo.
java -cp target\classes server.SimpleWebServer

pause
