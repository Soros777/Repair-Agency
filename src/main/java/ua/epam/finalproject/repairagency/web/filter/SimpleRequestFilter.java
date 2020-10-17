package ua.epam.finalproject.repairagency.web.filter;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.service.OrderUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

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
        String to = servletRequest.getParameter("fromDate");

        if(!StringUtils.isEmpty(from) && !StringUtils.isEmpty(to)) {
            from = format(from);
            to = format(to);
            servletRequest.setAttribute("from", from);
            servletRequest.setAttribute("to", to);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String format(String paramString) {
        String result = paramString;
        switch (paramString) {
            case "1 August, 2020":
                result = "1 августа, 2020";
                break;
        }
        return result;
    }

    @Override
    public void destroy() {

    }
}
