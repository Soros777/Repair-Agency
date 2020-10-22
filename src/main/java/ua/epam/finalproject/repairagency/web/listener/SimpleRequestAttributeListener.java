package ua.epam.finalproject.repairagency.web.listener;

import org.apache.log4j.Logger;

import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class SimpleRequestAttributeListener implements ServletRequestAttributeListener {

    private static final Logger Log = Logger.getLogger(SimpleRequestAttributeListener.class);

    @Override
    public void attributeAdded(ServletRequestAttributeEvent servletRequestAttributeEvent) {
        String attributeName = servletRequestAttributeEvent.getName();
        String attributeClassName = servletRequestAttributeEvent.getValue().getClass().getSimpleName();
        Log.trace("Added request attribute : " + attributeName + ", class : " + attributeClassName);
    }

    @Override
    public void attributeRemoved(ServletRequestAttributeEvent servletRequestAttributeEvent) {
        String attributeName = servletRequestAttributeEvent.getName();
        String attributeClassName = servletRequestAttributeEvent.getValue().getClass().getSimpleName();
        Log.trace("Removed request attribute : " + attributeName + ", class : " + attributeClassName);
    }

    @Override
    public void attributeReplaced(ServletRequestAttributeEvent servletRequestAttributeEvent) {
        String attributeName = servletRequestAttributeEvent.getName();
        String attributeClassName = servletRequestAttributeEvent.getValue().getClass().getSimpleName();
        Log.trace("Replaced request attribute : " + attributeName + ", class : " + attributeClassName);
    }
}
