package dataaccess;

import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseUserDaoTest {

    private DatabaseUserDao userDao;
    private DatabaseAuthTokenDao authDao;
    private DatabaseGameDao gameDao;

    @BeforeEach
    void setup() throws DataAccessException{
        userDao = new DatabaseUserDao();
        authDao = new DatabaseAuthTokenDao();
        gameDao = new DatabaseGameDao();

        ClearService clearService = new ClearService(userDao, authDao, gameDao);
        clearService.clear();
    }

    // create
    // positive
    @Test
    void createUserPositive() throws DataAccessException{
        UserData userData = new UserData("user", "pass", "email@email");

        userDao.createUser(userData);

        UserData getUser = userDao.getUser("user");

        assertNotNull(getUser);
        assertEquals("user", getUser.username());
        // idk how to get password in an easy way so I'm going to skip it.
        assertEquals("email@email", getUser.email());
    }

    // negative
    @Test
    void createUserNegative() throws DataAccessException{
        UserData userData = new UserData("user", "pass", "email@email");
        userDao.createUser(userData);

        assertThrows(DataAccessException.class, () -> userDao.createUser(userData));
    }


    // get
    // positive
    @Test
    void getUserPositive() throws DataAccessException{

        UserData userData = new UserData("user", "pass", "email@email");
        userDao.createUser(userData);

        UserData getUser = userDao.getUser("user");

        assertNotNull(getUser);
        assertEquals("user", getUser.username());

    }

    // negative
    @Test
    void getUserNegative() throws DataAccessException{

        UserData userData = new UserData("user", "pass", "email@email");
        userDao.createUser(userData);

        assertNull(userDao.getUser("no"));

    }


    // list
    // positive
    @Test
    void listUsersPositive() throws DataAccessException{

        UserData user1 = new UserData("user1", "pass", "email1@email");
        UserData user2 = new UserData("user2", "pass", "email2@email");

        userDao.createUser(user1);
        userDao.createUser(user2);

        List<UserData> users = userDao.listUsers();

        assertEquals(2, users.size());

        assertEquals("user1", users.getFirst().username());
        assertEquals("user2", users.getLast().username());

    }

    // negative
    @Test
    void listUsersNegative() throws DataAccessException{

        List<UserData> users = userDao.listUsers();

        assertTrue(users.isEmpty());
    }


    // clear
    @Test
    void clearUsers() throws DataAccessException {

        UserData userData = new UserData("user", "pass", "email@email");
        userDao.createUser(userData);

        ClearService clearService = new ClearService(userDao, authDao, gameDao);
        clearService.clear();

        assertNull(userDao.getUser("user"));
    }


}
