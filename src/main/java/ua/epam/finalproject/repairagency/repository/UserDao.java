package ua.epam.finalproject.repairagency.repository;

import ua.epam.finalproject.repairagency.model.Client;
import ua.epam.finalproject.repairagency.model.Role;
import ua.epam.finalproject.repairagency.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

public interface UserDao {

    User getRegisteredUser(Connection connection, String email, String password, Role role); //todo remove this method

    User getUserByEmail(Connection connection, String email) throws SQLException;

    int addUser(Connection connection, User user, int locale_id, int role_id) throws SQLException;

    List<User> findAllUsers(Connection connection);

    int getIdFromLocale(Connection connection, Locale locale) throws SQLException;

    int getIdFromRole(Connection connection, Role role) throws SQLException;

    void addClientWallet(Connection connection, int userId) throws SQLException;

    double getWalletValue(Connection connection, int id) throws SQLException;

    List<User> getUsersViaRole(Connection connection, int roleId) throws SQLException;

    void takeOffMoney(Client client, double cost, Connection connection) throws SQLException;
}
