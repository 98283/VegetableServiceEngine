package rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Looks up the VegetableComputeEngine from the RMI registry,
 * creates client tasks, and runs them on the engine.
 */
public class VegetableComputeTaskRegistry {
    private static final String ENGINE_NAME = "VegetableComputeEngine";
    private static final String HOST = "localhost";
    private static final int PORT = 1099;

    private Compute compute;

    /**
     * Look up the vegetable compute engine in the RMI registry.
     * @return true if engine was found
     */
    public boolean lookupEngine() {
        try {
            Registry registry = LocateRegistry.getRegistry(HOST, PORT);
            compute = (Compute) registry.lookup(ENGINE_NAME);
            return true;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Run a task on the compute engine and return the result.
     * @param task the task to execute
     * @return result from the engine, or error message if engine unavailable
     */
    public Object runTask(Task task) {
        if (compute == null && !lookupEngine()) {
            return "ERROR: Vegetable compute engine not available. Start RMI registry and engine first.";
        }
        try {
            return compute.executeTask(task);
        } catch (RemoteException e) {
            e.printStackTrace();
            compute = null;
            return "ERROR: " + e.getMessage();
        }
    }

    public static String getEngineName() {
        return ENGINE_NAME;
    }

    public static int getPort() {
        return PORT;
    }
}
