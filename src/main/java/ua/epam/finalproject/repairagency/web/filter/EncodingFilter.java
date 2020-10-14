package ua.epam.finalproject.repairagency.web.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import java.io.IOException;

@WebFilter(urlPatterns = {"/*"},
            initParams = {
            @WebInitParam(name = "encoding", value = "UTF-8",description = "Encoding Param") })
public class EncodingFilter implements Filter {

    private String encoding;
    private static final Logger Log = Logger.getLogger(EncodingFilter.class);

    @Override
    public void init(FilterConfig fCoding) throws ServletException {
        Log.trace("Filter initialization starts");
        encoding = fCoding.getInitParameter("encoding");
        Log.trace("Encoding --> " + encoding);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fChain) throws IOException, ServletException {
        Log.trace("Filter starts");
        String codeRequest = request.getCharacterEncoding();
        if(encoding != null && !encoding.equalsIgnoreCase(codeRequest)) {
            request.setCharacterEncoding(encoding);
//            response.setCharacterEncoding(encoding);
            Log.trace("The encoding of request and response set as : " + encoding);
        }
        fChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        encoding = null;
    }
}
