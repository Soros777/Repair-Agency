package ua.epam.finalproject.repairagency.web.command;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.model.Client;
import ua.epam.finalproject.repairagency.repository.ConnectionPool;
import ua.epam.finalproject.repairagency.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LeaveFeedbackCommand extends ActionCommand {

    private OrderService orderService;
    private static final Logger Log = Logger.getLogger(LeaveFeedbackCommand.class);

    public LeaveFeedbackCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Log.trace("Command starts");

        HttpSession session = request.getSession();
        String orderId = request.getParameter("orderId");
        String feedbackText = request.getParameter("feedbackText");
        ConnectionPool connectionPool = (ConnectionPool) session.getAttribute("connectionPool");
        Log.debug("Parameter is: orderId : " + orderId);

        orderService.addFeedBack(connectionPool, orderId, feedbackText);

        return "controller?p=authorizedPage&tab=create&conf=thanks";
    }
}
