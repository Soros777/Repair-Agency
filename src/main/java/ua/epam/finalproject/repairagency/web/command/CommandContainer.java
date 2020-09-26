package ua.epam.finalproject.repairagency.web.command;

import org.slf4j.Logger;
import ua.epam.finalproject.repairagency.web.ClientServlet;

import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class CommandContainer {

    private static Map<String, Command> commands;
    private static final Logger Log = getLogger(ClientServlet.class);

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
