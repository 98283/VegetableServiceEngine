package rmi;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory vegetable-price table used by the compute engine.
 * Thread-safe for concurrent servlet requests.
 */
public class VegetablePriceTable implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<String, Double> table = new ConcurrentHashMap<>();

    public void add(String vegetableName, double pricePerUnit) {
        table.put(vegetableName == null ? "" : vegetableName.trim().toLowerCase(), pricePerUnit);
    }

    public boolean update(String vegetableName, double pricePerUnit) {
        String key = vegetableName == null ? "" : vegetableName.trim().toLowerCase();
        if (table.containsKey(key)) {
            table.put(key, pricePerUnit);
            return true;
        }
        return false;
    }

    public boolean delete(String vegetableName) {
        return table.remove(vegetableName == null ? "" : vegetableName.trim().toLowerCase()) != null;
    }

    public Double getPrice(String vegetableName) {
        return table.get(vegetableName == null ? "" : vegetableName.trim().toLowerCase());
    }

    public boolean contains(String vegetableName) {
        return table.containsKey(vegetableName == null ? "" : vegetableName.trim().toLowerCase());
    }

    public Map<String, Double> getAll() {
        return new ConcurrentHashMap<>(table);
    }
}
