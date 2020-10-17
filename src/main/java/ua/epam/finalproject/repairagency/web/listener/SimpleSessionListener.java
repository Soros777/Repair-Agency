package ua.epam.finalproject.repairagency.web.listener;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.model.Order;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import java.util.List;

@WebListener
public class SimpleSessionListener implements HttpSessionAttributeListener {

    private static final Logger Log = Logger.getLogger(SimpleSessionListener.class);

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        if(event.getName().equals("orders")) {
            List<Order> orderList = (List<Order>) event.getValue();
            Log.trace("add: " + event.getClass().getSimpleName() + " : " +
                    event.getName());
            Log.trace("orders.size is : " + orderList.size());
        } else {
            Log.trace("add: " + event.getClass().getSimpleName() + " : " +
                    event.getName() + " : " + event.getValue());
        }
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        Log.trace("remove: " + event.getClass().getSimpleName() + " : " +
                event.getName() + " : " + event.getValue());
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        if(event.getName().equals("orders")) {
            List<Order> orderList = (List<Order>) event.getValue();
            Log.trace("add: " + event.getClass().getSimpleName() + " : " +
                    event.getName());
            Log.trace("orders.size is : " + orderList.size());
        } else {
            Log.trace("add: " + event.getClass().getSimpleName() + " : " +
                    event.getName() + " : " + event.getValue());
        }
    }
}
