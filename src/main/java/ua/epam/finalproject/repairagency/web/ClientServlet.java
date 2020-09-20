package ua.epam.finalproject.repairagency.web;

import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Soros
 * 20.09.2020
 */
public class ClientServlet extends HttpServlet {
    private static final Logger log = getLogger(ClientServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to clientList");
        response.sendRedirect("clientList.jsp");
    }
}
