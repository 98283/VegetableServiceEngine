package servlets;

import rmi.VegetableComputeTaskRegistry;
import rmi.tasks.CalVegetableCost;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/calcost")
public class CalVegetableCostServlet extends HttpServlet {
    private final VegetableComputeTaskRegistry registry = new VegetableComputeTaskRegistry();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain; charset=UTF-8");
        String name = req.getParameter("vegetableName");
        String qtyStr = req.getParameter("quantity");
        double quantity = 0;
        try {
            if (qtyStr != null && !qtyStr.trim().isEmpty()) {
                quantity = Double.parseDouble(qtyStr.trim());
            }
        } catch (NumberFormatException e) {
            sendResponse(resp, "ERROR: Invalid quantity.");
            return;
        }
        Object result = registry.runTask(new CalVegetableCost(name != null ? name : "", quantity));
        sendResponse(resp, result.toString());
    }

    private void sendResponse(HttpServletResponse resp, String message) throws IOException {
        try (PrintWriter out = resp.getWriter()) {
            out.print(message);
        }
    }
}
