package rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Simple test to verify RMI registry is accessible.
 */
public class TestRMIConnection {
    public static void main(String[] args) {
        try {
            System.out.println("Attempting to connect to RMI registry on localhost:1099...");
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            System.out.println("Connected to RMI registry!");
            
            System.out.println("Listing bound objects...");
            String[] names = registry.list();
            if (names.length == 0) {
                System.out.println("  (No objects bound)");
            } else {
                for (String name : names) {
                    System.out.println("  - " + name);
                }
            }
            
            System.out.println("\nLooking for 'VegetableComputeEngine'...");
            try {
                Object obj = registry.lookup("VegetableComputeEngine");
                System.out.println("SUCCESS: VegetableComputeEngine found!");
                System.out.println("Type: " + obj.getClass().getName());
            } catch (Exception e) {
                System.out.println("ERROR: VegetableComputeEngine not found!");
                System.out.println("  " + e.getMessage());
                System.out.println("\nMake sure RMIServer is running and has bound the engine.");
            }
        } catch (Exception e) {
            System.out.println("ERROR: Could not connect to RMI registry!");
            System.out.println("  " + e.getMessage());
            System.out.println("\nMake sure RMIServer is running:");
            System.out.println("  java -cp target\\classes server.RMIServer");
            System.exit(1);
        }
    }
}
