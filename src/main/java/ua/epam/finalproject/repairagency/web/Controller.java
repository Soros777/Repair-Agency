package ua.epam.finalproject.repairagency.web;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.web.command.Command;
import ua.epam.finalproject.repairagency.web.command.CommandContainer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Main servlet controller
 *
 * @author Soros
 * 27/09/2020
 */
public class Controller extends HttpServlet {

    private static final Logger Log = Logger.getLogger(Controller.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
                        throws IOException, ServletException
    {
        Log.debug("Controller starts");

        // extract command name from the request
        String commandName = request.getParameter("command");
        Log.trace("Request parameter: command --> " + commandName);

        // obtain command object by its name
        Command command = CommandContainer.get(commandName);
        Log.trace("Obtained command --> " + command);

        // execute command and get forward address
        String forward = command.execute(request, response);
        Log.trace("Forward address --> " + forward);

        Log.debug("Controller finished, now go to forward address --> ... ");

        // if the forward address is not null go to the address
        if(forward != null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher(forward);
            dispatcher.forward(request, response);
        }
    }
}
