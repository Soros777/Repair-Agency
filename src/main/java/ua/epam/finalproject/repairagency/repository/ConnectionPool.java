package ua.epam.finalproject.repairagency.repository;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {

    private static ConnectionPool instance;

    /* Just for testing. After that need return

    private Context context = new InitialContext();
    private DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/repairAgency");

     */

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

  //////// After testing need to be deleted
        Context context;
        DataSource dataSource = null;
        try {
            context = new InitialContext();
            dataSource = (DataSource) context.lookup("java:comp/env/jdbc/repairAgency");
        } catch (NamingException e) {
            e.printStackTrace();
        }
  ///////////////////////////////////

        return dataSource.getConnection();
    }
}
