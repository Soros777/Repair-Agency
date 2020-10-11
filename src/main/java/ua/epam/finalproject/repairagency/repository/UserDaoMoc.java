package ua.epam.finalproject.repairagency.repository;

import ua.epam.finalproject.repairagency.model.Client;
import ua.epam.finalproject.repairagency.model.Role;
import ua.epam.finalproject.repairagency.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class UserDaoMoc implements UserDao {

    private List<User> userList;

    public UserDaoMoc() {
        User director = User.getUserWithInitParams(1, "boss@gmail.com", "ѕ*йїEп:Ю%\u0016УNбЄ\u000FЭ Ъ–�",
                "Александр Васильевич", Role.DIRECTOR, "img/users/boss.jpg", null, Locale.ENGLISH, LocalDate.now());
        Client arni = Client.getClientWithInitParams(2, "arni@gmail.com", "я„\u0002›ъЎX\u0018ѕ3щЋѕЄ'\u0018#жп;",
                "Arnold Schwarzenegger", Role.CLIENT, 0, "img/users/Arnold.jpg", null, Locale.US, LocalDate.now());
        Client bond = Client.getClientWithInitParams(3, "bond007@gmail.com", "ЫГЕ;/кZ„Й\u001Bъ%›БыGBј*w",
                "James Bond", Role.CLIENT, 0, "img/users/JamesBond.jpg", null, Locale.ENGLISH, LocalDate.now());
        Client jolie = Client.getClientWithInitParams(4, "jolie@gmail.com", "*yџ\u0012д9OweЙє№A\u00AD\u000F©Л\u0016\"m",
                "Angelina Jolie", Role.CLIENT, 0, "img/users/Angelina-Jolie.jpg", null, Locale.ENGLISH, LocalDate.now());

    }

    @Override
    public User getRegisteredUser(Connection connection, String email, String password, Role role) {
        return null;
    }

    @Override
    public boolean addClient(Connection connection, Client client) throws SQLException {
        return false;
    }
}
