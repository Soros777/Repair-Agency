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

        HttpSession session = request.getSession();
        String from = (String) session.getAttribute("from");
        String to = (String) session.getAttribute("to");
        String forMasterId = (String) session.getAttribute("forMasterId");

        List<Order> orderList = orderService.findForPeriod(from, to);

        if(!StringUtils.isEmpty(forMasterId)) {
            orderList = orderService.filterMaster(orderList, forMasterId);
        }

        session.setAttribute("orders", orderList);

        Log.trace("Command finishes");
        return "controller?p=authorizedPage&tab=orders";
    }
}
