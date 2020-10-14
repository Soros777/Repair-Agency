package ua.epam.finalproject.repairagency.web.filter;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.model.Role;
import ua.epam.finalproject.repairagency.model.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

public class CommandAccessFilter implements Filter {

    private String indexPath;
    private static final Logger Log = Logger.getLogger(AuthorizeFilter.class);

    // commands access
    private Map<Role, List<String>> accessMap = new HashMap<Role, List<String>>();
    private List<String> commons = new ArrayList<String>();
    private List<String> outOfControl = new ArrayList<String>();


    public void init(FilterConfig fConfig) throws ServletException {
        Log.trace("Filter initialization starts");
        indexPath = fConfig.getInitParameter("INDEX_PATH");

        // roles
        accessMap.put(Role.MANAGER, asList(fConfig.getInitParameter("manager")));
        accessMap.put(Role.CLIENT, asList(fConfig.getInitParameter("client")));
        accessMap.put(Role.MASTER, asList(fConfig.getInitParameter("master")));
        Log.trace("Access map --> " + accessMap);

        // commons
        commons = asList(fConfig.getInitParameter("common"));
        Log.trace("Common commands --> " + commons);

        // out of control
        outOfControl = asList(fConfig.getInitParameter("out-of-control"));
        Log.trace("Out of control commands --> " + outOfControl);

        Log.debug("Filter initialization finished");
    }


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Log.debug("Filter starts");

        if (accessAllowed(request)) {
            Log.debug("Filter finished");
            chain.doFilter(request, response);
        } else {

            //ajax message
            String errorMessasge = "You do not have permission to access the requested resource";

            request.setAttribute("errorMessage", errorMessasge);
            Log.warn("Attempting to invoke an inadmissible command");

            response.getWriter().write("no permissions");
        }
    }

    private boolean accessAllowed(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String commandName = request.getParameter("command");
        if (StringUtils.isEmpty(commandName)) {
            return false;
        }

        if (outOfControl.contains(commandName)) {
            return true;
        }

        HttpSession session = httpRequest.getSession(false);
        if (session == null) {
            return false;
        }

        User user = (User) session.getAttribute("user");
        if(user == null) {
            return false;
        }
        Role role = user.getRole();

        return accessMap.get(role).contains(commandName)
                || commons.contains(commandName);
    }

    /**
     * Extracts parameter values from string.
     *
     * @param str
     *            parameter values string.
     * @return list of parameter values.
     */
    private List<String> asList(String str) {
        List<String> list = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(str);
        while (st.hasMoreTokens()) {
            list.add(st.nextToken());
        }
        return list;
    }

    public void destroy() {
    }
}
