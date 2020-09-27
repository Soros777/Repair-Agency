package ua.epam.finalproject.repairagency.web.command;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.TreeMap;
public class CommandContainer {

    private static Map<String, Command> commands = new TreeMap<>();
    private static final Logger Log = Logger.getLogger(CommandContainer.class);

    static {
        // common commands
        commands.put("register", new RegisterCommand());
        commands.put("login", new LoginCommand());
//        commands.put("logout", new LogoutCommand());
//        commands.put("noCommand", new NoCommand());
//        commands.put("viewSettings", new ViewSettingsCommand());
//        commands.put("updateSettings", new UpdateSettingsCommand());
//
//        // client commands
//        commands.put("listMenu", new ListMenuCommand());
//
//        //admin commands
//        commands.put("listOrders", new ListOrdersCommand());

        Log.debug("Command container was successfully initialized");
        Log.trace("Number of commands --> " + commands.size());
    }

    /**
     * Returns command object with the given name.
     *
     * @param commandName
     *              Name of the command.
     * @return Command object
     */
    public static Command get(String commandName) {
        if(commandName == null || !commands.containsKey(commandName)) {
            Log.trace("Command not found, name --> " + commandName);
            return commands.get("noCommand");
        }
        return commands.get(commandName);
    }
}
