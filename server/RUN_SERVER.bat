@echo off
echo ========================================
echo Vegetable Service Engine - Server Setup
echo ========================================
echo.

echo Step 1: Compiling RMI and Server classes...
if not exist "target\classes" mkdir target\classes
javac -d target\classes -encoding UTF-8 src\main\java\rmi\*.java src\main\java\rmi\tasks\*.java src\main\java\server\RMIServer.java
if errorlevel 1 (
    echo Compilation failed! Check errors above.
    pause
    exit /b 1
)
echo RMI classes compiled successfully!
echo.

echo Step 2: Starting RMI Server (port 1099)...
echo Keep this window open!
start "RMI Server" cmd /k "java -cp target\classes server.RMIServer"
timeout /t 3 /nobreak >nul
echo.

echo Step 3: Compiling Servlets...
echo Note: You need servlet-api.jar and jetty-server.jar in target\classes or classpath
echo.
echo For servlets, you need:
echo 1. servlet-api.jar (from Tomcat or download)
echo 2. jetty-server.jar and jetty-servlet.jar (for embedded server)
echo.
echo If you have these JARs, compile with:
echo javac -cp "target\classes;path\to\servlet-api.jar;path\to\jetty-server.jar;path\to\jetty-servlet.jar" -d target\classes src\main\java\servlets\*.java src\main\java\server\WebServer.java
echo.
echo Then run: java -cp "target\classes;path\to\servlet-api.jar;path\to\jetty-server.jar;path\to\jetty-servlet.jar" server.WebServer
echo.
pause
