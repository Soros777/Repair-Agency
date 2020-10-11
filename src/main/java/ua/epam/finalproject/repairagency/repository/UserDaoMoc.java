package ua.epam.finalproject.repairagency.repository;

import ua.epam.finalproject.repairagency.model.Client;
import ua.epam.finalproject.repairagency.model.Role;
import ua.epam.finalproject.repairagency.model.User;
import ua.epam.finalproject.repairagency.service.HashPassword;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class UserDaoMoc implements UserDao {

    private List<User> userList;

    public UserDaoMoc() {
        User director = User.getUserWithInitParams(1, "boss@gmail.com", "534529C7BC541D7FC695138173B204A28E3A3AB6B232FB4529B316403D37E13A",
                "Александр Васильевич", Role.DIRECTOR, "img/users/boss.jpg", null, Locale.ENGLISH, LocalDate.now());
        Client arni = Client.getClientWithInitParams(2, "arni@gmail.com", "BDD7A963C337355D3F367FAF0396DAA743FD634C44762BFE5D885920B355B3E5",
                "Arnold Schwarzenegger", Role.CLIENT, 0, "img/users/Arnold.jpg", null, Locale.US, LocalDate.now());
        Client bond = Client.getClientWithInitParams(3, "bond007@gmail.com", "8666BFF9D5870B8CFCD6C5AC194540C1EE3B14A70A7ED5B91C749BE93D39651F",
                "James Bond", Role.CLIENT, 0, "img/users/JamesBond.jpg", null, Locale.ENGLISH, LocalDate.now());
        Client jolie = Client.getClientWithInitParams(4, "jolie@gmail.com", "0AF50A1A1BE62FE78F7DDB2F2D7726041ACE95ECE6FE99F5CF108A0A047097AC",
                "Angelina Jolie", Role.CLIENT, 0, "img/users/Angelina-Jolie.jpg", null, Locale.ENGLISH, LocalDate.now());
        userList.add(director);
        userList.add(arni);
        userList.add(bond);
        userList.add(jolie);
    }

    @Override
    public User getRegisteredUser(Connection connection, String email, String password, Role role) {
        if(role != Role.CLIENT) {
            email = email.substring(1);
        }
        String finalEmail = email;
        return userList.stream().filter(u -> u.getEmail().equals(finalEmail)
                                        && u.getPassword().equals(password)
                                        && u.getRole().equals(role)).findAny().orElse(null);
    }

    @Override
    public boolean addClient(Connection connection, Client client) throws SQLException {
        userList.add(client);
        return true;
    }
}
