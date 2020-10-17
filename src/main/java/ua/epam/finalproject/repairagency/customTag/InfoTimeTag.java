package ua.epam.finalproject.repairagency.customTag;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class InfoTimeTag extends TagSupport {

    private static String DATE_FORMAT = "d MMMM, yyyy";
    private String needDate;
    private static final Logger Log = Logger.getLogger(InfoTimeTag.class);

    public void setNeedDate(String needDate) {
        this.needDate = needDate;
    }

    @Override
    public int doStartTag() throws JspException {
        LocalDate outDate = null;
        LocalDate localDateNow = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        switch (needDate) {
            case "now":
                outDate = localDateNow;
                break;
            case "monthStart":
                outDate = localDateNow.with(TemporalAdjusters.firstDayOfMonth());
                break;
            case "weekStart":
                outDate = localDateNow.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                break;
            default:
                Log.error("Not valid value in tag info-date");
                return SKIP_BODY;
        }

        String outStr = formatter.format(outDate);
        try {
            JspWriter out = pageContext.getOut();
            out.write(outStr);
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
