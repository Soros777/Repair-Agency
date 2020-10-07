package ua.epam.finalproject.repairagency.web.command;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.service.ClientService;
import ua.epam.finalproject.repairagency.to.ClientTo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginCommand extends ActionCommand {

    private static final String PARAM_NAME_EMAIL = "email";
    private static final String PARAM_NAME_PASSWORD = "password";
    private static final Logger Log = Logger.getLogger(LoginCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        Log.debug("Command starts");

        String email = request.getParameter(PARAM_NAME_EMAIL);
        String password = request.getParameter(PARAM_NAME_PASSWORD);

        ClientTo clientTo = null;
        try {
            clientTo = ClientService.findClient(email, password);
            Log.debug("Found in DB: clientTo --> " + clientTo);
        } catch (AppException e) {
            // todo go to error page
        }
        if(clientTo != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", clientTo);
            Log.trace("Set session attribute \"user\" : " + clientTo);
            session.setAttribute("role", "client");
            Log.trace("Set session attribute \"role\" : client");
            session.setAttribute("userName", clientTo.getClientName());
            Log.trace("Set session attribute \"userName\" : " + clientTo.getClientName());
            response.getWriter().write(clientTo.getClientName()); // for javascript
        } else {
            response.getWriter().write("Something wrong"); // for javascript
        }
        Log.debug("Command finished");
        return null;
    }
}
