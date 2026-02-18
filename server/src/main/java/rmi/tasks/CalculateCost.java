package rmi.tasks;

import rmi.Task;
import rmi.VegetablePriceTable;

/**
 * Client task: create a receipt for the whole transaction (cost, amount given, change, cashier).
 */
public class CalculateCost implements Task {
    private static final long serialVersionUID = 1L;

    private final double totalCost;
    private final double amountGiven;
    private final String cashierName;

    public CalculateCost(double totalCost, double amountGiven, String cashierName) {
        this.totalCost = totalCost;
        this.amountGiven = amountGiven;
        this.cashierName = cashierName == null ? "" : cashierName;
    }

    @Override
    public Object execute(VegetablePriceTable table) {
        if (amountGiven < totalCost) {
            return "ERROR: Amount given (" + amountGiven + ") is less than total cost (" + totalCost + ").";
        }
        double change = amountGiven - totalCost;
        StringBuilder receipt = new StringBuilder();
        receipt.append("========== VEGETABLE SERVICE ENGINE - RECEIPT ==========\n");
        receipt.append("Cashier: ").append(cashierName.isEmpty() ? "(not specified)" : cashierName).append("\n");
        receipt.append("--------------------------------------------------------\n");
        receipt.append(String.format("Total Cost:     %.2f%n", totalCost));
        receipt.append(String.format("Amount Given:   %.2f%n", amountGiven));
        receipt.append(String.format("Change Due:     %.2f%n", change));
        receipt.append("========================================================");
        return receipt.toString();
    }
}
