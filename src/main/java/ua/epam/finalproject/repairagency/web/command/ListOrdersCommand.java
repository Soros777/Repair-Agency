package ua.epam.finalproject.repairagency.web.command;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.model.Order;
import ua.epam.finalproject.repairagency.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class ListOrdersCommand extends ActionCommand {

    private OrderService orderService;
    private static final Logger Log = Logger.getLogger(ListOrdersCommand.class);

    public ListOrdersCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Log.trace("Command starts execute");

        List<Order> orderList = orderService.findAllOrders();

        HttpSession session = request.getSession();
        session.setAttribute("orders", orderList);

        Log.trace("Command finishes");
        return "controller?p=authorizedPage&tab=orders";
    }
}
