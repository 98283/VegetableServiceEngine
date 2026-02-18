# Next Steps After Starting RMI Server

## Current Status ✅
- RMI Server is running on port 1099
- VegetableComputeEngine is bound and ready

## Next Step: Start the Web Server (Servlets)

The Android app communicates with the servlets via HTTP. You need to start a web server that runs the servlets.

### Option 1: Use Tomcat (Recommended if you have it)

1. **Compile the servlets:**
   ```powershell
   # Download servlet-api.jar from Apache Tomcat or Maven
   # Place it in a lib folder
   javac -cp "target\classes;lib\servlet-api.jar" -d target\classes src\main\java\servlets\*.java
   ```

2. **Build WAR file:**
   ```powershell
   # Create WEB-INF structure
   mkdir target\vegetable-service-engine\WEB-INF\classes -Force
   xcopy /E /I /Y target\classes target\vegetable-service-engine\WEB-INF\classes
   xcopy /E /I /Y src\main\webapp\* target\vegetable-service-engine\
   
   # Create WAR (or just copy folder to Tomcat webapps)
   ```

3. **Deploy to Tomcat:**
   - Copy `target\vegetable-service-engine` folder to Tomcat's `webapps\` directory
   - Start Tomcat
   - Access at: `http://localhost:8080/vegetable-service-engine`

### Option 2: Use Embedded Jetty Server (Simpler)

1. **Download required JARs:**
   - `servlet-api.jar` (from Tomcat or Maven Central)
   - `jetty-server.jar` (from Eclipse Jetty)
   - `jetty-servlet.jar` (from Eclipse Jetty)
   
   Place them in a `lib` folder.

2. **Compile everything:**
   ```powershell
   javac -cp "target\classes;lib\*" -d target\classes `
     src\main\java\servlets\*.java `
     src\main\java\server\WebServer.java
   ```

3. **Run WebServer:**
   ```powershell
   java -cp "target\classes;lib\*" server.WebServer
   ```

   This will start a web server on port 8080.

### Option 3: Use Android Studio's Built-in Server (Easiest for Testing)

If you just want to test quickly:

1. In Android Studio, go to **Run → Edit Configurations**
2. Add a new **Application** configuration
3. Set main class to a simple HTTP server
4. Or use Android Studio's built-in web server features

## After Web Server is Running:

1. **Verify it's working:**
   - Open browser: `http://localhost:8080/vegetable-service-engine/add?vegetableName=Tomato&price=2.50`
   - Should return: `OK: Added vegetable 'Tomato' with price 2.5`

2. **Run Android App:**
   - In Android Studio, click **Run** (green play button)
   - Select your emulator
   - The app should connect to `http://10.0.2.2:8080/vegetable-service-engine`

## Quick Test Checklist:

- [x] RMI Server running (port 1099) ✅
- [ ] Web Server running (port 8080) ⬅️ **YOU ARE HERE**
- [ ] Android emulator started
- [ ] Android app installed and running
- [ ] Can add vegetable from app
- [ ] Can calculate cost from app

## Troubleshooting:

**"Connection refused" in Android app:**
- Make sure web server is running on port 8080
- Check `BASE_URL` in `ApiClient.java` matches your setup

**"Vegetable compute engine not available":**
- RMI server must be running (you already have this ✅)
- Check firewall isn't blocking port 1099
