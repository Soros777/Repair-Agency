package ua.epam.finalproject.repairagency.web.command;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.model.Client;
import ua.epam.finalproject.repairagency.service.HashPassword;
import ua.epam.finalproject.repairagency.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class RegisterCommand extends ActionCommand {

    private static final Logger Log = Logger.getLogger(RegisterCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        Log.debug("Command starts");

        String clientEmail = request.getParameter("registerEmail");
        String clientPassword = request.getParameter("registerPassword");
        String clientPasswordRepeat = request.getParameter("registerPasswordRepeat");
        String clientName = request.getParameter("registerClientName");
        Log.debug("Gotten params ==> ClientName: " + clientName + "; email: " + clientEmail);

        if(!UserService.validateEnteredData(clientName, clientEmail, clientPassword, clientPasswordRepeat, response)) {
           return null;
        }

        Log.debug("go to add a new client to DB");
        boolean added = UserService.addNewClient(clientEmail, clientPassword, clientName, request.getLocale());
        Log.debug("new client is added to DB");
        if(added) {
            try {
                response.getWriter().write("success registration");
            } catch (IOException e) {
                Log.error("Can't get request writer");
                throw new AppException("Can't get request writer", e);
            }
            Log.debug("Well done! Success registration!");
            return null;
        }
        Log.debug("Can't register a new user");
        throw new AppException("Can't register a new user");
    }
}
