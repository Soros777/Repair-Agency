package ua.epam.finalproject.repairagency.service;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.model.Order;
import ua.epam.finalproject.repairagency.model.Status;
import ua.epam.finalproject.repairagency.repository.EntityContainer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class OrderUtil {

    private static final Logger Log = Logger.getLogger(OrderUtil.class);

    public static Order getFromData(int id, int createdByClientId, int deviceId, String description,
                                    int masterId, int managerId, double cost, String statusStr, String createdDate, boolean getPay) {
        Log.trace("Start obtain order from data");
        Order order = new Order();
        order.setId(id);
        order.setCreatedByClient(EntityContainer.getClientById(createdByClientId));
        order.setDevice(EntityContainer.getDeviceById(deviceId));
        order.setDescription(description);
        order.setMaster(EntityContainer.getPersonalById(masterId));
        order.setManager(EntityContainer.getPersonalById(managerId));
        order.setCost(cost);
        order.setStatus(Status.fromString(statusStr));
        order.setCreatedDate(LocalDate.parse(createdDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        order.setGetPay(getPay);
        Log.trace("Obtained order with id : " + order.getId());
        return order;
    }
}
