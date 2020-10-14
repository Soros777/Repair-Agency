package ua.epam.finalproject.repairagency.web.listener;

import org.apache.log4j.Logger;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

@WebListener
public class SessionListenerImpl implements HttpSessionAttributeListener {

    private static final Logger Log = Logger.getLogger(SessionListenerImpl.class);

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        Log.trace("add: " + event.getClass().getSimpleName() + " : " +
                event.getName() + " : " + event.getValue());
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        Log.trace("remove: " + event.getClass().getSimpleName() + " : " +
                event.getName() + " : " + event.getValue());
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        Log.trace("replace: " + event.getClass().getSimpleName() + " : " +
                event.getName() + " : " + event.getValue());
    }
}
