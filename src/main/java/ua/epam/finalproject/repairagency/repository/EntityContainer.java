package ua.epam.finalproject.repairagency.repository;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.model.Client;
import ua.epam.finalproject.repairagency.model.Device;
import ua.epam.finalproject.repairagency.model.Role;
import ua.epam.finalproject.repairagency.model.User;
import ua.epam.finalproject.repairagency.service.UserUtil;

import java.sql.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class EntityContainer {

    private static Map<Integer, Client> clients = new HashMap<>();
    private static Map<Integer, User> personal = new HashMap<>();
    private static Map<Integer, Device> devices = new HashMap<>();
    private static Map<Integer, Role> roles = new HashMap<>();
    private static final Logger Log = Logger.getLogger(EntityContainer.class);

    public static void fillContainers(Connection connection) throws SQLException {
        Log.trace("Start fill entity containers");
        fillRoleContainer(connection);
        int clientRoleId = getRoleId(Role.CLIENT);
        fillClientContainer(connection, clientRoleId);
        fillPersonalContainer(connection, clientRoleId);
        fillDeviceContainer(connection);
    }

    private static void fillDeviceContainer(Connection connection) throws SQLException {
        Log.trace("Start fill device container");
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM devices");
            while (resultSet.next()) {
                Device device = Device.fromString(resultSet.getString("value"));
                devices.put(resultSet.getInt("id"), device);
            }
            Log.trace("Device container is : " + devices);
        } catch (SQLException e) {
            Log.error("Can't fill device container cause : " + e);
            RepositoryUtil.closeAndThrow(e, resultSet, statement);
        }
    }

    private static void fillPersonalContainer(Connection connection, int clientRoleId) throws SQLException {
        Log.trace("Start fill personal container");
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE id!=?");
            preparedStatement.setInt(1, clientRoleId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = obtain(resultSet);
                personal.put(resultSet.getInt("id"), user);
            }
            Log.trace("Personal container is : " + personal);
        } catch (SQLException e) {
            Log.error("Can't fill personal container cause : " + e);
            RepositoryUtil.closeAndThrow(e, resultSet, preparedStatement);
        }
    }

    private static User obtain(ResultSet resultSet) throws SQLException {
        return UserUtil.getUserFromParam(resultSet.getInt("id"),
                resultSet.getString("email"), resultSet.getString("password"),
                resultSet.getString("person_name"), Role.CLIENT,
                resultSet.getString("photo_path"), resultSet.getString("contact_phone"),
                Locale.forLanguageTag(resultSet.getString("locale_id")), resultSet.getString("registration_date"));
    }

    private static int getRoleId(Role role) {
        Log.trace("Start get id from role");
        Set<Map.Entry<Integer, Role>> entries = roles.entrySet();
        for (Map.Entry<Integer, Role> entry : entries) {
            if(entry.getValue() == role) {
                int result = entry.getKey();
                Log.trace("For role : " + role + "id is : " + result);
                return result;
            }
        }
        Log.error("Can't get role id");
        throw new AppException("Internal server error");
    }

    private static void fillRoleContainer(Connection connection) throws SQLException {
        Log.trace("Start fill role container");
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM roles");
            while (resultSet.next()) {
                Role role = Role.fromString(resultSet.getString("value"));
                roles.put(resultSet.getInt("id"), role);
            }
            Log.trace("Role container : " + roles);
        } catch (SQLException e) {
            Log.error("Can't fill role container cause : " + e);
            RepositoryUtil.closeAndThrow(e, resultSet, statement);
        }
    }

    private static void fillClientContainer(Connection connection, int clientRoleId) throws SQLException {
        Log.trace("Start fill client container");
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE role_id=?");
            preparedStatement.setInt(1, clientRoleId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                double walletCount = getWalletCount(connection, resultSet.getInt("id"));
                User user = obtain(resultSet);
                Client client = UserUtil.getClientFromUser(user, walletCount);
                clients.put(resultSet.getInt("id"), client);
            }
            Log.trace("Client container is : " + clients);
        } catch (SQLException e) {
            Log.error("Can't fill client container");
            RepositoryUtil.closeAndThrow(e, resultSet, preparedStatement);
        }
    }

    private static double getWalletCount(Connection connection, int clientId) throws SQLException {
        Log.trace("Start getting wallet count");
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT wallet_count FROM clients WHERE parent=?");
            preparedStatement.setInt(1, clientId);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                double result = resultSet.getDouble(1);
                Log.trace("Gotten wallet count : " + result + " for client id : " + clientId);
                return result;
            }
        } catch (SQLException e) {
            Log.error("Can't get wallet count cause : " + e);
            RepositoryUtil.closeAndThrow(e, resultSet, preparedStatement);
        }
        Log.error("Can't get wallet count.");
        throw new AppException("Internal server error");
    }

    public static Client getClientById(int clientId) {
        return clients.get(clientId);
    }

    public static Device getDeviceById(int deviceId) {
        return devices.get(deviceId);
    }

    public static User getPersonalById(int userId) {
        return personal.get(userId);
    }
}
