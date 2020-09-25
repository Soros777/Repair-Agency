package ua.epam.finalproject.repairagency.servlets;

import ua.epam.finalproject.repairagency.model.Client;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

public class AddClient extends Dispatcher {

    public String getServletInfo() {
        return "Add client servlet";
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Util.showInputParameters(request, response, "doGet");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext ctx = getServletContext();
        if(request.getParameter("save") != null) {
            String clientEmail = request.getParameter("email");
            String clientPassword = request.getParameter("password");
            String clientName = request.getParameter("client");
            Client newClient = new Client();
            newClient.setEmail(clientEmail);
            newClient.setPassword(clientPassword);
            newClient.setClientName(clientName);
            ctx.setAttribute("client", newClient);
            boolean res = ClientList.addClient(newClient);
            if(res) {
                this.forward("/successRegistration.jsp", request, response);
            } else {
                this.forward("/errorRegistration.html", request, response);
            }
        }
    }
}
