package ua.epam.finalproject.repairagency.services;

import ua.epam.finalproject.repairagency.dao.DBManager;
import ua.epam.finalproject.repairagency.model.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ClientService {

    public static Client findClient(String clientEmailFromRequest) {
        if(clientEmailFromRequest == null || "".equals(clientEmailFromRequest)) {
            return null;
        }

        DBManager dbManager = DBManager.getInstance();

        try {
            Connection connection = dbManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM clients");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String clientMailFromDB = resultSet.getString("email");
                if(clientEmailFromRequest.equals(clientMailFromDB)) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addClient(Client client) {
        if(client == null) {
            return false;
        }

        DBManager dbManager = DBManager.getInstance();

        try (Connection connection = dbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO clients (email, password, client_name) VALUES (?, ?, ?)"))
            {
                preparedStatement.setString(1, client.getEmail());
                preparedStatement.setString(2, client.getPassword());
                preparedStatement.setString(3, client.getClientName());

                int changedRows = preparedStatement.executeUpdate();

                return changedRows == 1;

            } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
