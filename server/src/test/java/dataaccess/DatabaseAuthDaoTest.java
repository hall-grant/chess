package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearService;


import static org.junit.jupiter.api.Assertions.*;

public class DatabaseAuthDaoTest {

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
    void createAuthPositive() throws DataAccessException{

        AuthData authData = new AuthData("authToken", "user");

        authDao.createAuth(authData);

        AuthData getAuth = authDao.getAuth("authToken");

        assertNotNull(getAuth);

        assertEquals("authToken", getAuth.authToken());
        assertEquals("user", getAuth.userName());

    }

    // negative
    @Test
    void createAuthNegative() throws DataAccessException{

        AuthData authData = new AuthData("authToken", "user");

        authDao.createAuth(authData);

        assertThrows(DataAccessException.class, () -> authDao.createAuth(authData));

    }


    // get auth
    // positive
    @Test
    void getAuthPositive() throws DataAccessException{

        AuthData authData = new AuthData("authToken", "user");

        authDao.createAuth(authData);

        AuthData getAuth = authDao.getAuth("authToken");

        assertNotNull(getAuth);
        assertEquals("authToken", getAuth.authToken());
        assertEquals("user", getAuth.userName());

    }

    // negative
    @Test
    void getAuthNegative() throws DataAccessException{

        AuthData getAuth = authDao.getAuth("notAToken");

        assertNull(getAuth);

    }


    // delete
    // positive
    @Test
    void deleteAuthPositive() throws DataAccessException{

        AuthData authData = new AuthData("authToken", "user");
        authDao.createAuth(authData);

        authDao.deleteAuth("authToken");

        AuthData getAuth = authDao.getAuth("authToken");

        assertNull(getAuth);

    }

    @Test
    // negative
    void deleteAuthNegative() throws DataAccessException{

        assertThrows(DataAccessException.class, () -> authDao.deleteAuth("no"));

    }


    // clear
    @Test
    void clearAuths() throws DataAccessException{

        AuthData authData = new AuthData("authToken", "user");

        authDao.createAuth(authData);

        // kind of seems redundant to do this since it already happens before each, but oh well
        ClearService clearService = new ClearService(userDao, authDao, gameDao);
        clearService.clear();

        assertNull(authDao.getAuth("authToken"));

    }


}
