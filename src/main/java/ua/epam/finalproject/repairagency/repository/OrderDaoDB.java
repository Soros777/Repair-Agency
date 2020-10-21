package ua.epam.finalproject.repairagency.repository;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.model.Order;
import ua.epam.finalproject.repairagency.model.Status;
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
            RepositoryUtil.closeAndThrow(e, preparedStatement);
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
            fillList(resultSet, orders);
        } catch (SQLException e) {
            Log.error("Can't save order cause " + e);
            RepositoryUtil.closeAndThrow(e, resultSet, statement);
        }
        Log.trace("Order list size is : " + orders.size());
        return orders;
    }

    @Override
    public List<Order> findForPeriod(Connection connection, String from, String to) throws SQLException {
        Log.debug("Start find orders");
        List<Order> orders = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM orders WHERE created_date BETWEEN ? and ?");
            preparedStatement.setString(1, from);
            preparedStatement.setString(2, to);
            Log.debug("=================== preparedStatement : " + preparedStatement);
            resultSet = preparedStatement.executeQuery();
            fillList(resultSet, orders);
        } catch (SQLException e) {
            Log.error("Can't save order cause " + e);
            RepositoryUtil.closeAndThrow(e, resultSet, preparedStatement);
        }
        Log.trace("Order list size is : " + orders.size());
        return orders;
    }

    @Override
    public Order setCost(Connection connection, Order order, double orderCost) throws SQLException {
        Log.trace("Start set cost");
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("UPDATE orders SET cost=? WHERE id=?");
            preparedStatement.setDouble(1, orderCost);
            preparedStatement.setInt(2, order.getId());
            if(preparedStatement.executeUpdate() == 1) {
                order.setCost(orderCost);
                Log.debug("Order cost updated successfully");
                return order;
            }
        } catch (SQLException e) {
            Log.error("Can't set order cost cause " + e);
            RepositoryUtil.closeAndThrow(e, preparedStatement);
        }
        Log.error("Can't set order cost");
        throw new AppException("Can't set order cost");
    }

    @Override
    public void setManager(Connection connection, Order order, int managerId) throws SQLException {
        Log.trace("Start set manager");

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("UPDATE orders SET manager_id=? WHERE id=?");
            preparedStatement.setInt(1, managerId);
            preparedStatement.setInt(2, order.getId());
            if(preparedStatement.executeUpdate() == 1) {
                Log.debug("Order manager updated successfully");
            }
        } catch (SQLException e) {
            Log.error("Can't set order manager cause " + e);
            RepositoryUtil.closeAndThrow(e, preparedStatement);
        }
    }

    @Override
    public Order changeStatus(Connection connection, Order order, Status newStatus) throws SQLException {
        Log.trace("Start change status");
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("UPDATE orders SET status=? WHERE id=?");
            preparedStatement.setString(1, newStatus.toString());
            preparedStatement.setInt(2, order.getId());
            if(preparedStatement.executeUpdate() == 1) {
                order.setStatus(newStatus);
                Log.debug("Order status updated successfully");
                return order;
            }
        } catch (SQLException e) {
            Log.error("Can't set order cost cause " + e);
            RepositoryUtil.closeAndThrow(e, preparedStatement);
        }
        Log.error("Can't change order status");
        throw new AppException("Can't change order status");
    }

    @Override
    public void setMaster(Connection connection, Order order, int masterId) throws SQLException {
        Log.trace("Start set master");
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("UPDATE orders SET master_id=? WHERE id=?");
            preparedStatement.setInt(1, masterId);
            preparedStatement.setInt(2, order.getId());
            if(preparedStatement.executeUpdate() == 1) {
                Log.debug("Master set successfully");
            }
        } catch (SQLException e) {
            Log.error("Can't set order cost cause " + e);
            RepositoryUtil.closeAndThrow(e, preparedStatement);
        }
    }

    @Override
    public List<Order> findForClient(Connection connection, int clientId) throws SQLException {
        Log.trace("Start obtain all client orders");
        List<Order> clientOrders = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM orders WHERE created_by_client_id=?");
            preparedStatement.setInt(1, clientId);
            resultSet = preparedStatement.executeQuery();
            fillList(resultSet, clientOrders);
        } catch (SQLException e) {
            Log.error("Can't obtain client orders cause " + e);
            RepositoryUtil.closeAndThrow(e, preparedStatement, resultSet);
        }
        Log.trace("Client order list size is : " + clientOrders.size());
        return clientOrders;
    }

    @Override
    public void payOrder(Connection connection, int orderId) throws SQLException {
        Log.trace("Start pay order");

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("UPDATE orders SET get_pay=1 WHERE id=?");
            preparedStatement.setInt(1, orderId);
            if(preparedStatement.executeUpdate() == 1) {
                Log.debug("Order field \'get_pay\' updated");
            } else {
                Log.error("Can't update order field");
                throw new AppException("Can't pay order");
            }
        } catch (SQLException e) {
            Log.error("Can't pay order cause " + e);
            RepositoryUtil.closeAndThrow(e, preparedStatement);
        }
    }

    @Override
    public Order findById(Connection connection, int orderId) throws SQLException {
        Log.trace("Start find by id");
        List<Order> orders = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM orders WHERE id=?");
            preparedStatement.setInt(1, orderId);
            resultSet = preparedStatement.executeQuery();
            fillList(resultSet, orders);
        } catch (SQLException e) {
            Log.error("Can't pay order cause " + e);
            RepositoryUtil.closeAndThrow(e, preparedStatement, resultSet);
        }
        return orders.get(0);
    }

    private void fillList(ResultSet resultSet, List<Order> orderList) throws SQLException {
        while (resultSet.next()) {
            Order order = OrderUtil.getFromData(resultSet.getInt("id"),
                    resultSet.getInt("created_by_client_id"), resultSet.getInt("device_id"),
                    resultSet.getString("description"), resultSet.getInt("master_id"),
                    resultSet.getInt("manager_id"), resultSet.getDouble("cost"),
                    resultSet.getString("status"), resultSet.getString("created_date"),
                    resultSet.getBoolean("get_pay"));
            orderList.add(order);
        }
    }
}
