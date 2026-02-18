@echo off
echo ========================================
echo Starting RMI Server (Port 1099)
echo ========================================
echo.

cd /d "%~dp0"

echo Checking if RMI classes are compiled...
if not exist "target\classes" mkdir target\classes

REM First compile RMI classes if needed
if not exist "target\classes\rmi\VegetableComputeEngine.class" (
    echo Compiling RMI classes...
    javac -d target\classes -encoding UTF-8 src\main\java\rmi\*.java src\main\java\rmi\tasks\*.java
    if errorlevel 1 (
        echo RMI compilation failed!
        pause
        exit /b 1
    )
)

REM Then compile RMIServer (needs RMI classes on classpath)
if not exist "target\classes\server\RMIServer.class" (
    echo Compiling RMIServer...
    javac -cp target\classes -d target\classes -encoding UTF-8 src\main\java\server\RMIServer.java
    if errorlevel 1 (
        echo RMIServer compilation failed!
        pause
        exit /b 1
    )
    echo All classes compiled successfully!
) else (
    echo Classes already compiled.
)

echo.
echo Starting RMI Server...
echo Keep this window open - DO NOT CLOSE IT!
echo.
echo You should see:
echo   "RMI Registry started on port 1099"
echo   "VegetableComputeEngine bound as 'VegetableComputeEngine'"
echo.

java -cp target\classes server.RMIServer

if errorlevel 1 (
    echo.
    echo ERROR: RMI Server failed to start!
    echo Check the error message above.
    pause
)
