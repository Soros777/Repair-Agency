package ua.epam.finalproject.repairagency.web.command;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.model.Order;
import ua.epam.finalproject.repairagency.repository.ConnectionPool;
import ua.epam.finalproject.repairagency.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class ShowClientOrderCommand extends ActionCommand {

    private OrderService orderService;
    private static final Logger Log = Logger.getLogger(ShowClientOrderCommand.class);

    public ShowClientOrderCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Log.trace("Command start");

        HttpSession session = request.getSession();
        String orderId = request.getParameter("orderId");
        ConnectionPool connectionPool = (ConnectionPool) session.getAttribute("connectionPool");
        Log.debug("Param is : orderId : " + orderId);

        Order requestedOder = orderService.findById(connectionPool, orderId);

        session.setAttribute("order", requestedOder);

        Log.trace("Command finishes");
        return "controller?p=authorizedPage&tab=order";
    }
}
