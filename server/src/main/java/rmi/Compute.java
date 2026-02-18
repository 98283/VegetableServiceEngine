package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * RMI remote interface for the compute engine.
 * Clients submit tasks that are executed on the server.
 */
public interface Compute extends Remote {
    /**
     * Execute a task on the compute engine and return the result.
     * @param task the task to execute
     * @return the result of the task execution
     */
    Object executeTask(Task task) throws RemoteException;
}
