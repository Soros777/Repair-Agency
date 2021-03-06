package ua.epam.finalproject.repairagency.web.command;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.model.Order;
import ua.epam.finalproject.repairagency.model.User;
import ua.epam.finalproject.repairagency.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ChangeOrderCostCommand extends ActionCommand {

    private OrderService orderService;
    private static final Logger Log = Logger.getLogger(ChangeOrderCostCommand.class);

    public ChangeOrderCostCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Log.trace("Command starts");

        HttpSession session = request.getSession();

        Order order = (Order) session.getAttribute("order");
        User manager = (User) session.getAttribute("user");
        String orderCost = request.getParameter("orderCost");
        Log.debug("Params are : order : " + order + "; orderCost : " + orderCost);

        Order updatedOrder = orderService.setOrderCost(order, orderCost, manager);

        session.setAttribute("order", updatedOrder);

        Log.trace("Command finishes");
        return "controller?p=authorizedPage&tab=order";
    }
}
