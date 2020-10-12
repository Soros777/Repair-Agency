package ua.epam.finalproject.repairagency.repository;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.model.Client;
import ua.epam.finalproject.repairagency.model.Role;
import ua.epam.finalproject.repairagency.model.User;
import ua.epam.finalproject.repairagency.service.UserUtil;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class UserDaoDB implements UserDao {

    private final static Logger Log = Logger.getLogger(UserDaoDB.class);

    @Override
    public User getRegisteredUser(Connection connection, String email, String password, Role role) {
        User registeredUser = null;
        try (
             // todo : joinquery
             PreparedStatement mainPreparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email=?");
             PreparedStatement localePreparedStatement = connection.prepareStatement("SELECT value FROM locales WHERE id=?");
             PreparedStatement rolePreparedStatement = connection.prepareStatement("SELECT value FROM roles WHERE id=?");
             PreparedStatement walletCountPreparedStatement = connection.prepareStatement("SELECT wallet_count FROM clients WHERE parent=?")
        )
        {
            Log.debug("Connections and preparedStatements are obtained");

            mainPreparedStatement.setString(1, email);

            Log.debug("start to execute main query" + mainPreparedStatement);
            ResultSet resultSet = mainPreparedStatement.executeQuery();
            Log.debug("mainQuery executed");

            if(resultSet.next()) {
                Log.debug("There is a recording in the DB");
                String passwordFromDB = resultSet.getString("password");
                if(!password.equals(passwordFromDB)) {
                    Log.trace("Entered incorrect password");
                    return null;
                }
                Log.trace("password is ok");
                int roleId = resultSet.getInt("role_id");
                if(!UserUtil.checkRole(rolePreparedStatement, roleId, role)) {
                    Log.debug("Roles are different");
                    return null;
                }
                int localeId = resultSet.getInt("locale_id");
                Locale userLocale = UserUtil.getUserLocale(localePreparedStatement, localeId);

                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDate userRegistrationDate = LocalDate.parse(resultSet.getString("registration_date"), dateTimeFormatter);

                int id = resultSet.getInt("id");
                String personName = resultSet.getString("person_name");
                String photoPath = resultSet.getString("photo_path");
                String contactPhone = resultSet.getString("contact_phone");

                Log.debug("Start to create user");
                if(role.valueEqualsTo("Client")) {
                    registeredUser = new Client();
                } else {
                    registeredUser = new User();
                }
                registeredUser.setId(id);
                registeredUser.setEmail(email);
                registeredUser.setPassword(password);
                registeredUser.setPersonName(personName);
                registeredUser.setRole(role);
                registeredUser.setPhotoPath(photoPath);
                registeredUser.setContactPhone(contactPhone);
                registeredUser.setLocale(userLocale);
                registeredUser.setRegistrationDate(userRegistrationDate);
                Log.debug("User fields are initiated");
            }
            if(registeredUser != null && role.valueEqualsTo("Client")) {
                Log.trace("It is a client");
                double walletCount = getWalletCount(walletCountPreparedStatement, registeredUser.getId());
                if(registeredUser instanceof Client) {
                    ((Client) registeredUser).setWalletCount(walletCount);
                }
            }
        } catch (SQLException e) {
            Log.error("Can't obtain User from DB : " + e);
            throw new AppException("Can't obtain User from DB.", e);
        }
        if(registeredUser == null) {
            Log.error("Failed during obtain registered user");
            throw new AppException("Failed during obtain registered user");
        }

        return registeredUser;
    }

    private double getWalletCount(PreparedStatement walletCountPreparedStatement, int id) throws SQLException {
        Log.debug("Start to obtain client wallet count for client with id : " + id);
        walletCountPreparedStatement.setInt(1, id);
        ResultSet resultSet = walletCountPreparedStatement.executeQuery();
        if(resultSet.next()) {
            return resultSet.getDouble(1);
        }
        Log.error("Can't obtain client wallet count");
        throw new AppException("Can't obtain client wallet count");
    }

    @Override
    public boolean addClient(Connection connection, Client client) throws SQLException {
        Log.info("adding client");

        PreparedStatement mainPreparedStatement = null;
        PreparedStatement walletPreparedStatement = null;
        PreparedStatement localePreparedStatement = null;
        ResultSet genKeys = null;

        try {
            mainPreparedStatement = connection.prepareStatement(
                    "INSERT INTO users (email, password, person_name, role_id, locale_id) VALUES (?, ?, ?, 2, ?)", Statement.RETURN_GENERATED_KEYS);
            walletPreparedStatement = connection.prepareStatement(
                    "INSERT INTO clients (parent) VALUES (?)");
            localePreparedStatement = connection.prepareStatement(
                    "SELECT id FROM locales WHERE value=?");
            int k = 1;
            mainPreparedStatement.setString(k++, client.getEmail());
            mainPreparedStatement.setString(k++, client.getPassword());
            mainPreparedStatement.setString(k++, client.getPersonName());
            mainPreparedStatement.setInt(k, getIdFromLocale(localePreparedStatement, client.getLocale()));

            mainPreparedStatement.execute();
            Log.debug("go to mainPreparedStatement.getGeneratedKeys() : " + mainPreparedStatement);
            genKeys = mainPreparedStatement.getGeneratedKeys();
            Log.debug("Gotten ResultSet");
            if(genKeys.next()) {
                Log.debug("Success added new client to users table");
                int id = genKeys.getInt(1);
                if(id > 0) {
                    walletPreparedStatement.setInt(1, id);
                    Log.debug("go to add client wallet_count");
                    return walletPreparedStatement.executeUpdate() == 1;
                }
            }
        } catch (SQLException e) {
            Log.error("Falling during adding client to DB");
            if(genKeys != null) {
                genKeys.close();
            }
            if(walletPreparedStatement != null) {
                walletPreparedStatement.close();
            }
            if(mainPreparedStatement != null) {
                mainPreparedStatement.close();
            }
            if(localePreparedStatement != null) {
                localePreparedStatement.close();
            }
            throw new SQLException(e);
        }

        Log.error("Falling during adding a new client to DB");
        throw new AppException("Falling during adding a new client to DB");
    }

    @Override
    public List<User> findAllUsers(Connection connection) {
        return null;
    }

    private int getIdFromLocale(PreparedStatement localePreparedStatement, Locale locale) throws SQLException {
        localePreparedStatement.setString(1, locale.getCountry());
        ResultSet resultSet = localePreparedStatement.executeQuery();
        if(resultSet.next()) {
            int id = resultSet.getInt(1);
            Log.debug("Success for getting locale id from DB: " + id);
            return id;
        }
        Log.trace("Define default locale EN");
        return 3;
    }
}
