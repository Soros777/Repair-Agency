package ua.epam.finalproject.repairagency.web.command;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.model.User;
import ua.epam.finalproject.repairagency.repository.ConnectionPool;
import ua.epam.finalproject.repairagency.repository.EntityContainer;
import ua.epam.finalproject.repairagency.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

public class BanClientCommand extends ActionCommand {

    private UserService userService;
    private static final Logger Log = Logger.getLogger(BanClientCommand.class);

    public BanClientCommand(UserService userService) {
        this.userService = userService;
    }
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Log.trace("Start ban / unban client");

        HttpSession session = request.getSession();
        String clientId = request.getParameter("clientId");
        ConnectionPool connectionPool = (ConnectionPool) session.getAttribute("connectionPool");
        Log.debug("Param clientId : " + clientId);

        User client = userService.changeStatus(clientId, connectionPool);

        try {
            EntityContainer.fillContainers(connectionPool.getConnection());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Log.trace("Command finishes");
        return "controller?p=authorizedPage&tab=successChangeStatus&clientId=" + clientId;
    }
}
