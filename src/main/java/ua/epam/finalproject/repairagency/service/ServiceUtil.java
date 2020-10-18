package ua.epam.finalproject.repairagency.service;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class ServiceUtil {

    private static final Logger Log = Logger.getLogger(ServiceUtil.class);

    public static void rollback(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                Log.error("Can't rollback connection");
            }
        }
    }

    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                Log.error("Can't close connection");
            }
        }
    }
}
