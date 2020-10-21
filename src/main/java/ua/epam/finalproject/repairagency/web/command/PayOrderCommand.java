package ua.epam.finalproject.repairagency.web.command;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.model.Client;
import ua.epam.finalproject.repairagency.model.Order;
import ua.epam.finalproject.repairagency.repository.ConnectionPool;
import ua.epam.finalproject.repairagency.service.OrderService;
import ua.epam.finalproject.repairagency.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class PayOrderCommand extends ActionCommand {

    private OrderService orderService;
    private UserService userService;
    private static final Logger Log = Logger.getLogger(PayOrderCommand.class);

    public PayOrderCommand(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Log.trace("Command starts execute");

        String orderId = request.getParameter("orderId");
        HttpSession session = request.getSession();
        Client client = (Client) session.getAttribute("user");
        List<Order> orders = (List<Order>) session.getAttribute("orders");
        ConnectionPool connectionPool = (ConnectionPool) session.getAttribute("connectionPool");

        if(!userService.payOrder(client, orderId, orders, connectionPool, orderService)) {
            return "controller?p=authorizedPage&tab=notEnoughMoney";
        }

        List<Order> clientOrders = orderService.findForClient(client, connectionPool);

        session.setAttribute("clientOrders", clientOrders);

        Log.trace("Command finishes");
        return "controller?p=authorizedPage&tab=clientOrders";
    }
}
