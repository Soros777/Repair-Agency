package ua.epam.finalproject.repairagency.web.command;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.Path;
import ua.epam.finalproject.repairagency.model.Client;
import ua.epam.finalproject.repairagency.service.ClientService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterCommand extends ActionCommand {

    private static final Logger Log = Logger.getLogger(RegisterCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        Log.debug("Command starts");

        String clientEmail = request.getParameter("email");
        String clientPassword = request.getParameter("password");
        String clientName = request.getParameter("client");

        Client newClient = new Client();
        newClient.setEmail(clientEmail);
        newClient.setPassword(clientPassword);
        newClient.setClientName(clientName);

        // put new Client to DB
        if(ClientService.addClient(newClient)) {
            // put new Client to the request
            request.setAttribute("client", newClient);
            Log.trace("Set the request attribute: client --> " + newClient);

            Log.debug("Command finished");
            return Path.PAGE_SUCCESS_REGISTRATION;
        }

        return Path.PAGE_ERROR_REGISTRATION;
    }
}
