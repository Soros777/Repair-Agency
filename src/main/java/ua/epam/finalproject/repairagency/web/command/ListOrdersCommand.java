package ua.epam.finalproject.repairagency.web.command;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.model.Order;
import ua.epam.finalproject.repairagency.model.User;
import ua.epam.finalproject.repairagency.service.OrderService;
import ua.epam.finalproject.repairagency.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class ListOrdersCommand extends ActionCommand {

    private OrderService orderService;
    private UserService userService;
    private static final Logger Log = Logger.getLogger(ListOrdersCommand.class);

    public ListOrdersCommand(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Log.trace("Command starts execute");

        HttpSession session = request.getSession();
        String from = (String) session.getAttribute("from");
        String to = (String) session.getAttribute("to");
        String forMasterName = request.getParameter("forMasterName");
        Log.debug("Param from request  == forMasterName == is : " + forMasterName);

        List<Order> orderList = orderService.findForPeriod(from, to);

        if(!StringUtils.isEmpty(forMasterName)) {
            Log.debug("Filter for master name : " + forMasterName);
            orderList = orderService.filterMaster(orderList, forMasterName);
        }

        List<User> masters = userService.getAllMasters();

        session.setAttribute("orders", orderList);
        session.setAttribute("masters", masters);

        Log.trace("Command finishes");
        return "controller?p=authorizedPage&tab=orders";
    }
}
