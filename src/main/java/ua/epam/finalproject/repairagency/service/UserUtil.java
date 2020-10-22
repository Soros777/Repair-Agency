package ua.epam.finalproject.repairagency.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.model.Client;
import ua.epam.finalproject.repairagency.model.Role;
import ua.epam.finalproject.repairagency.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserUtil {

    private static final Logger Log = Logger.getLogger(UserUtil.class);

    public static Role defineRole(HttpServletRequest request) {
        Log.trace("Start define a role");
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
        Log.trace("Defined role is : " + result.getValue());
        return result;
    }

    public static boolean checkRole(PreparedStatement rolePreparedStatement, int roleId, Role role) throws SQLException {
        Log.debug("Start checking roles");
        rolePreparedStatement.setInt(1, roleId);
        ResultSet resultSet = rolePreparedStatement.executeQuery();
        if(resultSet.next()) {
            String roleValue = resultSet.getString(1);
            Log.debug("Roles if obtained from DB");
            return roleValue.equalsIgnoreCase(role.getValue());
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
                return false;
            }
            Log.debug("Client name is empty");
            return false;
        }

        if(!validEmail(clientEmail)) {
            try {
                response.getWriter().write("not valid email");
            } catch (IOException e) {
                Log.error("Can't get request writer : " + e);
                return false;
            }
            Log.debug("Email is not valid according to regex");
            return false;
        }

        if(!checkPasswords(clientPassword, clientPasswordRepeat)) {
            try {
                response.getWriter().write("not same passwords");
            } catch (IOException e) {
                Log.error("Can't get request writer : " + e);
                return false;
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

    private static boolean checkPasswords(String pass, String passRep) {
        Log.trace("Start check passwords to be the same");
        if(!StringUtils.isEmpty(pass) && !StringUtils.isEmpty(passRep) && pass.equals(passRep)) {
            Log.trace("Passwords are OK");
            return true;
        }
        Log.trace("Password is not correct");
        return false;
    }

    public static Client getClientFromParam(String clientEmail, String personName, String clientPassword, Locale locale) {
        Client client = new Client();
        client.setEmail(clientEmail);
        client.setPersonName(personName);
        client.setPassword(HashPassword.getHash(clientPassword));
        client.setLocale(locale);
        client.setRole(Role.CLIENT);
        return client;
    }

    public static User getUserFromParam(int id, String email, String password, String personName, Role role,
                                        String photoPath, String contactPhone, Locale locale, String dateStr) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPassword(password);
        user.setPersonName(personName);
        user.setRole(role);
        user.setPhotoPath(photoPath);
        user.setContactPhone(contactPhone);
        user.setLocale(locale);
        user.setRegistrationDate(LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return user;
    }

    public static Client getClientFromUser(User user, double walletValue, boolean status) {
        Client client = new Client();
        client.setId(user.getId());
        client.setEmail(user.getEmail());
        client.setPersonName(user.getPersonName());
        client.setPassword(user.getPassword());
        client.setPhotoPath(user.getPhotoPath());
        client.setRole(user.getRole());
        client.setWalletCount(walletValue);
        client.setContactPhone(user.getContactPhone());
        client.setLocale(user.getLocale());
        client.setRegistrationDate(user.getRegistrationDate());
        client.setStatus(status);
        return client;
    }

    public static Client getClientFromUser(User user, double walletValue) {
        Client client = new Client();
        client.setId(user.getId());
        client.setEmail(user.getEmail());
        client.setPersonName(user.getPersonName());
        client.setPassword(user.getPassword());
        client.setPhotoPath(user.getPhotoPath());
        client.setRole(user.getRole());
        client.setWalletCount(walletValue);
        client.setContactPhone(user.getContactPhone());
        client.setLocale(user.getLocale());
        client.setRegistrationDate(user.getRegistrationDate());
        return client;
    }

    public static boolean checkUser(User registeredUser, String password, Role roleFromRequest) {
        if(!registeredUser.getPassword().equals(password)) {
            return false;
        }
        if(registeredUser.getRole() != roleFromRequest) {
            return false;
        }
        return true;
    }
}

