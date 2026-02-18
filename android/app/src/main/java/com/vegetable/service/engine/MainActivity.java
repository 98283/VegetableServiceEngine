package com.vegetable.service.engine;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private EditText addName, addPrice;
    private EditText updateName, updatePrice;
    private EditText deleteName;
    private EditText costName, costQty;
    private EditText receiptTotal, receiptAmount, receiptCashier;

    private TextView resultAdd, resultUpdate, resultDelete, resultCost, resultReceipt;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addName = findViewById(R.id.add_vegetable_name);
        addPrice = findViewById(R.id.add_price);
        updateName = findViewById(R.id.update_vegetable_name);
        updatePrice = findViewById(R.id.update_price);
        deleteName = findViewById(R.id.delete_vegetable_name);
        costName = findViewById(R.id.cost_vegetable_name);
        costQty = findViewById(R.id.cost_quantity);
        receiptTotal = findViewById(R.id.receipt_total_cost);
        receiptAmount = findViewById(R.id.receipt_amount_given);
        receiptCashier = findViewById(R.id.receipt_cashier);

        resultAdd = findViewById(R.id.result_add);
        resultUpdate = findViewById(R.id.result_update);
        resultDelete = findViewById(R.id.result_delete);
        resultCost = findViewById(R.id.result_cost);
        resultReceipt = findViewById(R.id.result_receipt);

        findViewById(R.id.btn_add).setOnClickListener(v -> doAdd());
        findViewById(R.id.btn_update).setOnClickListener(v -> doUpdate());
        findViewById(R.id.btn_delete).setOnClickListener(v -> doDelete());
        findViewById(R.id.btn_calc_cost).setOnClickListener(v -> doCalcCost());
        findViewById(R.id.btn_receipt).setOnClickListener(v -> doReceipt());
    }

    @Override
    protected void onDestroy() {
        executor.shutdown();
        super.onDestroy();
    }

    private void doAdd() {
        String name = addName.getText().toString().trim();
        String priceStr = addPrice.getText().toString().trim();
        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Enter vegetable name and price", Toast.LENGTH_SHORT).show();
            return;
        }
        resultAdd.setText("...");
        Map<String, String> params = new HashMap<>();
        params.put("vegetableName", name);
        params.put("price", priceStr);
        runRequest("/add", params, resultAdd);
    }

    private void doUpdate() {
        String name = updateName.getText().toString().trim();
        String priceStr = updatePrice.getText().toString().trim();
        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Enter vegetable name and price", Toast.LENGTH_SHORT).show();
            return;
        }
        resultUpdate.setText("...");
        Map<String, String> params = new HashMap<>();
        params.put("vegetableName", name);
        params.put("price", priceStr);
        runRequest("/update", params, resultUpdate);
    }

    private void doDelete() {
        String name = deleteName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Enter vegetable name", Toast.LENGTH_SHORT).show();
            return;
        }
        resultDelete.setText("...");
        Map<String, String> params = new HashMap<>();
        params.put("vegetableName", name);
        runRequest("/delete", params, resultDelete);
    }

    private void doCalcCost() {
        String name = costName.getText().toString().trim();
        String qtyStr = costQty.getText().toString().trim();
        if (name.isEmpty() || qtyStr.isEmpty()) {
            Toast.makeText(this, "Enter vegetable name and quantity", Toast.LENGTH_SHORT).show();
            return;
        }
        resultCost.setText("...");
        Map<String, String> params = new HashMap<>();
        params.put("vegetableName", name);
        params.put("quantity", qtyStr);
        runRequest("/calcost", params, resultCost);
    }

    private void doReceipt() {
        String totalStr = receiptTotal.getText().toString().trim();
        String amountStr = receiptAmount.getText().toString().trim();
        String cashier = receiptCashier.getText().toString().trim();
        if (totalStr.isEmpty() || amountStr.isEmpty()) {
            Toast.makeText(this, "Enter total cost and amount given", Toast.LENGTH_SHORT).show();
            return;
        }
        resultReceipt.setText("...");
        Map<String, String> params = new HashMap<>();
        params.put("totalCost", totalStr);
        params.put("amountGiven", amountStr);
        params.put("cashierName", cashier);
        runRequest("/receipt", params, resultReceipt);
    }

    private void runRequest(String path, Map<String, String> params, TextView resultView) {
        executor.execute(() -> {
            try {
                String response = ApiClient.post(path, params);
                mainHandler.post(() -> resultView.setText(response));
            } catch (IOException e) {
                mainHandler.post(() -> {
                    resultView.setText("Error: " + e.getMessage());
                    Toast.makeText(MainActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
