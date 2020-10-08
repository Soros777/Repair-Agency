package ua.epam.finalproject.repairagency.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.Util;
import ua.epam.finalproject.repairagency.database.ConnectionPool;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.model.Client;
import ua.epam.finalproject.repairagency.to.ClientTo;
import ua.epam.finalproject.repairagency.web.controller.Controller;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientService {

    private static final Logger Log = Logger.getLogger(ClientService.class);

    public static ClientTo findClient(String email, String password) throws AppException {
        Log.info("finding client");
        if(StringUtils.isEmpty(email) | StringUtils.isEmpty(password)) {
            throw new AppException("Incorrect login / password");
        }

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM clients WHERE email=?"))
            {
                preparedStatement.setString(1, email);
                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()) {
                    String passFromBD = resultSet.getString("password");
                    if(!password.equals(passFromBD)) {
                        throw new AppException("Incorrect login / password");
                    }
                    Locale userLocale = Locale.forLanguageTag(resultSet.getString("locale"));
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDate userRegistrationDate = LocalDate.parse(resultSet.getString("registration_date"), dateTimeFormatter);
                    int id = resultSet.getInt("id");
                    String email1 = resultSet.getString("email");
                    String client_name = resultSet.getString("client_name");
                    double wallet_count = Double.parseDouble(resultSet.getString("wallet_count"));
                    String contact_phone = resultSet.getString("contact_phone");

                    ClientTo clientTo = ClientTo.getClientToWithInitParams(
                            id,
                            email1,
                            client_name,
                            wallet_count,
                            contact_phone,
                            userLocale,
                            userRegistrationDate);
                    return clientTo;
                }
            } catch (SQLException | NamingException e) {
                throw new AppException("Can't obtain Client from DB", e);
            }

        return null;
    }

    public static boolean addClient(Client client) {
        Log.info("adding client");
        if(client == null) {
            return false;
        }

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO clients (email, password, client_name) VALUES (?, ?, ?)"))
        {
            int k = 1;
            preparedStatement.setString(k++, client.getEmail());
            preparedStatement.setString(k++, client.getPassword());
            preparedStatement.setString(k, client.getClientName());

            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException | NamingException e) {
                Log.error("Can't put new Client to DB" + e);
                throw new AppException(e.getMessage());
        }
    }

    public static void setSessionAttributes(HttpServletRequest request, ClientTo clientTo) {

        HttpSession session = request.getSession();

        session.setAttribute("user", clientTo);
        Log.trace("Set session attribute \"user\" : " + clientTo);
        session.setAttribute("role", "client");
        Log.trace("Set session attribute \"role\" : client");
        session.setAttribute("userName", clientTo.getClientName());
        Log.trace("Set session attribute \"userName\" : " + clientTo.getClientName());
        Log.debug("ID session is: " + session.getId());
    }

    public static boolean validEmail(String emailStr) {
        Log.trace("Start check regex email");
        Pattern emailValidationPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailValidationPattern.matcher(emailStr);
        if(!matcher.find()) {
            return false;
        }
        Log.trace("Email regex is OK");
        return true;
    }

    public static boolean dontRepeatEmail(String emailStr) {
        Log.trace("Start check email for not existing in DB");
        List<String> emailFromDB = new ArrayList<>();
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             Statement statement = connection.createStatement())
        {
            ResultSet resultSet = statement.executeQuery("SELECT email FROM clients");
            while (resultSet.next()) {
                emailFromDB.add(resultSet.getString(1));
            }
            if(emailFromDB.contains(emailStr)) {
                return false;
            }
        } catch (SQLException | NamingException e) {
            Log.error("Can't get emails from DB" + e);
            throw new AppException(e.getMessage());
        }
        Log.trace("New email is OK");
        return true;
    }

    public static boolean checkPasswords(String pass, String passRep) {
        Log.trace("Start check passwords to be the same");
        if(!StringUtils.isEmpty(pass) && !StringUtils.isEmpty(passRep) && pass.equals(passRep)) {
            Log.trace("Passwords are OK");
            return true;
        }
        return false;
    }
}
