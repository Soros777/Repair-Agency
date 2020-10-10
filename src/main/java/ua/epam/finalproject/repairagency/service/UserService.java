package ua.epam.finalproject.repairagency.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.dao.ConnectionPool;
import ua.epam.finalproject.repairagency.dao.UserDao;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.model.Client;
import ua.epam.finalproject.repairagency.model.Role;
import ua.epam.finalproject.repairagency.model.User;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
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

        User registeredUser = null;
        Log.debug("go to DB");
        try {
            Connection connection = ConnectionPool.getInstance().getConnection();
            registeredUser = UserDao.getRegisteredUser(connection, email, password, role);
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }

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

        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();

            if(UserDao.addClient(connection, client)) {
                connection.commit();
                Log.debug("Success! New Client is in DB.");
                return true;
            }
        } catch (SQLException e) {
            if(connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) { }
            }
        } catch (NamingException e) {
            Log.error("Can't get instance of Connection Pool cause : " + e);
            throw new AppException("Can't get instance of Connection Pool", e);
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) { }
            }
        }

        Log.error("Failing during adding a new client to DB");
        return false;
    }
}
