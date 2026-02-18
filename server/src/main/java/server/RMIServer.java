package server;

import rmi.Compute;
import rmi.VegetableComputeEngine;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Starts the RMI registry on port 1099 and binds the VegetableComputeEngine.
 * Run this before starting the web application.
 */
public class RMIServer {
    private static final int PORT = 1099;
    private static final String ENGINE_NAME = "VegetableComputeEngine";

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(PORT);
            Compute engine = new VegetableComputeEngine();
            registry.rebind(ENGINE_NAME, engine);
            System.out.println("RMI Registry started on port " + PORT);
            System.out.println("VegetableComputeEngine bound as '" + ENGINE_NAME + "'");
        } catch (RemoteException e) {
            System.err.println("Failed to start RMI server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
