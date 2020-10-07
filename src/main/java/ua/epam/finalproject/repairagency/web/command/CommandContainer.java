package ua.epam.finalproject.repairagency.web.command;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.TreeMap;
public class CommandContainer {

    private static Map<String, ActionCommand> actionCommands = new TreeMap<>();
    private static final Logger Log = Logger.getLogger(CommandContainer.class);

    static {
        // common commands
        actionCommands.put("register", new RegisterCommand());
        actionCommands.put("login", new LoginCommand());
        actionCommands.put("logout", new LogoutCommand());
//        actionCommands.put("noCommand", new NoCommand());
//        actionCommands.put("viewSettings", new ViewSettingsCommand());
//        actionCommands.put("updateSettings", new UpdateSettingsCommand());
//
//        // client commands
//        actionCommands.put("listMenu", new ListMenuCommand());
//
//        //admin commands
//        actionCommands.put("listOrders", new ListOrdersCommand());

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
