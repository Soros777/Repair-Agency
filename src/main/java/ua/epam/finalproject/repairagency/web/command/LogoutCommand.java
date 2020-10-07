package ua.epam.finalproject.repairagency.web.command;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.resource.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutCommand extends ActionCommand{
    private static final Logger Log = Logger.getLogger(LogoutCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws AppException {
//        String page = ConfigurationManager.getProperty("path.page.index");
        String page = "/index.jsp";
        // destroy session
        HttpSession session = request.getSession();
        String id = session.getId();

        session.invalidate();
        Log.debug("invalidated session with id : " + id);

        return page;
    }
}
