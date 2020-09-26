package ua.epam.finalproject.repairagency.dao;

import org.slf4j.Logger;

import java.sql.*;

import static org.slf4j.LoggerFactory.getLogger;

public class DBManager {

    private final String connectionUrl = "jdbc:mysql://localhost:3306/repairagency?serverTimezone=UTC&user=root&password=root";
    private static DBManager dbManager;
    private static final Logger log = getLogger(DBManager.class);

    private DBManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static DBManager getInstance() {
        if(dbManager == null) {
            dbManager = new DBManager();
        }
        return dbManager;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionUrl);
    }

    public boolean insertNewClient(String login, String password) {
        try {
            Connection connection = getConnection();
//            PreparedStatement preparedStatement = connection.prepareStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
