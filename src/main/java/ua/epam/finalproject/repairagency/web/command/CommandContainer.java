package ua.epam.finalproject.repairagency.web.command;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.repository.DeviceDao;
import ua.epam.finalproject.repairagency.repository.OrderDaoDB;
import ua.epam.finalproject.repairagency.repository.UserDaoDB;
import ua.epam.finalproject.repairagency.service.OrderService;
import ua.epam.finalproject.repairagency.service.UserService;

import java.util.Map;
import java.util.TreeMap;
public class CommandContainer {

    private static Map<String, ActionCommand> actionCommands = new TreeMap<>();
    private static final Logger Log = Logger.getLogger(CommandContainer.class);

    static {
        // common commands
        UserService userService = new UserService(new UserDaoDB());
        actionCommands.put("register", new RegisterCommand(userService));
        actionCommands.put("login", new LoginCommand(userService));
        actionCommands.put("logout", new LogoutCommand());
//        actionCommands.put("noCommand", new NoCommand());
//        actionCommands.put("viewSettings", new ViewSettingsCommand());
//        actionCommands.put("updateSettings", new UpdateSettingsCommand());
//
        // client commands
        OrderService orderService = new OrderService(new OrderDaoDB(), new DeviceDao());
        actionCommands.put("createOrder", new CreateOrderCommand(orderService));
//        actionCommands.put("listMenu", new ListMenuCommand());
//
        //manager commands
        actionCommands.put("listOrders", new ListOrdersCommand(orderService));
        actionCommands.put("showOrder", new ShowOrderCommand());

        Log.debug("Command container was successfully initialized");
        Log.trace("Number of commands --> " + actionCommands.size());
    }

    /**
     * Returns command object with the given name.
     *
     * @param commandName
     *              Name of the command.
     * @return Command object
     */
    public static ActionCommand get(String commandName) {
        if(commandName == null || !actionCommands.containsKey(commandName)) {
            Log.trace("Command not found, name --> " + commandName);
            return actionCommands.get("noCommand");
        }
        return actionCommands.get(commandName);
    }
}
