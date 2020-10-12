package ua.epam.finalproject.repairagency.repository;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderDaoDB implements OrderDao {

    private static final Logger Log = Logger.getLogger(OrderDaoDB.class);

    @Override
    public boolean saveOrder(Connection connection, int clientId, int deviceId, String description) throws SQLException {
        Log.debug("Start save order");

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO orders (created_by_client_id, device_id, description) VALUES (?, ?, ?)");
            int k = 1;
            preparedStatement.setInt(k++, clientId);
            preparedStatement.setInt(k++, deviceId);
            preparedStatement.setString(k, description);
            Log.debug("Go to DB with preparedStatement " + preparedStatement);
            if(preparedStatement.executeUpdate() == 1) {
                Log.trace("Successfully added order");
                return true;
            }
        } catch (SQLException e) {
            Log.error("Can't save order cause " + e);
            if(preparedStatement != null) {
                preparedStatement.close();
            }
            throw new SQLException(e);
        }
        Log.error("Can't save order");
        throw new AppException("Can't save order");
    }
}
