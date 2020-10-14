package ua.epam.finalproject.repairagency.web.filter;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.model.Role;
import ua.epam.finalproject.repairagency.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"authorizedPage.jsp"},
        initParams = {@WebInitParam(name="INDEX_PATH", value = "/index.jsp")})
public class AuthorizeFilter implements Filter {

    private String indexPath;
    private static final Logger Log = Logger.getLogger(AuthorizeFilter.class);

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        Log.trace("Filter initialization starts");
        indexPath = fConfig.getInitParameter("INDEX_PATH");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Log.trace("Filter starts");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(user == null) {
            Log.warn("Not authorized client response. Go to the main-index page");
            response.sendRedirect(request.getContextPath() + indexPath);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
