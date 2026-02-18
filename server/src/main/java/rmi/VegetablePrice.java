package rmi;

import java.io.Serializable;

/**
 * Entity representing a vegetable and its price in the vegetable-price table.
 */
public class VegetablePrice implements Serializable {
    private static final long serialVersionUID = 1L;

    private String vegetableName;
    private double pricePerUnit;

    public VegetablePrice() {
    }

    public VegetablePrice(String vegetableName, double pricePerUnit) {
        this.vegetableName = vegetableName;
        this.pricePerUnit = pricePerUnit;
    }

    public String getVegetableName() {
        return vegetableName;
    }

    public void setVegetableName(String vegetableName) {
        this.vegetableName = vegetableName;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    @Override
    public String toString() {
        return "VegetablePrice{name='" + vegetableName + "', price=" + pricePerUnit + "}";
    }
}
