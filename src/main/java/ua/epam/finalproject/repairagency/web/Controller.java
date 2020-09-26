package ua.epam.finalproject.repairagency.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.epam.finalproject.repairagency.web.command.Command;
import ua.epam.finalproject.repairagency.web.command.CommandContainer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Controller {

    private static final Logger Log = LoggerFactory.getLogger(Controller.class);

    /**
     * Main method of this controller
     */
    private void process(HttpServletRequest request, HttpServletResponse response)
                        throws IOException, ServletException
    {
        // extract command name from the request
        String commandName = request.getParameter("command");

        // obtain command object by its name
        Command command = CommandContainer.get(commandName);

        // execute command and get forward address
        String forward = command.execute(request, response);

        // if the forward address is not null go to the address
        if(forward != null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher(forward);
            dispatcher.forward(request, response);
        }
    }
}
