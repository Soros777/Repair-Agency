package ua.epam.finalproject.repairagency.web.listener;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.repository.ConnectionPool;
import ua.epam.finalproject.repairagency.repository.EntityContainer;

import javax.naming.NamingException;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.sql.Connection;
import java.sql.SQLException;

@WebListener
public class SimpleSessionListener implements HttpSessionListener {

    private static final Logger Log = Logger.getLogger(SimpleSessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        Log.trace("Start create session");

        ConnectionPool connectionPool;
        try {
            connectionPool = ConnectionPool.getInstance();
            Connection connection = connectionPool.getConnection();
            EntityContainer.fillContainers(connection);
            Log.trace("Containers was fel successfully.");
        } catch (SQLException | NamingException e) {
            Log.error("Can't obtain connection.");
            throw new AppException("Internal server error.");
        }

        HttpSession session = httpSessionEvent.getSession();
        session.setAttribute("connectionPool", connectionPool);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        Log.trace("Session destroyed");
    }
}
