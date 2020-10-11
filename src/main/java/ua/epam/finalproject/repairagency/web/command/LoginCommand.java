package ua.epam.finalproject.repairagency.web.command;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.model.User;
import ua.epam.finalproject.repairagency.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginCommand extends ActionCommand {

    private final UserService userService;
    private static final Logger Log = Logger.getLogger(LoginCommand.class);

    public LoginCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        Log.debug("Command starts");

        User registeredUser = userService.getRegisteredUser(request);
        Log.debug("Found User ==> " + registeredUser);

        try {
            response.setCharacterEncoding("utf-8");
            if(registeredUser != null) {
                response.getWriter().write(registeredUser.getPersonName()); // for javascript
                Log.debug("For javascript: " + registeredUser.getPersonName());
            } else {
                response.getWriter().write("Something wrong"); // for javascript
                Log.debug("Something wrong");
            }
        } catch (IOException e) {
            Log.error("Can't get Writer to response: " + e);
            throw new AppException(e.getMessage());
        }

        Log.debug("Command finished");
        return null;
    }
}
