package rmi.tasks;

import rmi.Task;
import rmi.VegetablePriceTable;

/**
 * Client task: query vegetable price and calculate vegetable cost for a given quantity.
 */
public class CalVegetableCost implements Task {
    private static final long serialVersionUID = 1L;

    private final String vegetableName;
    private final double quantity;

    public CalVegetableCost(String vegetableName, double quantity) {
        this.vegetableName = vegetableName;
        this.quantity = quantity;
    }

    @Override
    public Object execute(VegetablePriceTable table) {
        if (vegetableName == null || vegetableName.trim().isEmpty()) {
            return "ERROR: Vegetable name cannot be empty.";
        }
        if (quantity <= 0) {
            return "ERROR: Quantity must be positive.";
        }
        Double price = table.getPrice(vegetableName);
        if (price == null) {
            return "ERROR: Vegetable '" + vegetableName + "' not found.";
        }
        double cost = price * quantity;
        return String.format("OK: %.2f x %.2f = %.2f", price, quantity, cost);
    }
}
