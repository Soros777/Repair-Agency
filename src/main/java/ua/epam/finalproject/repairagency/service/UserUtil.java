package ua.epam.finalproject.repairagency.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.repository.ConnectionPool;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.model.Role;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserUtil {

    private static final Logger Log = Logger.getLogger(UserUtil.class);

    public static Role determineRole(HttpServletRequest request) {
        Log.trace("Start determine a role");
        String email = request.getParameter(UserService.PARAM_NAME_EMAIL);
        Role result;
        char pre = email.charAt(0);
        switch (pre) {
            case 36: // $ DIRECTOR
                result = Role.DIRECTOR;
                break;
            case 33: // ! ADMIN
                result = Role.ADMINISTRATOR;
                break;
            case 64: // @ MANAGER
                result = Role.MANAGER;
                break;
            case 35: // # MASTER
                result = Role.MASTER;
                break;
            default: // CLIENT
                result = Role.CLIENT;
        }
        HttpSession session = request.getSession();
        session.setAttribute("role", result.value());
        Log.trace("Determined role is in the session scope with attribute \"role\":" + result.value());
        return result;
    }

    public static boolean checkRole(PreparedStatement rolePreparedStatement, int roleId, Role role) throws SQLException {
        Log.debug("Start checking roles");
        rolePreparedStatement.setInt(1, roleId);
        ResultSet resultSet = rolePreparedStatement.executeQuery();
        if(resultSet.next()) {
            String roleValue = resultSet.getString(1);
            Log.debug("Roles are ok");
            return roleValue.equalsIgnoreCase(role.value());
        }
        Log.error("Failing checking roles");
        return false;
    }

    public static Locale getUserLocale(PreparedStatement localePreparedStatement, int localeId) throws SQLException {
        Log.debug("Start to obtain user locale");
        localePreparedStatement.setInt(1, localeId);
        ResultSet userLocaleRS = localePreparedStatement.executeQuery();
        if(userLocaleRS.next()) {
            String localeValue = userLocaleRS.getString(1);
            Log.trace("Locale is : " + localeValue);
            return Locale.forLanguageTag(localeValue);
        }
        Log.error("Can't obtain locale");
        throw new AppException("Can't obtain locale");
    }

    public static boolean validateEnteredData(String clientName, String clientEmail, String clientPassword,
                                              String clientPasswordRepeat, HttpServletResponse response)
    {

        if(StringUtils.isEmpty(clientName)) {
            try {
                response.getWriter().write("introduce yourself");
            } catch (IOException e) {
                Log.error("Can't get request writer : " + e);
                throw new AppException("Can't get request writer", e);
            }
            Log.debug("Client name is empty");
            return false;
        }

        if(!UserUtil.validEmail(clientEmail)) {
            try {
                response.getWriter().write("not valid email");
            } catch (IOException e) {
                Log.error("Can't get request writer : " + e);
                throw new AppException("Can't get request writer", e);
            }
            Log.debug("Email is not valid according to regex");
            return false;
        }

        if(!dontRepeatEmail(clientEmail)) {
            try {
                response.getWriter().write("repeated email");
            } catch (IOException e) {
                Log.error("Can't get request writer : " + e);
                throw new AppException("Can't get request writer", e);
            }
            Log.debug("Email is repeated");
            return false;
        }

        if(!checkPasswords(clientPassword, clientPasswordRepeat)) {
            try {
                response.getWriter().write("not same passwords");
            } catch (IOException e) {
                Log.error("Can't get request writer : " + e);
                throw new AppException("Can't get request writer", e);
            }
            Log.debug("Password is not the same");
            return false;
        }

        return true;
    }

    private static boolean validEmail(String emailStr) {
        Log.trace("Start check regex email");
        Pattern emailValidationPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailValidationPattern.matcher(emailStr);
        if(!matcher.find() || (emailStr.length() > 129)) {
            Log.trace("Entered email is not valid");
            return false;
        }
        Log.trace("Email regex is OK");
        return true;
    }

    private static boolean dontRepeatEmail(String emailStr) {
        Log.trace("Start check email for not existing in DB");
        List<String> emailFromDB = new ArrayList<>();
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             Statement statement = connection.createStatement())
        {
            ResultSet resultSet = statement.executeQuery("SELECT email FROM users");
            while (resultSet.next()) {
                emailFromDB.add(resultSet.getString(1));
            }
            if(emailFromDB.contains(emailStr)) {
                return false;
            }
        } catch (SQLException | NamingException e) {
            Log.error("Can't get emails from DB" + e);
            throw new AppException("Can't get emails from DB", e);
        }
        Log.trace("New email is OK");
        return true;
    }

    private static boolean checkPasswords(String pass, String passRep) {
        Log.trace("Start check passwords to be the same");
        if(!StringUtils.isEmpty(pass) && !StringUtils.isEmpty(passRep) && pass.equals(passRep)) {
            Log.trace("Passwords are OK");
            return true;
        }
        Log.trace("Password is not correct");
        return false;
    }
}
