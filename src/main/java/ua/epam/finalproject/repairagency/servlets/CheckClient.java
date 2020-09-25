package ua.epam.finalproject.repairagency.servlets;

import ua.epam.finalproject.repairagency.model.Client;
import ua.epam.finalproject.repairagency.services.ClientService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CheckClient extends Dispatcher {

    @Override
    public String getServletInfo() {
        return "Registration servlet";
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        Client client = ClientService.findClient(request.getParameter("email"));
        if(client == null) {
            this.forward("/index.html", request, response);
        } else {
            if(!client.getPassword().equals(request.getParameter("password"))) {
                this.forward("/index.html", request, response);
            } else {
                this.forward("/successLogin.jsp", request, response);
            }
        }
    }
}
