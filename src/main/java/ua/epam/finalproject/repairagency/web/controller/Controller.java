package ua.epam.finalproject.repairagency.web.controller;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.web.command.ActionCommand;
import ua.epam.finalproject.repairagency.web.command.CommandContainer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * Main servlet controller
 *
 * @author Soros
 * 27/09/2020
 */
public class Controller extends HttpServlet {

    private static final Logger Log = Logger.getLogger(Controller.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8"); // may be in listener, may be not
        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        // todo команд в запросе быть не должно

        Log.debug("Controller starts " + request.getMethod());

        String forward = request.getParameter("p");
        if(forward == null) {
            forward = "index.jsp";
        } else {
            switch (forward) {
                case "clientMain" :
                case "create" :
                    forward = "WEB-INF/authorizedPages/clientMain.jsp";
                    break;
            }
        }

        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
                        throws IOException, ServletException
    {
        Log.debug("Controller starts " + request.getMethod());
        // extract command name from the request

        String commandName = request.getParameter("command");
        Log.trace("Request parameter: command --> " + commandName);

        // obtain command object by its name
        ActionCommand command = CommandContainer.get(commandName);
        Log.trace("Obtained command --> " + command);

        Log.debug("go to the command.execute");
        // execute command and get forward address
        String forward = command.execute(request, response);

        Log.trace("Forward address --> " + forward);

        Log.debug("Controller finished, now go to forward address --> " + forward);

        // if the forward address is not null go to the address
        if(forward != null) {
            Log.trace("forwarding to " + forward);
            request.getRequestDispatcher(forward).forward(request, response);
        }
    }
}
