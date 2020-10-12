package ua.epam.finalproject.repairagency.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.model.Client;
import ua.epam.finalproject.repairagency.model.Role;
import ua.epam.finalproject.repairagency.model.User;
import ua.epam.finalproject.repairagency.repository.ConnectionPool;
import ua.epam.finalproject.repairagency.repository.UserDao;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;

public class UserService {

    public static final String PARAM_NAME_EMAIL = "email";
    private static final String PARAM_NAME_PASSWORD = "password";
    private final UserDao userDao;

    private static final Logger Log = Logger.getLogger(UserService.class);

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User getRegisteredUser(HttpServletRequest request) {
        Log.debug("Start getting user");
        Role role = UserUtil.defineRole(request);

        String email = request.getParameter(PARAM_NAME_EMAIL);
        String password = request.getParameter(PARAM_NAME_PASSWORD);

        password = HashPassword.getHash(password);
        if(!role.valueEqualsTo("Client")) {
            email = email.substring(1);
        }
        Log.debug("Entered email is : " + email);
        if(StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            Log.debug("Incorrect login or password");
            return null;
        }

        User registeredUser;
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            Connection connection = connectionPool.getConnection();
            Log.debug("go to DB");
            registeredUser = userDao.getRegisteredUser(connection, email, password, role);
        } catch (SQLException | NamingException e) {
            Log.error("Can't get Registered user cause : " + e);
            throw new AppException("Can't get Registered user", e);
        }

        HttpSession session = request.getSession();
        session.setAttribute("user", registeredUser);
        Log.trace("The attribute \"user\" is placed in the session scope");

        Log.debug("Return registered user : " + registeredUser);
        return registeredUser;
    }

    public boolean addNewClient(String clientEmail, String clientPassword, String clientName, Locale locale) {
        Log.debug("Start to add a new client to DB");

        Client client = new Client();
        client.setPassword(HashPassword.getHash(clientPassword));
        client.setEmail(clientEmail);
        client.setPersonName(clientName);
        client.setLocale(locale);

        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();

            if(userDao.addClient(connection, client)) {
                connection.commit();
                Log.debug("Success! New == Client == is in DB.");
                return true;
            }
        } catch (SQLException e) {
            if(connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) { } // just about close connection
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
