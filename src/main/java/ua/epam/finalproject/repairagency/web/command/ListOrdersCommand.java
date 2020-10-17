package ua.epam.finalproject.repairagency.web.command;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.model.Order;
import ua.epam.finalproject.repairagency.service.OrderService;
import ua.epam.finalproject.repairagency.service.OrderUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
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

        String from = request.getParameter("fromDate");
        String to = request.getParameter("toDate");
        Log.debug("!!!!========= params are: fromDate : " + from + "  and  toDate : " + to);

        List<Order> orderList = null;
        if(StringUtils.isEmpty(from) && StringUtils.isEmpty(to)) {
            Log.debug("Parameters from and to are empty");
            orderList = orderService.findAllOrders();
            from = OrderUtil.getFromDate("weekStart");
            to = OrderUtil.getFromDate("now");
        } else {
            orderList = orderService.findForPeriod(from, to);
        }


        HttpSession session = request.getSession();
        session.setAttribute("orders", orderList);

        Log.debug("!!!!==one==!!! from : " + from);
        session.setAttribute("from", from);
        Log.debug("!!!!==two==!!! from : " + from);
        session.setAttribute("to", to);

        Log.trace("Command finishes");
        return "controller?p=authorizedPage&tab=orders";
    }
}
