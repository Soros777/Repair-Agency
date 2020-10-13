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
    public User getUserByEmail(Connection connection, String email) throws SQLException {
        User user = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email=?");
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                //todo create user
            }
        } catch (SQLException e) {
            Log.error("Can't get user by email cause : " + e);
            if(resultSet != null) {
                resultSet.close();
            }
            if(preparedStatement != null) {
                preparedStatement.close();
            }
            throw new SQLException(e);
        }
        return user;
    }

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
    public int addUser(Connection connection, User user, int locale_id, int role_id) throws SQLException {
        Log.debug("Start adding new user : " + user);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO users (email, password, person_name, role_id, locale_id) " +
                                            "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            int k = 1;
            preparedStatement.setString(k++, user.getEmail());
            preparedStatement.setString(k++, user.getPassword());
            preparedStatement.setString(k++, user.getPersonName());
            preparedStatement.setInt(k++, role_id);
            preparedStatement.setInt(k, locale_id);
            Log.debug("do execute the preparedstatement " + preparedStatement);
            preparedStatement.execute();
            resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()) {
                int userId = resultSet.getInt(1);
                Log.debug("The user is added successfully. It's id is : " + userId);
                return userId;
            }
        } catch (SQLException e) {
            Log.error("Can't add user cause : " + e);
            if(resultSet != null) {
                resultSet.close();
            }
            if(preparedStatement != null) {
                preparedStatement.close();
            }
            throw new SQLException(e);
        }
        Log.debug("Can't add new user");
        throw new AppException("Can't add new user");
    }

    @Override
    public List<User> findAllUsers(Connection connection) {
        return null;
    }

    @Override
    public int getIdFromLocale(Connection connection, Locale locale) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int result;
        try {
            preparedStatement = connection.prepareStatement("SELECT id FROM locales WHERE value=?");
            preparedStatement.setString(1, locale.getCountry());
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                result = resultSet.getInt(1);
                Log.debug("Success obtained locale id : " + result);
                return result;
            }
            // is there is not such locale in the db. Can I use the resultSet and preparedStatement twice?
            preparedStatement.setString(1, "EN");
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                result = resultSet.getInt(1);
                Log.debug("Obtained default locale \"EN\"");
                return result;
            }
        } catch (SQLException e) {
            Log.error("Can't obtain locale id");
            if(resultSet != null) {
                resultSet.close();
            }
            if(preparedStatement != null) {
                preparedStatement.close();
            }
            throw new SQLException(e);
        }
        Log.error("Can't define locale id");
        throw new AppException("Can't add new user");
    }

    @Override
    public int getIdFromRole(Connection connection, Role role) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int result = -1;
        try {
            preparedStatement = connection.prepareStatement("SELECT id FROM roles WHERE value=?");
            preparedStatement.setString(1, role.toString());
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                result = resultSet.getInt(1);
                Log.debug("Success obtained role id : " + result);
                return result;
            }
        } catch (SQLException e) {
            Log.error("Can't obtain role id");
            if(resultSet != null) {
                resultSet.close();
            }
            if(preparedStatement != null) {
                preparedStatement.close();
            }
            throw new SQLException(e);
        }
        Log.error("Can't define role id");
        throw new AppException("Can't add new user");
    }

    @Override
    public void addClientWallet(Connection connection, int userId) throws SQLException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO clients (parent) VALUES (?)");
            preparedStatement.setInt(1, userId);
            if(preparedStatement.executeUpdate() != 1) {
                throw new AppException("Can't register new client");
            }
        } catch (SQLException e) {
            Log.error("Can't add client wallet");
            if(preparedStatement != null) {
                preparedStatement.close();
            }
            throw new SQLException(e);
        }
    }
}