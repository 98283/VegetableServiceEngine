package server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.*;

/**
 * Simple embedded web server using Jetty to run the servlets.
 * Run this after starting RMIServer.
 */
public class WebServer {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try {
            Server server = new Server(PORT);
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/vegetable-service-engine");
            server.setHandler(context);

            // Register all servlets
            context.addServlet(new ServletHolder(new AddVegetableServlet()), "/add");
            context.addServlet(new ServletHolder(new UpdateVegetableServlet()), "/update");
            context.addServlet(new ServletHolder(new DeleteVegetableServlet()), "/delete");
            context.addServlet(new ServletHolder(new CalVegetableCostServlet()), "/calcost");
            context.addServlet(new ServletHolder(new ReceiptServlet()), "/receipt");

            server.start();
            System.out.println("Web server started on port " + PORT);
            System.out.println("Servlets available at: http://localhost:" + PORT + "/vegetable-service-engine");
            System.out.println("Press Ctrl+C to stop");
            server.join();
        } catch (Exception e) {
            System.err.println("Failed to start web server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
