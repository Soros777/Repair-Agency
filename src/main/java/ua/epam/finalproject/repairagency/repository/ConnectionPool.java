package ua.epam.finalproject.repairagency.repository;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {

    private static ConnectionPool instance;
    private static String SOURCE_NAME = "java:comp/env/jdbc/repairAgency";
    private Context context = new InitialContext();
    private DataSource dataSource = (DataSource) context.lookup(SOURCE_NAME);

    private ConnectionPool() throws NamingException {}

    public static ConnectionPool getInstance() throws NamingException {
        if(instance == null) {
            synchronized (ConnectionPool.class) {
                if(instance == null) {
                    instance = new ConnectionPool();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
