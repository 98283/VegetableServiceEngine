package server;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import rmi.VegetableComputeTaskRegistry;
import rmi.tasks.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple HTTP server using Java's built-in HttpServer (no external dependencies).
 * Run this after starting RMIServer.
 */
public class SimpleWebServer {
    private static final int PORT = 8080;
    private static final VegetableComputeTaskRegistry registry = new VegetableComputeTaskRegistry();

    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
            
            // Add servlet endpoints
            server.createContext("/vegetable-service-engine/add", new AddHandler());
            server.createContext("/vegetable-service-engine/update", new UpdateHandler());
            server.createContext("/vegetable-service-engine/delete", new DeleteHandler());
            server.createContext("/vegetable-service-engine/calcost", new CalCostHandler());
            server.createContext("/vegetable-service-engine/receipt", new ReceiptHandler());
            
            server.setExecutor(null);
            server.start();
            System.out.println("========================================");
            System.out.println("Web Server started on port " + PORT);
            System.out.println("Servlets available at:");
            System.out.println("  http://localhost:" + PORT + "/vegetable-service-engine/add");
            System.out.println("  http://localhost:" + PORT + "/vegetable-service-engine/update");
            System.out.println("  http://localhost:" + PORT + "/vegetable-service-engine/delete");
            System.out.println("  http://localhost:" + PORT + "/vegetable-service-engine/calcost");
            System.out.println("  http://localhost:" + PORT + "/vegetable-service-engine/receipt");
            System.out.println("========================================");
            System.out.println("Press Ctrl+C to stop");
        } catch (IOException e) {
            System.err.println("Failed to start web server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Map<String, String> parseQuery(String query) {
        Map<String, String> params = new HashMap<>();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=", 2);
                if (keyValue.length == 2) {
                    try {
                        params.put(
                            URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8),
                            URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8)
                        );
                    } catch (Exception e) {
                        // Ignore malformed params
                    }
                }
            }
        }
        return params;
    }

    private static void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
        exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }

    static class AddHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> params = parseQuery(query);
            String name = params.getOrDefault("vegetableName", "");
            double price = 0;
            try {
                if (params.containsKey("price")) {
                    price = Double.parseDouble(params.get("price"));
                }
            } catch (NumberFormatException e) {
                sendResponse(exchange, "ERROR: Invalid price.");
                return;
            }
            Object result = registry.runTask(new AddVegetablePrice(name, price));
            sendResponse(exchange, result.toString());
        }
    }

    static class UpdateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> params = parseQuery(query);
            String name = params.getOrDefault("vegetableName", "");
            double price = 0;
            try {
                if (params.containsKey("price")) {
                    price = Double.parseDouble(params.get("price"));
                }
            } catch (NumberFormatException e) {
                sendResponse(exchange, "ERROR: Invalid price.");
                return;
            }
            Object result = registry.runTask(new UpdateVegetablePrice(name, price));
            sendResponse(exchange, result.toString());
        }
    }

    static class DeleteHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> params = parseQuery(query);
            String name = params.getOrDefault("vegetableName", "");
            Object result = registry.runTask(new DeleteVegetablePrice(name));
            sendResponse(exchange, result.toString());
        }
    }

    static class CalCostHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> params = parseQuery(query);
            String name = params.getOrDefault("vegetableName", "");
            double quantity = 0;
            try {
                if (params.containsKey("quantity")) {
                    quantity = Double.parseDouble(params.get("quantity"));
                }
            } catch (NumberFormatException e) {
                sendResponse(exchange, "ERROR: Invalid quantity.");
                return;
            }
            Object result = registry.runTask(new CalVegetableCost(name, quantity));
            sendResponse(exchange, result.toString());
        }
    }

    static class ReceiptHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> params = parseQuery(query);
            double totalCost = 0;
            double amountGiven = 0;
            try {
                if (params.containsKey("totalCost")) {
                    totalCost = Double.parseDouble(params.get("totalCost"));
                }
                if (params.containsKey("amountGiven")) {
                    amountGiven = Double.parseDouble(params.get("amountGiven"));
                }
            } catch (NumberFormatException e) {
                sendResponse(exchange, "ERROR: Invalid totalCost or amountGiven.");
                return;
            }
            String cashier = params.getOrDefault("cashierName", "");
            Object result = registry.runTask(new CalculateCost(totalCost, amountGiven, cashier));
            sendResponse(exchange, result.toString());
        }
    }
}
