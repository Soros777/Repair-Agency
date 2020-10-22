package ua.epam.finalproject.repairagency.web.command;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.model.Order;
import ua.epam.finalproject.repairagency.repository.ConnectionPool;
import ua.epam.finalproject.repairagency.service.OrderService;
import ua.epam.finalproject.repairagency.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class ShowMasterOrdersCommand extends ActionCommand {

    private OrderService orderService;
    private UserService userService;
    private static final Logger Log = Logger.getLogger(ShowMasterOrdersCommand.class);

    public ShowMasterOrdersCommand(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Log.trace("Command start");

        HttpSession session = request.getSession();
        String masterId = request.getParameter("masterId");
        ConnectionPool connectionPool = (ConnectionPool) session.getAttribute("connectionPool");
        Log.trace("Param masterId : " + masterId);


        List<Order> orders = orderService.findForMaster(masterId, connectionPool);

        session.setAttribute("orders", orders);

        Log.trace("Command finishes");
        return "controller?p=authorizedPage&tab=orders";
    }
}
