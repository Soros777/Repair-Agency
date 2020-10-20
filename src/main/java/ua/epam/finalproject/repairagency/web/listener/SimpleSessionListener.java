package ua.epam.finalproject.repairagency.web.listener;

import org.apache.log4j.Logger;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import java.util.List;

@WebListener
public class SimpleSessionListener implements HttpSessionAttributeListener {

    private static final Logger Log = Logger.getLogger(SimpleSessionListener.class);

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        if(event.getValue() instanceof List<?>) {
            Log.trace("Add : " + event.getClass().getSimpleName() + " : " + event.getName() +
                    " with size : " + ((List<?>) event.getValue()).size());
        } else {
            Log.trace("Add : " + event.getClass().getSimpleName() + " : " + event.getName() + " : " + event.getValue());
        }
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        if(event.getValue() instanceof List<?>) {
            Log.trace("Replace : " + event.getClass().getSimpleName() + " : " + event.getName() +
                    " with size : " + ((List<?>) event.getValue()).size());
        } else {
            Log.trace("Replace : " + event.getClass().getSimpleName() + " : " + event.getName() + " : " + event.getValue());
        }
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {  }


}
