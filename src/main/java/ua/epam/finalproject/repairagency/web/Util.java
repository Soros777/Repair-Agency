package ua.epam.finalproject.repairagency.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

public class Util {

    public static void showInputParameters(HttpServletRequest request, HttpServletResponse response, String methodName) {
        String uri = request.getRequestURI();
        String params = formatParams(request);
        try {
            String out = new StringBuilder("Method ")
                    .append(methodName)
                    .append("\nURI: ")
                    .append(uri)
                    .append("\n")
                    .append(params)
                    .toString();
            response.getWriter().write(out);
        } catch (IOException e) {
            throw new RuntimeException("Can't write in response");
        }
    }

    private static String formatParams(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        Map parameterMap = request.getParameterMap();
        Iterator it = parameterMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String values = Arrays.toString((String[]) pair.getValue());
            stringBuilder.append(pair.getKey()).append(" ==> ").append(values).append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }
}
