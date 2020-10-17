package ua.epam.finalproject.repairagency.web.filter;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.service.OrderUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebFilter(urlPatterns = {"/controller"})
public class SimpleRequestFilter implements Filter {

    private static final Logger Log = Logger.getLogger(SimpleRequestFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Log.trace("Start doFilter");
        String from = servletRequest.getParameter("fromDate");
        String to = servletRequest.getParameter("toDate");

        Log.debug("Got parameters : fromDate : " + from + " and toDate " + to);

        if(!StringUtils.isEmpty(from) && !StringUtils.isEmpty(to)) {
            from = format(from);
            to = format(to);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            from = formatter.format(LocalDate.ofYearDay(2020, 1));
            to = formatter.format(LocalDate.now());
        }

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        Log.debug("to session: from : " + from + " to + " + to);
        session.setAttribute("from", from);
        session.setAttribute("to", to);

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String format(String dateParamStr) {
        StringBuilder result = new StringBuilder();
        Pattern pattern = Pattern.compile("\\d{4}");
        Matcher matcher = pattern.matcher(dateParamStr);
        if(matcher.find()) {
            result.append(matcher.group());
        }
        pattern = Pattern.compile("\\b[a-zA-Zа-яА-Я]{3,}");
        matcher = pattern.matcher(dateParamStr);
        if(matcher.find()) {
            String month = getMonth(matcher.group());
            result.append(month);
        }
        pattern = Pattern.compile("^\\d{1,2}\\b");
        matcher = pattern.matcher(dateParamStr);
        if(matcher.find()) {
            String dayStr = matcher.group();
            if(dayStr.length() == 1) {
                dayStr = "0" + dayStr;
            }
            result.append(dayStr);
        }

        return result.toString();
    }

//    public static void main(String[] args) {
//        SimpleRequestFilter filter = new SimpleRequestFilter();
//        String string = filter.format("1 October, 2020");
//        System.out.println(filter.format(string);
//    }

    private String getMonth(String month) {
        switch (month) {
            case "January":
            case "января":
                return "-01-";
            case "February":
            case "февраля":
                return "-02-";
            case "March":
            case "марта":
                return "-03-";
            case "April":
            case "апреля":
                return "-04-";
            case "May":
            case "мая":
                return "-05-";
            case "June":
            case "июня":
                return "-06-";
            case "July":
            case "июля":
                return "-07-";
            case "August":
            case "августа":
                return "-08-";
            case "September":
            case "сентября":
                return "-09-";
            case "October":
            case "октября":
                return "-10-";
            case "November":
            case "ноября":
                return "-11-";
            case "December":
            case "декабря":
                return "-12-";
            default:
                Log.error("Incorrect input date");
                throw new AppException("Incorrect entered date");
        }
    }

    @Override
    public void destroy() {

    }
}
