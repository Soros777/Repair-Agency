package ua.epam.finalproject.repairagency.web.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {"/jspf/*"},
            initParams = {@WebInitParam(name="INDEX_PATH", value = "/index.jsp")})
public class PageRedirectSecurityFilter implements Filter {

    private String indexPath;
    private static final Logger Log = Logger.getLogger(PageRedirectSecurityFilter.class);

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        Log.trace("Filter initialization starts");
        indexPath = fConfig.getInitParameter("INDEX_PATH");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fChain) throws IOException, ServletException {
        Log.trace("Filter starts");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponce = (HttpServletResponse) response;
        Log.trace("Go to the main-index page");
        httpResponce.sendRedirect(httpRequest.getContextPath() + indexPath);
        fChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
