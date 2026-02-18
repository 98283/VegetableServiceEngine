package rmi.tasks;

import rmi.Task;
import rmi.VegetablePriceTable;

/**
 * Client task: update a vegetable-price entity in the vegetable-price table.
 */
public class UpdateVegetablePrice implements Task {
    private static final long serialVersionUID = 1L;

    private final String vegetableName;
    private final double pricePerUnit;

    public UpdateVegetablePrice(String vegetableName, double pricePerUnit) {
        this.vegetableName = vegetableName;
        this.pricePerUnit = pricePerUnit;
    }

    @Override
    public Object execute(VegetablePriceTable table) {
        if (vegetableName == null || vegetableName.trim().isEmpty()) {
            return "ERROR: Vegetable name cannot be empty.";
        }
        if (pricePerUnit < 0) {
            return "ERROR: Price cannot be negative.";
        }
        boolean updated = table.update(vegetableName, pricePerUnit);
        if (updated) {
            return "OK: Updated vegetable '" + vegetableName + "' to price " + pricePerUnit;
        }
        return "ERROR: Vegetable '" + vegetableName + "' not found.";
    }
}
