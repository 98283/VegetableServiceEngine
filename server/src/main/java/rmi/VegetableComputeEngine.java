package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Compute engine that extends UnicastRemoteObject and implements the Compute interface.
 * Holds the vegetable-price table and executes client tasks (Add, Update, Delete, CalCost, Receipt).
 */
public class VegetableComputeEngine extends UnicastRemoteObject implements Compute {
    private static final long serialVersionUID = 1L;

    private final VegetablePriceTable table = new VegetablePriceTable();

    public VegetableComputeEngine() throws RemoteException {
        super();
    }

    @Override
    public Object executeTask(Task task) throws RemoteException {
        if (task == null) {
            return "ERROR: Task cannot be null.";
        }
        return task.execute(table);
    }

    /** Exposed for testing; servlets use tasks only. */
    public VegetablePriceTable getTable() {
        return table;
    }
}
