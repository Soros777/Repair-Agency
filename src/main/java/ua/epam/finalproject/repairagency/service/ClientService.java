package ua.epam.finalproject.repairagency.service;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.database.ConnectionPool;
import ua.epam.finalproject.repairagency.model.Client;
import ua.epam.finalproject.repairagency.web.Controller;

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

    public static Client findClient(String email) {
        Log.info("finding client");
        if(email == null || "".equals(email)) {
            return null;
        }

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM clients WHERE email=?"))
            {
                preparedStatement.setString(1, email);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()) {
                    Locale userLocale = Locale.forLanguageTag(resultSet.getString("locale"));
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDate userRegistrationDate = LocalDate.parse(resultSet.getString("registration_date"), dateTimeFormatter);
                    Client client = Client.getClientWithInitParams(
                            resultSet.getInt("id"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getString("client_name"),
                            Double.parseDouble(resultSet.getString("wallet_count")),
                            resultSet.getString("contact_phone"),
                            userLocale,
                            userRegistrationDate);
                    return client;
                }
            } catch (SQLException | NamingException e) {
                e.printStackTrace();
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
