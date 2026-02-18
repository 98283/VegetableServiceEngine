# Vegetable Service Engine

A simple mobile distributed application: an **Android client** talks to a **Java server** (servlets + RMI) that maintains a vegetable-price table and executes five types of client tasks.

## Architecture

- **Android app**: Sends HTTP requests to the server for each task.
- **Java servlets**: Receive requests, create the appropriate RMI task, and return the result.
- **RMI**: `VegetableComputeEngine` (in RMI registry) holds the vegetable-price table and executes tasks submitted by the servlets via the `Compute` interface.

## Server (RMI + Servlets)

### Classes

| Class | Role |
|-------|------|
| `rmi.Compute` | RMI remote interface: `Object executeTask(Task task)` |
| `rmi.Task` | Serializable interface for tasks: `Object execute(VegetablePriceTable table)` |
| `rmi.VegetablePriceTable` | In-memory vegetable-price table (thread-safe) |
| `rmi.VegetableComputeEngine` | Extends `UnicastRemoteObject`, implements `Compute`, runs tasks on the table |
| `rmi.VegetableComputeTaskRegistry` | Looks up the engine in the RMI registry and runs tasks |
| `rmi.tasks.AddVegetablePrice` | Task: add vegetable-price |
| `rmi.tasks.UpdateVegetablePrice` | Task: update vegetable-price |
| `rmi.tasks.DeleteVegetablePrice` | Task: delete vegetable-price |
| `rmi.tasks.CalVegetableCost` | Task: query price and compute cost (price × quantity) |
| `rmi.tasks.CalculateCost` | Task: generate receipt (cost, amount given, change, cashier) |

### Run the server (local testing)

1. **Start the RMI registry and engine** (port 1099):

   ```bash
   cd server
   mvn compile exec:java -Dexec.mainClass="server.RMIServer"
   ```

   Or run `server.RMIServer` from your IDE. Leave this running.

2. **Build and deploy the web app** (e.g. Tomcat 9):

   ```bash
   mvn package
   ```

   Deploy `target/vegetable-service-engine.war` to Tomcat (e.g. under `http://localhost:8080/vegetable-service-engine`).

3. **Servlet endpoints** (POST, form or query params):

   - Add: `/add` — `vegetableName`, `price`
   - Update: `/update` — `vegetableName`, `price`
   - Delete: `/delete` — `vegetableName`
   - Calculate cost: `/calcost` — `vegetableName`, `quantity`
   - Receipt: `/receipt` — `totalCost`, `amountGiven`, `cashierName`

## Android app

- **Base URL**: In `ApiClient.java`, `BASE_URL` is set to `http://10.0.2.2:8080/vegetable-service-engine` so the **emulator** can reach the host machine’s Tomcat. For a **physical device**, use your PC’s IP (e.g. `http://192.168.1.x:8080/vegetable-service-engine`).
- **Tasks in the UI**:
  1. Add vegetable price  
  2. Update vegetable price  
  3. Delete vegetable  
  4. Calculate vegetable cost (quantity × price)  
  5. Print receipt (total cost, amount given, change, cashier)

Build and run the Android app from Android Studio or:

```bash
cd android
./gradlew assembleDebug
```

## Testing RMI locally

- Run `RMIServer` and the web app on the same machine (localhost).
- For client and server on **different computers**, set `java.rmi.server.hostname` when starting the RMI server to the server’s IP and use that IP in `VegetableComputeTaskRegistry` and in the Android `BASE_URL` for the servlets.

## Requirements checklist

- RMI registry on port **1099** (default).
- **localhost** used for local testing (server and registry on same host).
- **Compute** interface for RMI interaction; **Task** interface for the five task types.
- **VegetableComputeEngine**: extends `UnicastRemoteObject`, implements `Compute`, executes 5 task types.
- **VegetableComputeTaskRegistry**: finds the engine, creates tasks, runs them via the engine.
- Five task classes implementing `Task` and the five servlets for the mobile client.
