package com.vegetable.service.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * HTTP client for Vegetable Service Engine servlets.
 * Use BASE_URL = "http://10.0.2.2:8080/vegetable-service-engine" for Android emulator (localhost).
 * Use your machine's IP for a physical device (e.g. "http://192.168.1.x:8080/vegetable-service-engine").
 */
public class ApiClient {

    // Android emulator: 10.0.2.2 is the host machine's loopback
    public static final String BASE_URL = "http://10.0.2.2:8080/vegetable-service-engine";

    public static String get(String path, Map<String, String> params) throws IOException {
        return request("GET", path, params);
    }

    public static String post(String path, Map<String, String> params) throws IOException {
        return request("POST", path, params);
    }

    private static String request(String method, String path, Map<String, String> params) throws IOException {
        String query = buildQuery(params);
        String fullPath = path;
        if (query != null && !query.isEmpty()) {
            fullPath = path + "?" + query;
        }
        URL url = new URL(BASE_URL + fullPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        conn.setDoInput(true);
        if ("POST".equals(method)) {
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            if (query != null && !query.isEmpty()) {
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(query.getBytes(StandardCharsets.UTF_8));
                }
            }
        }
        int code = conn.getResponseCode();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(code >= 200 && code < 300 ? conn.getInputStream() : conn.getErrorStream(), StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        conn.disconnect();
        return sb.toString().trim();
    }

    private static String buildQuery(Map<String, String> params) {
        if (params == null || params.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> e : params.entrySet()) {
            if (sb.length() > 0) sb.append("&");
            sb.append(encode(e.getKey())).append("=").append(encode(e.getValue()));
        }
        return sb.toString();
    }

    private static String encode(String s) {
        if (s == null) return "";
        try {
            return java.net.URLEncoder.encode(s, "UTF-8");
        } catch (Exception e) {
            return s;
        }
    }
}
