package ua.epam.finalproject.repairagency.service;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ua.epam.finalproject.repairagency.model.Role;
import ua.epam.finalproject.repairagency.model.User;
import ua.epam.finalproject.repairagency.repository.ConnectionPool;
import ua.epam.finalproject.repairagency.repository.UserDao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.WeakHashMap;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private static UserService userService;

    @Mock
    private static UserDao userDao;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @Mock
    private ConnectionPool connectionPool;
    @Mock
    private Connection connection;


    @BeforeClass
    public static void setup() {
        userService = new UserService(userDao);
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

//    @Test
//    public void getRegisteredUser_Should_Return_Boss() throws SQLException, NamingException {
//        given(userDao.getRegisteredUser(null, "$boss@gmail.com", HashPassword.getHash("point777"), Role.DIRECTOR))
//                .willReturn(User.getUserWithInitParams(1, "boss@gmail.com", HashPassword.getHash("point777"),
//                        "Александр Васильевич", Role.DIRECTOR, "img/users/boss.jpg", null, Locale.ENGLISH, null));
//        when(request.getParameter("email")).thenReturn("$boss@gmail.com");
//        when(request.getParameter("password")).thenReturn("point777");
//        when(request.getSession()).thenReturn(session);
//        when(connectionPool.getInstance()).thenReturn(connectionPool);
//        when(connectionPool.getConnection()).thenReturn(connection);
//
//        User user = userService.getRegisteredUser(request);
//        assertNotNull(user);
//    }

    @Test
    public void addNewClient() {
    }
}