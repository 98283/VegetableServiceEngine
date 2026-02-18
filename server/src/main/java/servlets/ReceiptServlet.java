package servlets;

import rmi.VegetableComputeTaskRegistry;
import rmi.tasks.CalculateCost;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/receipt")
public class ReceiptServlet extends HttpServlet {
    private final VegetableComputeTaskRegistry registry = new VegetableComputeTaskRegistry();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain; charset=UTF-8");
        String totalStr = req.getParameter("totalCost");
        String amountStr = req.getParameter("amountGiven");
        String cashier = req.getParameter("cashierName");
        double totalCost = 0;
        double amountGiven = 0;
        try {
            if (totalStr != null && !totalStr.trim().isEmpty()) {
                totalCost = Double.parseDouble(totalStr.trim());
            }
            if (amountStr != null && !amountStr.trim().isEmpty()) {
                amountGiven = Double.parseDouble(amountStr.trim());
            }
        } catch (NumberFormatException e) {
            sendResponse(resp, "ERROR: Invalid totalCost or amountGiven.");
            return;
        }
        Object result = registry.runTask(new CalculateCost(totalCost, amountGiven, cashier != null ? cashier : ""));
        sendResponse(resp, result.toString());
    }

    private void sendResponse(HttpServletResponse resp, String message) throws IOException {
        try (PrintWriter out = resp.getWriter()) {
            out.print(message);
        }
    }
}
