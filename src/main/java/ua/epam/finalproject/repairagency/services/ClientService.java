package ua.epam.finalproject.repairagency.services;

import ua.epam.finalproject.repairagency.dao.DBManager;
import ua.epam.finalproject.repairagency.model.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
                Locale userLocale = Locale.forLanguageTag(resultSet.getString("locale"));
                LocalDate userRegistrationDate = LocalDate.parse(resultSet.getString("registration_date"));
                Client client = null;
                if(clientEmailFromRequest.equals(clientMailFromDB)) {
                    client = Client.getClientWithInitParams(
                            resultSet.getInt("id"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getString("client_name"),
                            Double.parseDouble(resultSet.getString("wallet_count")),
                            resultSet.getString("contact_phone"),
                            userLocale,
                            userRegistrationDate);
                }
                return client;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static void main(String[] args) {
//        DBManager dbManager = DBManager.getInstance();
//
//        try {
//            Connection connection = dbManager.getConnection();
//            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM clients");
//
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()) {
//                System.out.println("Client : " + resultSet.getString("client_name"));
//
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}
