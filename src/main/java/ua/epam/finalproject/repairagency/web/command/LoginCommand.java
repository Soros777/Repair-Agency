package ua.epam.finalproject.repairagency.web.command;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.Path;
import ua.epam.finalproject.repairagency.model.Client;
import ua.epam.finalproject.repairagency.service.ClientService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginCommand extends Command {

    private static final Logger Log = Logger.getLogger(LoginCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        Log.debug("Command starts");

        // get Client from DB by email
        Client client = ClientService.findClient(request.getParameter("email"));
        Log.trace("Found in DB: client --> " + client);

        String forward = Path.PAGE_ERROR_LOGIN;

        if(client != null) {
            HttpSession session = request.getSession();
            session.setAttribute("client", client);
            forward = Path.PAGE_SUCCESS_LOGIN;
        }

        return forward;
    }
}
