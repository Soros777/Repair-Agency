package ua.epam.finalproject.repairagency.repository;

import ua.epam.finalproject.repairagency.model.Order;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface OrderDao {

    boolean saveOrder(Connection connection, int clientId, int deviceId, String description) throws SQLException;

    List<Order> findAllOrders(Connection connection) throws SQLException;

    List<Order> findForPeriod(Connection connection, String from, String to) throws SQLException;
}
