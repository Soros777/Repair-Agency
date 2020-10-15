package ua.epam.finalproject.repairagency.repository;

import org.apache.log4j.Logger;

import java.sql.SQLException;

public class RepositoryUtil {

    private static final Logger Log = Logger.getLogger(RepositoryUtil.class);

    public static void closeAndThrow(SQLException e, AutoCloseable ... resources) throws SQLException {
        try {
            for (AutoCloseable res : resources) {
                if(res != null) {
                    res.close();
                }
            }
        } catch (Exception exception) {
            Log.error("There is an exception : " + exception);
        }
        throw e;
    }
}
