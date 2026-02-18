# Running Vegetable Service Engine on Android Emulator

## Prerequisites

1. **Android Studio** installed and configured
2. **Java JDK** (for running the RMI server)
3. **Android Emulator** set up

## Step-by-Step Instructions

### Step 1: Start the RMI Server (Required First!)

The Android app communicates with the server, so you **must start the RMI server first**.

#### Option A: Using Maven (if installed)
```bash
cd server
mvn compile exec:java -Dexec.mainClass="server.RMIServer"
```

#### Option B: Using Java directly
1. Open a terminal/command prompt
2. Navigate to the `server` directory
3. Compile the RMI classes:
   ```bash
   javac -d target/classes -encoding UTF-8 src/main/java/rmi/*.java src/main/java/rmi/tasks/*.java src/main/java/server/*.java
   ```
4. Run the RMI server:
   ```bash
   java -cp target/classes server.RMIServer
   ```

**Expected output:**
```
RMI Registry started on port 1099
VegetableComputeEngine bound as 'VegetableComputeEngine'
```

**Keep this terminal window open** - the RMI server must stay running.

### Step 2: Deploy the Web Application (Tomcat or similar)

The servlets need to be running. You have two options:

#### Option A: Deploy WAR to Tomcat
1. Build the WAR:
   ```bash
   cd server
   mvn package
   ```
2. Copy `target/vegetable-service-engine.war` to Tomcat's `webapps/` directory
3. Start Tomcat (usually runs on port 8080)

#### Option B: Run from Android Studio (for testing)
- The servlets can be run directly if you set up a local web server
- Or use Android Studio's built-in web server capabilities

**Note:** The Android app expects the server at `http://10.0.2.2:8080/vegetable-service-engine` (emulator's localhost).

### Step 3: Open Project in Android Studio

1. Open Android Studio
2. Click **File → Open**
3. Navigate to and select the `android` folder
4. Click **OK**
5. Wait for Gradle sync to complete

### Step 4: Configure the Emulator

1. Click **Tools → Device Manager** (or the device icon in toolbar)
2. Click **Create Device** (if no emulator exists)
3. Select a device (e.g., **Pixel 5**)
4. Click **Next**
5. Download a system image (e.g., **API 34** or **API 33**)
6. Click **Next** → **Finish**

### Step 5: Verify API Client Configuration

1. Open `app/src/main/java/com/vegetable/service/engine/ApiClient.java`
2. Verify `BASE_URL` is set to:
   ```java
   public static final String BASE_URL = "http://10.0.2.2:8080/vegetable-service-engine";
   ```
   - `10.0.2.2` is the Android emulator's special IP for the host machine's localhost
   - Port `8080` assumes Tomcat default (adjust if different)
   - Path `/vegetable-service-engine` matches your WAR context path

### Step 6: Start the Emulator

1. In **Device Manager**, click the **Play** button next to your emulator
2. Wait for the emulator to boot (may take 1-2 minutes)

### Step 7: Run the App

1. In Android Studio, click **Run → Run 'app'** (or press `Shift+F10`)
2. Select your running emulator from the device list
3. Click **OK**
4. The app will build and install on the emulator

### Step 8: Test the Application

Once the app launches:

1. **Add Vegetable Price:**
   - Enter vegetable name (e.g., "Tomato")
   - Enter price (e.g., "2.50")
   - Click **Add**
   - Check result message below

2. **Update Vegetable Price:**
   - Enter existing vegetable name
   - Enter new price
   - Click **Update**

3. **Delete Vegetable:**
   - Enter vegetable name
   - Click **Delete**

4. **Calculate Cost:**
   - Enter vegetable name
   - Enter quantity (e.g., "3")
   - Click **Calculate cost**
   - Result shows: price × quantity = total cost

5. **Print Receipt:**
   - Enter total cost (e.g., "7.50")
   - Enter amount given (e.g., "10.00")
   - Enter cashier name (e.g., "John Doe")
   - Click **Print receipt**
   - Receipt shows cost, amount given, change due, and cashier

## Troubleshooting

### "Network error" or connection refused
- **Check RMI server is running** (Step 1)
- **Check web server is running** (Step 2)
- Verify `BASE_URL` in `ApiClient.java` matches your server setup
- For physical device: Change `10.0.2.2` to your PC's IP address (e.g., `192.168.1.100`)

### "Vegetable compute engine not available"
- RMI server must be running on port 1099
- Check firewall isn't blocking port 1099

### App crashes on launch
- Check AndroidManifest.xml has INTERNET permission
- Verify `usesCleartextTraffic="true"` is set (for HTTP, not HTTPS)

### Gradle sync fails
- Check internet connection (Gradle downloads dependencies)
- Try **File → Invalidate Caches / Restart**

## Quick Test Checklist

- [ ] RMI server running (port 1099)
- [ ] Web server running (port 8080)
- [ ] Emulator started
- [ ] App installed and launched
- [ ] Can add a vegetable successfully
- [ ] Can calculate cost successfully

## Notes

- **For physical Android device:** Change `BASE_URL` to your PC's IP address (find with `ipconfig` on Windows or `ifconfig` on Mac/Linux)
- The emulator uses `10.0.2.2` to access the host machine's localhost
- Both RMI server and web server must be running simultaneously
