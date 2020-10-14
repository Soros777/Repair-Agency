package ua.epam.finalproject.repairagency.web.listener;

import org.apache.log4j.Logger;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

@WebListener
public class SimpleRequestListener implements ServletRequestListener {

    private static final Logger Log = Logger.getLogger(SimpleRequestListener.class);

    @Override
    public void requestDestroyed(ServletRequestEvent event) {
        HttpServletRequest request = (HttpServletRequest) event.getServletRequest();
        String id = request.getRequestedSessionId();
        Log.trace("Destroy the request with id " + id);
    }

    @Override
    public void requestInitialized(ServletRequestEvent event) {
        HttpServletRequest request = (HttpServletRequest) event.getServletRequest();
        String uri = "Request initialized for " + request.getRequestURI();
        String requestId = "Request initialized with id = " + request.getRequestedSessionId();
        Log.trace(uri + "\n" + requestId);
    }
}
