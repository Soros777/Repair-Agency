package ua.epam.finalproject.repairagency.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.epam.finalproject.repairagency.model.Client;
import ua.epam.finalproject.repairagency.model.Role;
import ua.epam.finalproject.repairagency.model.User;
import ua.epam.finalproject.repairagency.service.HashPassword;

import java.sql.SQLException;
import java.util.Locale;

import static org.junit.Assert.*;

public class UserDaoTest {

    private UserDao userDao;

    @Before
    public void setUp() throws Exception {
        userDao = new UserDaoMoc();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getRegisteredUser_should_Return_Boss() {
        User expected = User.getUserWithInitParams(1, "boss@gmail.com", HashPassword.getHash("point777"), "Александр Васильевич",
                Role.DIRECTOR, "img/users/boss.jpg", null, Locale.ENGLISH, null);
        User actual = userDao.getRegisteredUser(null, "$boss@gmail.com", HashPassword.getHash("point777"), Role.DIRECTOR);
        assertEquals(expected, actual);
    }

    @Test
    public void getRegisteredUser_should_Return_Arni() {
        Client expected = Client.getClientWithInitParams(2, "arni@gmail.com", HashPassword.getHash("trewas01"), "Arnold Schwarzenegger",
                Role.CLIENT, 0, "img/users/Arnold.jpg", null, Locale.US, null);
        Client actual = (Client) userDao.getRegisteredUser(null, "arni@gmail.com", HashPassword.getHash("trewas01"), Role.CLIENT);
        assertEquals(expected, actual);
    }

    @Test
    public void getRegisteredUser_NullUser() {
        Client actual = (Client) userDao.getRegisteredUser(null, "nothing@gmail.com", HashPassword.getHash("something"), Role.CLIENT);
        assertNull(actual);
    }

    @Test
    public void addClient() throws SQLException {
        int countUsersBefore = userDao.findAllUsers(null).size();
        Client client = Client.getClientWithInitParams(100, "ki@li.da", HashPassword.getHash("123"), "Брюс Ли",
                Role.CLIENT, 0, "img/users/userIcon.png", null, Locale.CHINA, null);
        userDao.addClient(null, client);
        assertEquals(countUsersBefore + 1, userDao.findAllUsers(null).size());
    }
}