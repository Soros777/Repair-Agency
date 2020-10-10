package ua.epam.finalproject.repairagency.dao;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;
import ua.epam.finalproject.repairagency.model.Client;
import ua.epam.finalproject.repairagency.model.Role;
import ua.epam.finalproject.repairagency.model.User;
import ua.epam.finalproject.repairagency.service.HashPassword;
import ua.epam.finalproject.repairagency.service.UserUtil;

import javax.naming.NamingException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class UserDao {

    private final static Logger Log = Logger.getLogger(UserDao.class);

    public static User getRegisteredUser(String email, String password, Role role) {
        User registeredUser = null;
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             // todo : joinquery
             PreparedStatement mainPreparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email=?");
             PreparedStatement localePreparedStatement = connection.prepareStatement("SELECT value FROM locales WHERE id=?");
             PreparedStatement rolePreparedStatement = connection.prepareStatement("SELECT value FROM roles WHERE id=?");
             PreparedStatement walletCountPreparedStatement = connection.prepareStatement("SELECT wallet_count FROM clients WHERE parent=?")
        )
        {
            Log.debug("Connections and preparedStatements are obtained");
            password = HashPassword.getHash(password); //just for catch exception

            mainPreparedStatement.setString(1, email);

            Log.debug("start to execute main query");
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
                assert registeredUser instanceof Client;
                ((Client) registeredUser).setWalletCount(walletCount);

            }
        } catch (NamingException | SQLException | NoSuchAlgorithmException e) {
            Log.error("Can't obtain User from DB : " + e);
            throw new AppException("Can't obtain User from DB.", e);
        }
        if(registeredUser == null) {
            Log.error("Failed during obtain registered user");
            throw new AppException("Failed during obtain registered user");
        }

        return registeredUser;
    }

    private static double getWalletCount(PreparedStatement walletCountPreparedStatement, int id) throws SQLException {
        Log.debug("Start to obtain client wallet count for client with id : " + id);
        walletCountPreparedStatement.setInt(1, id);
        ResultSet resultSet = walletCountPreparedStatement.executeQuery();
        if(resultSet.next()) {
            return resultSet.getDouble(1);
        }
        Log.error("Can't obtain client wallet count");
        throw new AppException("Can't obtain client wallet count");
    }

    public static boolean addClient(Client client) {
        Log.info("adding client");

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement mainPreparedStatement = connection.prepareStatement(
                     "INSERT INTO users (email, password, person_name, role_id, locale_id) VALUES (?, ?, ?, 2, ?)", Statement.RETURN_GENERATED_KEYS);
             PreparedStatement walletPreparedStatement = connection.prepareStatement(
                     "INSERT INTO clients (parent) VALUES (?)");
        )
        {

            Log.info("================  connection.getAutoCommit() : " + connection.getAutoCommit());
//            connection.setAutoCommit(false);
            Log.info("================  connection.getAutoCommit() : " + connection.getAutoCommit());

            int k = 1;
            mainPreparedStatement.setString(k++, client.getEmail());
            mainPreparedStatement.setString(k++, client.getPassword());
            mainPreparedStatement.setString(k++, client.getPersonName());
            mainPreparedStatement.setInt(k, getIdFromLocale(client.getLocale()));

            Log.debug("go to mainPreparedStatement.getGeneratedKeys() : " + mainPreparedStatement);
            mainPreparedStatement.execute();
            ResultSet genKeys = mainPreparedStatement.getGeneratedKeys();
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

//            connection.commit(); // < ===== don't work

        } catch (SQLException | NamingException e) {
            Log.error("Can't put new Client to DB : " + e);
            throw new AppException("Can't put new Client to DB", e);
        }
        Log.error("Falling during adding a new client");
        throw new AppException("Falling during adding a new client");
    }

    private static int getIdFromLocale(Locale locale) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT id FROM locales WHERE value=?");
        )
        {
            preparedStatement.setString(1, locale.getCountry());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                int id = resultSet.getInt(1);
                Log.debug("Success for getting locale id from DB: " + id);
                return id;
            }
        } catch (SQLException | NamingException e) {
            Log.error("Can't get locale id from DB :" + e);
            throw new AppException("Can't get locale id from DB", e);
        }
        Log.trace("Define default locale EN");
        return 3;
    }
}
