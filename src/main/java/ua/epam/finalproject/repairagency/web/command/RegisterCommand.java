package ua.epam.finalproject.repairagency.web.command;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.Path;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.model.Client;
import ua.epam.finalproject.repairagency.service.ClientService;
import ua.epam.finalproject.repairagency.service.HashPassword;
import ua.epam.finalproject.repairagency.to.ClientTo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class RegisterCommand extends ActionCommand {

    private static final Logger Log = Logger.getLogger(RegisterCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        Log.debug("Command starts");

        String clientEmail = request.getParameter("email");
        String clientPassword = request.getParameter("password");
        String clientPasswordRepeat = request.getParameter("passwordRepeat");
        String clientName = request.getParameter("clientName");
        Log.debug("Gotten params ==> ClientName: " + clientName + "; email: " + clientEmail);

        if(StringUtils.isEmpty(clientName)) {
            try {
                response.getWriter().write("introduce yourself");
            } catch (IOException e) {
                Log.error("Can't get request writer");
                throw new AppException(e.getMessage());
            }
            Log.debug("Client name is empty");
            return null;
        }

        if(!ClientService.validEmail(clientEmail)) {
            try {
                response.getWriter().write("not valid email");
            } catch (IOException e) {
                Log.error("Can't get request writer");
                throw new AppException(e.getMessage());
            }
            Log.debug("Email is not valid according to regex");
            return null;
        }

        if(!ClientService.dontRepeatEmail(clientEmail)) {
            try {
                response.getWriter().write("repeated email");
            } catch (IOException e) {
                Log.error("Can't get request writer");
                throw new AppException(e.getMessage());
            }
            Log.debug("Email is repeated");
            return null;
        }

        if(!ClientService.checkPasswords(clientPassword, clientPasswordRepeat)) {
            try {
                response.getWriter().write("not same passwords");
            } catch (IOException e) {
                Log.error("Can't get request writer");
                throw new AppException(e.getMessage());
            }
            Log.debug("Password is not the same");
            return null;
        }


        Client newClient = new Client();
        try {
            newClient.setPassword(HashPassword.getHash(clientPassword));
        } catch (NoSuchAlgorithmException e) {
            Log.error("Can't get hash from password");
            throw new AppException(e.getMessage());
        }
        newClient.setEmail(clientEmail);
        newClient.setClientName(clientName);

        Log.debug("Start to put new Client to DB");

        if(ClientService.addClient(newClient)) {
            Log.debug("New Client is in DB");

            try {
                response.getWriter().write("success registration");
            } catch (IOException e) {
                Log.error("Can't get request writer");
                throw new AppException(e.getMessage());
            }
            Log.debug("success registration");
            return null;


            //// Вопрос: Почему в дальнейшем не происходит forward из класса Controller на страницу WEB-INF/authorizedPages/clientMain.jsp?p=clientMain
//            Log.debug("Command are about to finish with forwardPage " + Path.PAGE_SUCCESS_RESULT_REGISTRATION);
//            return Path.PAGE_SUCCESS_RESULT_REGISTRATION;
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        }
        Log.debug("Can't register a new user");
        throw new AppException("Can't register a new user");
    }
}
