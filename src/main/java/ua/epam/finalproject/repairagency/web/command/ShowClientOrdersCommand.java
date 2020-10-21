package ua.epam.finalproject.repairagency.web.command;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.model.Client;
import ua.epam.finalproject.repairagency.model.Order;
import ua.epam.finalproject.repairagency.repository.ConnectionPool;
import ua.epam.finalproject.repairagency.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class ShowClientOrdersCommand extends ActionCommand {

    private OrderService orderService;
    private static final Logger Log = Logger.getLogger(ShowClientOrdersCommand.class);

    public ShowClientOrdersCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Log.trace("Command start");

        HttpSession session = request.getSession();
        Client client = (Client) session.getAttribute("user");
        ConnectionPool connectionPool = (ConnectionPool) session.getAttribute("connectionPool");

        List<Order> clientOrders = orderService.findForClient(client, connectionPool);

        session.setAttribute("clientOrders", clientOrders);

        Log.trace("Command finishes");
        return "controller?p=authorizedPage&tab=clientOrders";
    }
}
