package ua.epam.finalproject.repairagency.repository;

import java.sql.Connection;
import java.sql.SQLException;

public interface OrderDao {

    boolean saveOrder(Connection connection, int clientId, int deviceId, String description) throws SQLException;
}
