package ua.epam.finalproject.repairagency.web.command;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.Path;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.model.Client;
import ua.epam.finalproject.repairagency.service.ClientService;
import ua.epam.finalproject.repairagency.service.HashPassword;
import ua.epam.finalproject.repairagency.to.ClientTo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;

public class RegisterCommand extends ActionCommand {

    private static final Logger Log = Logger.getLogger(RegisterCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        Log.debug("Command starts");

        String clientEmail = request.getParameter("email");
        String clientPassword = request.getParameter("password");
        String clientName = request.getParameter("clientName");

        Log.debug("Gotten params ==> ClientName: " + clientName + "; email: " + clientEmail);

        Client newClient = new Client();
        try {
            newClient.setPassword(HashPassword.getHash(clientPassword));
        } catch (NoSuchAlgorithmException e) {
            Log.error(e);
            throw new AppException(e.getMessage());
        }
        newClient.setEmail(clientEmail);
        newClient.setClientName(clientName);

        Log.debug("Start to put new Client to DB");

        if(ClientService.addClient(newClient)) {
            Log.debug("New Client is in DB");
            // obtain clientTo
            ClientTo clientTo = ClientTo.getFromBean(newClient);

            // put new Client to the session
            ClientService.setSessionAttributes(request, clientTo);

            Log.debug("Command are about to finish with forwardPage " + Path.PAGE_SUCCESS_REGISTRATION);
            return Path.PAGE_SUCCESS_REGISTRATION;
        }
        Log.debug("Command are about to finish with forwardPage " + Path.PAGE_ERROR_REGISTRATION);
        return Path.PAGE_ERROR_REGISTRATION;
    }
}
