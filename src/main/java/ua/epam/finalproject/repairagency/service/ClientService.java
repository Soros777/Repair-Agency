package ua.epam.finalproject.repairagency.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.database.ConnectionPool;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.model.Client;
import ua.epam.finalproject.repairagency.to.ClientTo;
import ua.epam.finalproject.repairagency.web.controller.Controller;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ClientService {

    private static final Logger Log = Logger.getLogger(Controller.class);

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
                e.printStackTrace();
            }

        return false;
    }
}