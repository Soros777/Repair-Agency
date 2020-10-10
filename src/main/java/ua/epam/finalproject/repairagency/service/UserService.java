package ua.epam.finalproject.repairagency.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.database.ConnectionPool;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.model.Client;
import ua.epam.finalproject.repairagency.model.Role;
import ua.epam.finalproject.repairagency.model.User;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserService {

    private static final String PARAM_NAME_EMAIL = "email";
    private static final String PARAM_NAME_PASSWORD = "password";

    private static Role role;
    private static final Logger Log = Logger.getLogger(UserService.class);

    private static Role determineRole(HttpServletRequest request) {
        Log.trace("Start determine a role");
        String email = request.getParameter(PARAM_NAME_EMAIL);
        char pre = email.charAt(0);

        switch (pre) {
            case 36: // $ DIRECTOR
                role = Role.DIRECTOR;
                break;
            case 33: // ! ADMIN
                role = Role.ADMINISTRATOR;
                break;
            case 64: // @ MANAGER
                role = Role.MANAGER;
                break;
            case 35: // # MASTER
                role = Role.MASTER;
                break;
            default: // CLIENT
                role = Role.CLIENT;
        }

        HttpSession session = request.getSession();
        session.setAttribute("role", role.value());
        Log.trace("Determined role is in the session scope with attribute \"role\":" + role.value());
        return role;
    }

    public static User getRegisteredUser(HttpServletRequest request) {
        Log.debug("Start getting user");
        Role role = determineRole(request);

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
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             // todo : joinquery
             PreparedStatement mainPreparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email=?");
             PreparedStatement localePreparedStatement = connection.prepareStatement("SELECT value FROM locales WHERE id=?");
             PreparedStatement rolePreparedStatement = connection.prepareStatement("SELECT value FROM roles WHERE id=?");
             PreparedStatement walletCountPreparedStatement = connection.prepareStatement("SELECT wallet_count FROM clients WHERE parent=?")
            )
        {
            Log.debug("Connections and preparedStatements are obtained");
            password = HashPassword.getHash(password);

            mainPreparedStatement.setString(1, email);

            Log.debug("start to execute main query");
            ResultSet resultSet = mainPreparedStatement.executeQuery();
            Log.debug("mainQuery executed");

            User registeredUser = null;
            if(resultSet.next()) {
                Log.debug("There is a recording in the DB");
                String passwordFromDB = resultSet.getString("password");
                if(!password.equals(passwordFromDB)) {
                    Log.trace("Entered incorrect password");
                    return null;
                }
                Log.trace("password is ok");
                int roleId = resultSet.getInt("role_id");
                if(!checkRole(rolePreparedStatement, roleId, role)) {
                    Log.debug("Roles are different");
                    return null;
                }
                int localeId = resultSet.getInt("locale_id");
                Locale userLocale = getUserLocale(localePreparedStatement, localeId);

                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDate userRegistrationDate = LocalDate.parse(resultSet.getString("registration_date"), dateTimeFormatter);

                int id = resultSet.getInt("id");
                String personName = resultSet.getString("person_name");
                String photoPath = resultSet.getString("photo_path");
                String contactPhone = resultSet.getString("contact_phone");

                Log.debug("Start to create user");
                if(role.valueEqualsTo("Client")) {
                    registeredUser = new Client();
                } else {
                    registeredUser = new User();
                }
                registeredUser.setId(id);
                registeredUser.setEmail(email);
                registeredUser.setPassword(password);
                registeredUser.setPersonName(personName);
                registeredUser.setRole(role);
                registeredUser.setPhotoPath(photoPath);
                registeredUser.setContactPhone(contactPhone);
                registeredUser.setLocale(userLocale);
                registeredUser.setRegistrationDate(userRegistrationDate);
                Log.debug("User fields are initiated");
            }

            if(registeredUser != null && role.valueEqualsTo("Client")) {
                Log.trace("It is a client");
                double walletCount = getWalletCount(walletCountPreparedStatement, registeredUser.getId());
                assert registeredUser instanceof Client;
                ((Client) registeredUser).setWalletCount(walletCount);

            }

            HttpSession session = request.getSession();
            session.setAttribute("user", registeredUser);
            Log.trace("The attribute \"user\" is placed in the session scope");

            Log.debug("Return registered user : " + registeredUser);
            return registeredUser;

        } catch (NamingException | SQLException | NoSuchAlgorithmException e) {
            Log.error("Can't obtain User from DB");
            throw new AppException("Can't obtain User from DB.", e);
        }
    }

    private static double getWalletCount(PreparedStatement walletCountPreparedStatement, int id) throws SQLException {
        Log.debug("Start to obtain client wallet count for client with id : " + id);
        walletCountPreparedStatement.setInt(1, id);
        ResultSet resultSet = walletCountPreparedStatement.executeQuery();
        if(resultSet.next()) {
            return resultSet.getDouble(1);
        }
        Log.error("Can't obtain client wallet count");
        throw new AppException("Can't obtain client wallet count");
    }

    private static boolean checkRole(PreparedStatement rolePreparedStatement, int roleId, Role role) throws SQLException {
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

    private static Locale getUserLocale(PreparedStatement localePreparedStatement, int localeId) throws SQLException {
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
        return false;
    }

    public static boolean validateEnteredData(String clientName, String clientEmail, String clientPassword,
                                              String clientPasswordRepeat, HttpServletResponse response)
    {

        if(StringUtils.isEmpty(clientName)) {
            try {
                response.getWriter().write("introduce yourself");
            } catch (IOException e) {
                Log.error("Can't get request writer");
                throw new AppException("Can't get request writer", e);
            }
            Log.debug("Client name is empty");
            return false;
        }

        if(!validEmail(clientEmail)) {
            try {
                response.getWriter().write("not valid email");
            } catch (IOException e) {
                Log.error("Can't get request writer");
                throw new AppException("Can't get request writer", e);
            }
            Log.debug("Email is not valid according to regex");
            return false;
        }

        if(!dontRepeatEmail(clientEmail)) {
            try {
                response.getWriter().write("repeated email");
            } catch (IOException e) {
                Log.error("Can't get request writer");
                throw new AppException("Can't get request writer", e);
            }
            Log.debug("Email is repeated");
            return false;
        }

        if(checkPasswords(clientPassword, clientPasswordRepeat)) {
            try {
                response.getWriter().write("not same passwords");
            } catch (IOException e) {
                Log.error("Can't get request writer");
                throw new AppException("Can't get request writer", e);
            }
            Log.debug("Password is not the same");
            return false;
        }

        return true;
    }
}
