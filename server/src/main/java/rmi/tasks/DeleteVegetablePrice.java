package rmi.tasks;

import rmi.Task;
import rmi.VegetablePriceTable;

/**
 * Client task: delete a vegetable-price entity from the vegetable-price table.
 */
public class DeleteVegetablePrice implements Task {
    private static final long serialVersionUID = 1L;

    private final String vegetableName;

    public DeleteVegetablePrice(String vegetableName) {
        this.vegetableName = vegetableName;
    }

    @Override
    public Object execute(VegetablePriceTable table) {
        if (vegetableName == null || vegetableName.trim().isEmpty()) {
            return "ERROR: Vegetable name cannot be empty.";
        }
        boolean removed = table.delete(vegetableName);
        if (removed) {
            return "OK: Deleted vegetable '" + vegetableName + "'.";
        }
        return "ERROR: Vegetable '" + vegetableName + "' not found.";
    }
}
