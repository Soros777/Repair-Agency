package ua.epam.finalproject.repairagency.web.command;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.service.ClientService;
import ua.epam.finalproject.repairagency.service.HashPassword;
import ua.epam.finalproject.repairagency.to.ClientTo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class LoginCommand extends ActionCommand {

    private static final String PARAM_NAME_EMAIL = "email";
    private static final String PARAM_NAME_PASSWORD = "password";
    private static final Logger Log = Logger.getLogger(LoginCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        Log.debug("Command starts");

        String email = request.getParameter(PARAM_NAME_EMAIL);
        String password = request.getParameter(PARAM_NAME_PASSWORD);

        ClientTo clientTo;
        try {
            password = HashPassword.getHash(password);
            clientTo = ClientService.findClient(email, password);
            Log.debug("Found in DB: clientTo --> " + clientTo);
            if(clientTo != null) {
                ClientService.setSessionAttributes(request, clientTo);
                response.getWriter().write(clientTo.getClientName()); // for javascript
            } else {
                response.getWriter().write("Something wrong"); // for javascript
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            Log.error(e);
            throw new AppException(e.getMessage());
        }

        Log.debug("Command finished");
        return null;
    }
}
