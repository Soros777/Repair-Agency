package ua.epam.finalproject.repairagency.web.command;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.model.Client;
import ua.epam.finalproject.repairagency.service.OrderService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CreateOrderCommand extends ActionCommand {

    private OrderService orderService;
    private static final Logger Log = Logger.getLogger(CreateOrderCommand.class);

    public CreateOrderCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Log.trace("Command starts");

        HttpSession session = request.getSession();
        Client client = (Client) session.getAttribute("user");
        int clientId = client.getId();
        Log.debug("Determined client id : " + clientId);

        String deviceStr = request.getParameter("device");
        String description = request.getParameter("description");
        Log.debug("Parameters are: " + deviceStr + " and " + description);

        if(!orderService.createOrder(clientId, deviceStr, description)) {
            Log.error("Can't execute command");
            throw new AppException("Can't execute command");
        }

        return "controller?p=authorizedPage&tab=create&conf=thanks";
    }
}
