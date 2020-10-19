package ua.epam.finalproject.repairagency.web.command;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.model.Order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class ShowOrderCommand extends ActionCommand {

    private static final Logger Log = Logger.getLogger(ShowOrderCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Log.trace("Command start");

        HttpSession session = request.getSession();
        List<Order> orderList = (List<Order>) session.getAttribute("orders");
        String orderId = request.getParameter("orderId");
        Log.trace("Order list size is : " + orderList.size() + "; orderId : " + orderId);

        Order requestedOder = null;
        for (Order order : orderList) {
            if(order.getId() == Integer.parseInt(orderId)) {
                Log.trace("Requested order is obtained");
                requestedOder = order;
                session.setAttribute("order", order);
                break;
            }
        }

        if(requestedOder == null) {
            Log.error("Can't obtain requested order");
            throw new AppException("Can't obtain requested order");
        }

        Log.trace("Command finishes");
        return "controller?p=authorizedPage&tab=order";
    }
}
