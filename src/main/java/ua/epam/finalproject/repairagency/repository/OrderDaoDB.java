package ua.epam.finalproject.repairagency.repository;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.model.Order;
import ua.epam.finalproject.repairagency.service.OrderUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<Order> findAllOrders(Connection connection) throws SQLException {
        Log.debug("Start find orders");
        List<Order> orders = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM orders");
            while (resultSet.next()) {
                Order order = OrderUtil.getFromData(resultSet.getInt("id"),
                        resultSet.getInt("created_by_client_id"), resultSet.getInt("device_id"),
                        resultSet.getString("description"), resultSet.getInt("master_id"),
                        resultSet.getInt("manager_id"), resultSet.getDouble("cost"),
                        resultSet.getString("status"), resultSet.getString("created_date"));
                orders.add(order);
            }
        } catch (SQLException e) {
            Log.error("Can't save order cause " + e);
            RepositoryUtil.closeAndThrow(e, resultSet, statement);
        }
        Log.trace("Order list is : " + orders);
        return orders;
    }
}
