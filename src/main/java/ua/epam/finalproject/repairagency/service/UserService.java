package ua.epam.finalproject.repairagency.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.dao.UserDao;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.model.Client;
import ua.epam.finalproject.repairagency.model.Role;
import ua.epam.finalproject.repairagency.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class UserService {

    public static final String PARAM_NAME_EMAIL = "email";
    private static final String PARAM_NAME_PASSWORD = "password";

    private static final Logger Log = Logger.getLogger(UserService.class);

    public static User getRegisteredUser(HttpServletRequest request) {
        Log.debug("Start getting user");
        Role role = UserUtil.determineRole(request);

        String email = request.getParameter(PARAM_NAME_EMAIL);
        String password = request.getParameter(PARAM_NAME_PASSWORD);

        if(!role.valueEqualsTo("Client")) {
            email = email.substring(1);
        }
        Log.debug("Entered email is : " + email);
        if(StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            Log.debug("Incorrect login or password");
            return null;
        }

        Log.debug("go to DB");
        User registeredUser = UserDao.getRegisteredUser(email, password, role);

        HttpSession session = request.getSession();
        session.setAttribute("user", registeredUser);
        Log.trace("The attribute \"user\" is placed in the session scope");

        Log.debug("Return registered user : " + registeredUser);
        return registeredUser;
    }

    public static boolean addNewClient(String clientEmail, String clientPassword, String clientName, Locale locale) {
        Log.debug("Start to add a new client to DB");

        Client client = new Client();
        try {
            client.setPassword(HashPassword.getHash(clientPassword));
        } catch (NoSuchAlgorithmException e) {
            Log.error("Can't get hash cos : " + e);
            throw new AppException("Can't get hash cos : ", e);
        }
        client.setEmail(clientEmail);
        client.setPersonName(clientName);
        client.setLocale(locale);
        return UserDao.addClient(client);
    }
}
