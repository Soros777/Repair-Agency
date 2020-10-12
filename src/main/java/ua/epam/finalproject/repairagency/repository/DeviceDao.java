package ua.epam.finalproject.repairagency.repository;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeviceDao {

    private static final Logger Log = Logger.getLogger(DeviceDao.class);

    public int getDeviceId(Connection connection, String deviceStr) throws SQLException {
        Log.debug("Start define device id");

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT id FROM devices WHERE value=?");
            preparedStatement.setString(1, deviceStr.toUpperCase());
            Log.debug("go to db with preparedStatement " + preparedStatement);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                int deviceId = resultSet.getInt(1);
                Log.debug("Success define device id : " + deviceId);
                return deviceId;
            }
        } catch (SQLException e) {
            Log.error("Cant define device id");
            if(resultSet != null) {
                resultSet.close();
            }
            if(preparedStatement != null) {
                preparedStatement.close();
            }
            throw new SQLException(e);
        }

        Log.error("Can't define device id");
        throw new AppException("Can't define device id");
    }
}
