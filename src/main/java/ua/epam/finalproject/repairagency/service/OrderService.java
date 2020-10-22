package ua.epam.finalproject.repairagency.service;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.model.Client;
import ua.epam.finalproject.repairagency.model.Order;
import ua.epam.finalproject.repairagency.model.Status;
import ua.epam.finalproject.repairagency.model.User;
import ua.epam.finalproject.repairagency.repository.ConnectionPool;
import ua.epam.finalproject.repairagency.repository.DeviceDao;
import ua.epam.finalproject.repairagency.repository.EntityContainer;
import ua.epam.finalproject.repairagency.repository.OrderDao;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class OrderService {

    private OrderDao orderDao;
    private DeviceDao deviceDao;

    private static final Logger Log = Logger.getLogger(OrderService.class);

    public OrderService(OrderDao orderDao, DeviceDao deviceDao) {
        this.orderDao = orderDao;
        this.deviceDao = deviceDao;
    }

    public boolean createOrder(int clientId, String deviceStr, String description) {
        Log.trace("Start create order");

        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            Log.debug("Connection is obtained, go to DB");

            int deviceId = deviceDao.getDeviceId(connection, deviceStr);

            if(deviceId != -1 && orderDao.saveOrder(connection, clientId, deviceId, description)) {
                connection.commit();
                Log.debug("Success! New == order == is in DB.");
                return true;
            }
        } catch (SQLException e) {
            ServiceUtil.rollback(connection);
        } catch (NamingException e) {
            Log.error("Can't get instance of Connection Pool cause : " + e);
            throw new AppException("Can't get instance of Connection Pool", e);
        } finally {
            ServiceUtil.close(connection);
        }
        Log.error("Can't create order");
        throw new AppException("Can't create order");
    }

    public List<Order> findAllOrders() {
        Log.trace("Start find all orders");
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            return orderDao.findAllOrders(connection);
        } catch (SQLException e) {
            ServiceUtil.rollback(connection);
        } catch (NamingException e) {
            Log.error("Can't get instance of Connection Pool cause : " + e);
            throw new AppException("Can't get instance of Connection Pool", e);
        } finally {
            ServiceUtil.close(connection);
        }
        Log.error("Can't find all orders");
        throw new AppException("Can't find orders");
    }

    public List<Order> findForPeriod(String from, String to) {
        Log.debug("Start find for period");
        from = from + " 00:00:00";
        to = to + " 23:59:59";

        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            return orderDao.findForPeriod(connection, from, to);
        } catch (SQLException e) {
            ServiceUtil.rollback(connection);
        } catch (NamingException e) {
            Log.error("Can't get instance of Connection Pool cause : " + e);
            throw new AppException("Internal server error");
        } finally {
            ServiceUtil.close(connection);
        }
        Log.error("Can't find orders from period");
        throw new AppException("Can't find orders");
    }

    public List<Order> filterMaster(List<Order> orderList, String masterName) {
        Log.debug("Start filter orderList for master");

        return orderList.stream()
                .filter(order -> order.getMaster() != null && order.getMaster().getPersonName().equals(masterName))
                .collect(Collectors.toList());

    }

    public Order setOrderCost(Order order, String orderCost, User manager) {
        Log.trace("Start set order cost");

        double newValue = Double.parseDouble(orderCost);
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            Order updatedOrder = orderDao.setCost(connection, order, newValue);
            if(updatedOrder.getManager() == null) {
                orderDao.setManager(connection, order, manager.getId());
            }
            updatedOrder.setManager(manager);
            connection.commit();
            return updatedOrder;
        } catch (SQLException e) {
            ServiceUtil.rollback(connection);
        } catch (NamingException e) {
            Log.error("Can't get instance of Connection Pool cause : " + e);
            throw new AppException("Internal server error");
        } finally {
            ServiceUtil.close(connection);
        }

        Log.error("Can't set order cost");
        throw new AppException("Can't set order cost");
    }

    public Order changeStatus(Order order, String newOrderStatus) {
        Log.trace("Start change order status");

        Status newStatus = Status.fromString(newOrderStatus);
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            Order updatedOrder = orderDao.changeStatus(connection, order, newStatus);
            connection.commit();
            return updatedOrder;
        } catch (SQLException e) {
            ServiceUtil.rollback(connection);
        } catch (NamingException e) {
            Log.error("Can't get instance of Connection Pool cause : " + e);
            throw new AppException("Internal server error");
        } finally {
            ServiceUtil.close(connection);
        }

        Log.error("Can't change order status");
        throw new AppException("Can't change order status");
    }

    public Order appointMaster(Order order, String masterName) {
        Log.trace("Start appoint master");
        User master = EntityContainer.getPersonalByName(masterName);

        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            orderDao.setMaster(connection, order, master.getId());
            order.setMaster(master);
            connection.commit();
            return order;
        } catch (SQLException e) {
            ServiceUtil.rollback(connection);
        } catch (NamingException e) {
            Log.error("Can't get instance of Connection Pool cause : " + e);
            throw new AppException("Internal server error");
        } finally {
            ServiceUtil.close(connection);
        }
        Log.error("Can't appoint master");
        throw new AppException("Can't appoint master");
    }

    public List<Order> findForClient(Client client, ConnectionPool connectionPool) {
        Log.trace("Start find all client orders");

        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            return orderDao.findForClient(connection, client.getId());
        } catch (SQLException e) {
            ServiceUtil.rollback(connection);
        } finally {
            ServiceUtil.close(connection);
        }
        Log.error("Can't find client orders");
        throw new AppException("Can't find client orders");
    }

    public void payOrder(String orderId, Connection connection) throws SQLException {
        Log.trace("Go to order Dao");
        orderDao.payOrder(connection, Integer.parseInt(orderId));
    }

    public Order findById(Connection connection, String orderId) throws SQLException {
        Log.trace("Go to order Dao");
        return orderDao.findById(connection, Integer.parseInt(orderId));
    }

    public List<Order> findForMaster(String masterId, ConnectionPool connectionPool) {
        Log.trace("Start find orders for master");

        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            return orderDao.findForMaster(connection, Integer.parseInt(masterId));
        } catch (SQLException e) {
            ServiceUtil.rollback(connection);
        } finally {
            ServiceUtil.close(connection);
        }
        Log.error("Can't find orders for master");
        throw new AppException("Can't find orders for master");
    }

    public Order findById(ConnectionPool connectionPool, String orderId) {
        Log.trace("Start find by id");

        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            return findById(connection, orderId);
        } catch (SQLException e) {
            ServiceUtil.rollback(connection);
        } finally {
            ServiceUtil.close(connection);
        }
        Log.error("Can't find by id");
        throw new AppException("Can't find order by id");
    }

    public void addFeedBack(ConnectionPool connectionPool, String orderId, String feedbackText) {

    }
}
