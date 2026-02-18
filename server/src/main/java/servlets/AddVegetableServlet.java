package servlets;

import rmi.VegetableComputeTaskRegistry;
import rmi.tasks.AddVegetablePrice;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/add")
public class AddVegetableServlet extends HttpServlet {
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
        String priceStr = req.getParameter("price");
        double price = 0;
        try {
            if (priceStr != null && !priceStr.trim().isEmpty()) {
                price = Double.parseDouble(priceStr.trim());
            }
        } catch (NumberFormatException e) {
            sendResponse(resp, "ERROR: Invalid price.");
            return;
        }
        Object result = registry.runTask(new AddVegetablePrice(name != null ? name : "", price));
        sendResponse(resp, result.toString());
    }

    private void sendResponse(HttpServletResponse resp, String message) throws IOException {
        try (PrintWriter out = resp.getWriter()) {
            out.print(message);
        }
    }
}
