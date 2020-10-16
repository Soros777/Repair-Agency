package ua.epam.finalproject.repairagency.customTag;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.model.Order;
import ua.epam.finalproject.repairagency.model.Status;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class OrdersInfoTag extends TagSupport {

    private List<Order> orders;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private String needInfo;
    private static final Logger Log = Logger.getLogger(OrdersInfoTag.class);

    public void setNeedInfo(String needInfo) {
        this.needInfo = needInfo;
    }

    @Override
    public int doStartTag() throws JspException {
        Log.trace("Start doTag");
        orders = (List<Order>) pageContext.getSession().getAttribute("orders");
        dateFrom = (LocalDate) pageContext.getRequest().getAttribute("filterOrderFrom");
        dateTo = (LocalDate) pageContext.getRequest().getAttribute("filterOrderTo");

        long result;
        if(!needInfo.equals("all")) {
            result = orders.stream()
                    .filter(o -> !o.getCreatedDate().isAfter(dateTo))
                    .filter(o -> !o.getCreatedDate().isBefore(dateFrom))
                    .filter(o -> o.getStatus() == Status.fromString(needInfo))
                    .count();
        } else {
            result = orders.stream()
                    .filter(o -> !o.getCreatedDate().isAfter(dateTo))
                    .filter(o -> !o.getCreatedDate().isBefore(dateFrom))
                    .count();
        }

        try {
            JspWriter out = pageContext.getOut();
            out.write(String.valueOf(result));
        } catch (IOException e) {
            Log.error("Can't write in the out cause : " + e);
            throw new AppException("Internal server error");
        }

        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }
}
