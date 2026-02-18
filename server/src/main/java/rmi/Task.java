package rmi;

import java.io.Serializable;

/**
 * Interface for client tasks executed by the VegetableComputeEngine.
 * All task implementations (Add, Update, Delete, CalCost, Receipt) implement this.
 */
public interface Task extends Serializable {
    /**
     * Execute this task using the provided vegetable price table.
     * @param table the vegetable price table (provided by the engine)
     * @return result of the execution (type depends on task)
     */
    Object execute(VegetablePriceTable table);
}
