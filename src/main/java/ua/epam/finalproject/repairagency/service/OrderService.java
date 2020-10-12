package ua.epam.finalproject.repairagency.service;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.repository.ConnectionPool;
import ua.epam.finalproject.repairagency.repository.DeviceDao;
import ua.epam.finalproject.repairagency.repository.OrderDao;
import ua.epam.finalproject.repairagency.repository.OrderDaoDB;
import ua.epam.finalproject.repairagency.web.command.RegisterCommand;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;

public class OrderService {

    private OrderDao orderDao;
    private DeviceDao deviceDao;

    private static final Logger Log = Logger.getLogger(OrderService.class);

    public OrderService(OrderDao orderDao, DeviceDao deviceDao) {
        this.orderDao = orderDao;
        this.deviceDao = deviceDao;
    }

    public boolean createOrder(int clientId, String deviceStr, String description) {
        Log.trace("Start OrderService");

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
                } catch (SQLException ex) { } // just about close connection
            }
        } catch (NamingException e) {
            Log.error("Can't get instance of Connection Pool cause : " + e);
            throw new AppException("Can't get instance of Connection Pool", e);
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) { }
            }
        }
        Log.error("Can't create order");
        throw new AppException("Can't create order");
    }
}
