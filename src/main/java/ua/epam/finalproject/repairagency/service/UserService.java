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
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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
}
