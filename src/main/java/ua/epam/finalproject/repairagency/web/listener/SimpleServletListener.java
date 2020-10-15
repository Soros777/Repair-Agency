package ua.epam.finalproject.repairagency.web.listener;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.repository.ConnectionPool;
import ua.epam.finalproject.repairagency.repository.EntityContainer;

import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.SQLException;

@WebListener
public class SimpleServletListener implements ServletContextListener {

    private static final Logger Log = Logger.getLogger(SimpleServletListener.class);


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Log.trace("Start init servlet context");
        Connection connection;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            EntityContainer.fillContainers(connection);
            Log.trace("Containers was fel successfully.");
        } catch (SQLException | NamingException e) {
            Log.error("Can't obtain connection.");
            throw new AppException("Internal server error.");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
