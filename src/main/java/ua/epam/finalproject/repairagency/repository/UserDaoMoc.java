package ua.epam.finalproject.repairagency.repository;

import ua.epam.finalproject.repairagency.model.Client;
import ua.epam.finalproject.repairagency.model.Role;
import ua.epam.finalproject.repairagency.model.User;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserDaoMoc implements UserDao {

    private List<User> userList = new ArrayList<>();

    public UserDaoMoc() {
        User director = User.getUserWithInitParams(1, "boss@gmail.com", "534529C7BC541D7FC695138173B204A28E3A3AB6B232FB4529B316403D37E13A",
                "Александр Васильевич", Role.DIRECTOR, "img/users/boss.jpg", null, Locale.ENGLISH, null);
        Client arni = Client.getWithInitParams(2, "arni@gmail.com", "BDD7A963C337355D3F367FAF0396DAA743FD634C44762BFE5D885920B355B3E5",
                "Arnold Schwarzenegger", Role.CLIENT, 0, "img/users/Arnold.jpg", null, Locale.US, null);
        Client bond = Client.getWithInitParams(3, "bond007@gmail.com", "8666BFF9D5870B8CFCD6C5AC194540C1EE3B14A70A7ED5B91C749BE93D39651F",
                "James Bond", Role.CLIENT, 0, "img/users/JamesBond.jpg", null, Locale.ENGLISH, null);
        Client jolie = Client.getWithInitParams(4, "jolie@gmail.com", "0AF50A1A1BE62FE78F7DDB2F2D7726041ACE95ECE6FE99F5CF108A0A047097AC",
                "Angelina Jolie", Role.CLIENT, 0, "img/users/Angelina-Jolie.jpg", null, Locale.ENGLISH, null);
        userList.add(director);
        userList.add(arni);
        userList.add(bond);
        userList.add(jolie);
    }


    @Override
    public User getRegisteredUser(Connection connection, String email, String password, Role role) {
        return null;
    }

    @Override
    public User getUserByEmail(Connection connection, String email) throws SQLException {
        return null;
    }

    @Override
    public int addUser(Connection connection, User user, int locale_id, int role_id) throws SQLException {
        return 0;
    }

    @Override
    public List<User> findAllUsers(Connection connection) {
        return null;
    }

    @Override
    public int getIdFromLocale(Connection connection, Locale locale) throws SQLException {
        return 0;
    }

    @Override
    public int getIdFromRole(Connection connection, Role role) throws SQLException {
        return 0;
    }

    @Override
    public void addClientWallet(Connection connection, int userId) throws SQLException {

    }

    @Override
    public double getWalletValue(Connection connection, int id) throws SQLException {
        return 0;
    }
}
