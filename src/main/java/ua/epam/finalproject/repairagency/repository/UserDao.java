package ua.epam.finalproject.repairagency.repository;

import ua.epam.finalproject.repairagency.model.Client;
import ua.epam.finalproject.repairagency.model.Role;
import ua.epam.finalproject.repairagency.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {

    User getRegisteredUser(Connection connection, String email, String password, Role role);

    boolean addClient(Connection connection, Client client) throws SQLException;

    List<User> findAllUsers(Connection connection);
}
