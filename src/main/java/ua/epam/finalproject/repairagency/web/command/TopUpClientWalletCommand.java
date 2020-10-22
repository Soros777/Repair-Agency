package ua.epam.finalproject.repairagency.web.command;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.repository.ConnectionPool;
import ua.epam.finalproject.repairagency.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class TopUpClientWalletCommand extends ActionCommand {

    private UserService userService;
    private static final Logger Log = Logger.getLogger(TopUpClientWalletCommand.class);

    public TopUpClientWalletCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Log.trace("Command starts");

        HttpSession session = request.getSession();
        String clientId = request.getParameter("clientId");
        String amount = request.getParameter("amount");
        ConnectionPool connectionPool = (ConnectionPool) session.getAttribute("connectionPool");
        Log.debug("Params are : clientId : " + clientId + "; amount : " + amount);

        userService.totUpWallet(clientId, amount, connectionPool);

        Log.trace("Command finishes");
        return "controller?p=authorizedPage&tab=successTopUpWallet&clientId=" + clientId;
    }
}
