package ua.epam.finalproject.repairagency.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.model.Client;
import ua.epam.finalproject.repairagency.model.Role;
import ua.epam.finalproject.repairagency.model.User;
import ua.epam.finalproject.repairagency.repository.ConnectionPool;
import ua.epam.finalproject.repairagency.repository.EntityContainer;
import ua.epam.finalproject.repairagency.repository.UserDao;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
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

        Role roleFromRequest = UserUtil.defineRole(request);

        String email = request.getParameter(PARAM_NAME_EMAIL);
        String password = request.getParameter(PARAM_NAME_PASSWORD);
        password = HashPassword.getHash(password);
        if(!roleFromRequest.valueEqualsTo(Role.CLIENT.getValue())) {
            email = email.substring(1);
        }

        Log.debug("Entered email is : " + email);

        if(StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            Log.debug("Incorrect login or password");
            return null;
        }

        Connection connection;
        try {
            connection = ConnectionPool.getInstance().getConnection();
        } catch (SQLException | NamingException e) {
            Log.error("Cant obtain connection cause : " + e);
            throw new AppException("Can't authorise user");
        }

        User registeredUser;
        try {
            registeredUser = userDao.getUserByEmail(connection, email);

            if(registeredUser != null && registeredUser.getRole() == Role.CLIENT) {
                Log.debug("It is a client");
                double walletValue = userDao.getWalletValue(connection, registeredUser.getId());
                registeredUser = UserUtil.getClientFromUser(registeredUser, walletValue);
            }
            if(registeredUser == null) {
                return null;
            }
        } catch (SQLException e) {
            Log.error("Can't get user from db cause : " + e);
            throw new AppException("Can't authorise user");
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    Log.error("Can't close connection");
                }
            }
        }

        if(!UserUtil.checkUser(registeredUser, password, roleFromRequest)) {
            registeredUser = null;
        }

        HttpSession session = request.getSession();
        session.setAttribute("user", registeredUser);
        Log.trace("The attribute \"user\" is placed in the session scope " + registeredUser);

        return registeredUser;
    }

    public boolean addNewClient(String clientEmail, String clientPassword, String clientPasswordRepeat,
                                String personName, Locale locale, HttpServletResponse response) {
        Log.debug("Start to add a new client to DB");

        if(!UserUtil.validateEnteredData(personName, clientEmail, clientPassword, clientPasswordRepeat, response)) {
            return false;
        }

        Connection connection;
        try {
            connection = ConnectionPool.getInstance().getConnection();
        } catch (SQLException | NamingException e) {
            Log.error("Can't get connection cause : " + e);
            throw new AppException("Can't add new client");
        }

        if(!checkDontRepeatEmail(connection, clientEmail, response)) {
            return false;
        }

        Client client = UserUtil.getClientFromParam(clientEmail, personName, clientPassword, locale);

        int localeId;
        int roleId;

        try {
            localeId = userDao.getIdFromLocale(connection, locale);
            roleId = userDao.getIdFromRole(connection, client.getRole());
            int userId = userDao.addUser(connection, client, localeId, roleId);
            client.setId(userId);
            userDao.addClientWallet(connection, userId);

            connection.commit();
            Log.debug("Success! New == Client == is in DB.");
            return true;
        } catch (SQLException e) {
            Log.error("Cant obtain id for locale or role" + e);
            if(connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    Log.error("Can't close connection"  + e);
                }
            }
            throw new AppException("Can't add new user");
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Log.error("Can't close connection");
                }
            }
        }
    }

    private boolean checkDontRepeatEmail(Connection connection, String clientEmail, HttpServletResponse response) {
        Log.debug("Start to check entered email for repeating in DB");
        try {
            User user = userDao.getUserByEmail(connection, clientEmail);
            if(user != null) {
                response.getWriter().write("repeated email");
                return false;
            }
        } catch (SQLException e) {
            Log.error("Can't add new client cause : " + e);
            try {
                connection.close();
            } catch (SQLException ex) {
                Log.error("Can't close connection cause : " + ex);
            }
            throw new AppException("Can't add new client");
        } catch (IOException e) {
            Log.error("Can't get response writer cause : " + e);
            throw new AppException("Can't add new client");
        }
        Log.debug("Entered email is new");
        return true;
    }

    public List<User> getAllMasters() {
        Log.trace("Start get all masters");
        Role role = Role.MASTER;
        int roleId = EntityContainer.getIdFromRole(role);

        Connection connection;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            return userDao.getUsersViaRole(connection, roleId);
        } catch (SQLException | NamingException e) {
            Log.error("Can't get all masters cause : " + e);
            throw new AppException("Can't get all masters");
        }
    }
}
