package ua.epam.finalproject.repairagency.dao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {

    private static ConnectionPool instance;
    private Context context = new InitialContext();
    private DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/repairAgency");

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
