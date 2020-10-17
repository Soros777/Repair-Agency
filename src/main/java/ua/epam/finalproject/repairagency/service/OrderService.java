package ua.epam.finalproject.repairagency.service;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.model.Order;
import ua.epam.finalproject.repairagency.repository.ConnectionPool;
import ua.epam.finalproject.repairagency.repository.DeviceDao;
import ua.epam.finalproject.repairagency.repository.OrderDao;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
            if(connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    Log.error("Can't rollback connection");
                }
            }
        } catch (NamingException e) {
            Log.error("Can't get instance of Connection Pool cause : " + e);
            throw new AppException("Can't get instance of Connection Pool", e);
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Log.error("Can't close connection");
                }
            }
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
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    Log.error("Can't rollback connection");
                }
            }
        } catch (NamingException e) {
            Log.error("Can't get instance of Connection Pool cause : " + e);
            throw new AppException("Can't get instance of Connection Pool", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Log.error("Can't close connection");
                }
            }
        }
        Log.error("Can't find all orders");
        throw new AppException("Can't find orders");
    }

    public List<Order> findForPeriod(String from, String to) {
        Log.debug("Start find for period");

        // преобразовать для БД
        from = OrderUtil.format(from);
        to = OrderUtil.format(to);

        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            return orderDao.findForPeriod(connection, from, to);
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    Log.error("Can't rollback connection");
                }
            }
        } catch (NamingException e) {
            Log.error("Can't get instance of Connection Pool cause : " + e);
            throw new AppException("Can't get instance of Connection Pool", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Log.error("Can't close connection");
                }
            }
        }
        Log.error("Can't find orders from period");
        throw new AppException("Can't find orders");
    }
}
