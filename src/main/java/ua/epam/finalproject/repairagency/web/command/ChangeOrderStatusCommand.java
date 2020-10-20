package ua.epam.finalproject.repairagency.web.command;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.model.Order;
import ua.epam.finalproject.repairagency.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ChangeOrderStatusCommand extends ActionCommand {

    private OrderService orderService;
    private static final Logger Log = Logger.getLogger(ChangeOrderStatusCommand.class);

    public ChangeOrderStatusCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Log.trace("Command starts");

        HttpSession session = request.getSession();

        Order order = (Order) session.getAttribute("order");
        String newOrderStatus = request.getParameter("newOrderStatus");
        Log.debug("Params are : order : " + order + "; newOrderStatus : " + newOrderStatus);

        Order updatedOrder = orderService.changeStatus(order, newOrderStatus);

        session.setAttribute("order", updatedOrder);

        Log.trace("Command finishes");
        return "controller?p=authorizedPage&tab=order";
    }
}
