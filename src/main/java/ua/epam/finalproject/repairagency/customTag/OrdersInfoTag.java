package ua.epam.finalproject.repairagency.customTag;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.model.Order;
import ua.epam.finalproject.repairagency.model.Status;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class OrdersInfoTag extends TagSupport {

    private List<Order> allOrders;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private String needInfo;
    private static final Logger Log = Logger.getLogger(OrdersInfoTag.class);

    public void setNeedInfo(String needInfo) {
        this.needInfo = needInfo;
    }

    @Override
    public int doStartTag() throws JspException {
        Log.trace("Start do Tag");
        allOrders = (List<Order>) pageContext.getSession().getAttribute("orders");
        String dateFromStr = (String) pageContext.getSession().getAttribute("from");
        String dateToStr = (String) pageContext.getSession().getAttribute("to");
        dateFrom = LocalDate.parse(dateFromStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        dateTo = LocalDate.parse(dateToStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        List<Order> specificOrders;
        if(!needInfo.equals("all")) {
            specificOrders = allOrders.stream()
                    .filter(o -> !o.getCreatedDate().isAfter(dateTo))
                    .filter(o -> !o.getCreatedDate().isBefore(dateFrom))
                    .filter(o -> o.getStatus() == Status.fromString(needInfo))
                    .collect(Collectors.toList());
        } else {
            specificOrders = allOrders.stream()
                    .filter(o -> !o.getCreatedDate().isAfter(dateTo))
                    .filter(o -> !o.getCreatedDate().isBefore(dateFrom))
                    .collect(Collectors.toList());
        }
        try {
            JspWriter out = pageContext.getOut();
            out.write(String.valueOf(specificOrders.size()));
        } catch (IOException e) {
            Log.error("Can't write in the out cause : " + e);
            throw new AppException("Internal server error");
        }
        HttpSession session = pageContext.getSession();
        session.setAttribute(needInfo + "Orders", specificOrders);

        Log.trace("Do Tag finishes");
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }
}
