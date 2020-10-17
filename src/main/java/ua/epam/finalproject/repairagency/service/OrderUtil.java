package ua.epam.finalproject.repairagency.service;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.model.Order;
import ua.epam.finalproject.repairagency.model.Status;
import ua.epam.finalproject.repairagency.repository.EntityContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class OrderUtil {

    private static final Logger Log = Logger.getLogger(OrderUtil.class);

    public static Order getFromData(int id, int createdByClientId, int deviceId, String description,
                                    int masterId, int managerId, double cost, String statusStr, String createdDate) {
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
        Log.trace("Obtained order with id : " + order.getId());
        return order;
    }

    public static String getFromDate(String need) {
        Log.debug("Start getFromDate with param : " + need);
        LocalDate today = LocalDate.now();
        LocalDate needDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy");
        switch (need) {
            case "now":
                needDate = today;
                break;
            case "monthStart":
                needDate = today.with(TemporalAdjusters.firstDayOfMonth());
                break;
            case "weekStart":
                needDate = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                break;
            default:
                Log.error("Param is incorrect");
                throw new AppException("Internal server error");
        }
        return formatter.format(needDate);
    }

    public static String format(String dataStr) {
        //from = 17 октября, 2020
        //need return: 2020-10-17
        if(dataStr.equals("17 октября, 2020")) {
            dataStr = "2020-10-17";
        }
        if(dataStr.equals("1 октября, 2020")) {
            dataStr = "2020-10-1";
        }
        if(dataStr.equals("12 октября, 2020")) {
            dataStr = "2020-10-12";
        }
        if(dataStr.equals("1 August, 2020")) {
            dataStr = "2020-07-01";
        }

        return dataStr;
    }
}
