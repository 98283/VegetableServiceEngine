package rmi.tasks;

import rmi.Task;
import rmi.VegetablePriceTable;

/**
 * Client task: add a new vegetable-price entity to the vegetable-price table.
 */
public class AddVegetablePrice implements Task {
    private static final long serialVersionUID = 1L;

    private final String vegetableName;
    private final double pricePerUnit;

    public AddVegetablePrice(String vegetableName, double pricePerUnit) {
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
        table.add(vegetableName, pricePerUnit);
        return "OK: Added vegetable '" + vegetableName + "' with price " + pricePerUnit;
    }
}
