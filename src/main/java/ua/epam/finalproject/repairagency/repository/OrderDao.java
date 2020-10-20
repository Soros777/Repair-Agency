package ua.epam.finalproject.repairagency.repository;

import ua.epam.finalproject.repairagency.model.Order;
import ua.epam.finalproject.repairagency.model.Status;
import ua.epam.finalproject.repairagency.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface OrderDao {

    boolean saveOrder(Connection connection, int clientId, int deviceId, String description) throws SQLException;

    List<Order> findAllOrders(Connection connection) throws SQLException;

    List<Order> findForPeriod(Connection connection, String from, String to) throws SQLException;

    Order setCost(Connection connection, Order order, double orderCost) throws SQLException;

    void setManager(Connection connection, Order order, int managerId) throws SQLException;

    Order changeStatus(Connection connection, Order order, Status newStatus) throws SQLException;

    void setMaster(Connection connection, Order order, int masterId) throws SQLException;
}
