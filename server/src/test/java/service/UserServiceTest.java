package service;

import dataaccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.records.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private DatabaseAuthTokenDao authDao;
    private UserService userService;

    @BeforeEach
    void setup(){
        DatabaseUserDao userDao = new DatabaseUserDao();
        authDao = new DatabaseAuthTokenDao();
        userService = new UserService(userDao, authDao);
    }

    // register
    // positive
    @Test
    void register() throws DataAccessException{
        RegisterRequest req = new RegisterRequest("dude", "pass", "email@email");
        // skip over http
        RegisterResult res = userService.register(req);

        assertNotNull(res.authToken());
        assertEquals("dude", res.username());
    }

    // negative
    // bad request
    @Test
    void registerMissingName(){
        RegisterRequest req = new RegisterRequest(null, "pass", "email@email");
        assertThrows(DataAccessException.class, () -> userService.register(req));
    }

    @Test
    void registerMissingPass(){
        RegisterRequest req = new RegisterRequest("bob", null, "email@email");
        assertThrows(DataAccessException.class, () -> userService.register(req));
    }

    @Test
    void registerMissingEmail(){
        RegisterRequest req = new RegisterRequest("bob", "pass", null);
        assertThrows(DataAccessException.class, () -> userService.register(req));
    }

    // already taken
    @Test
    void registerTwice() throws DataAccessException{
        RegisterRequest bob1 = new RegisterRequest("bob", "pass", "bob@email");
        userService.register(bob1);

        // try registering again with bob
        RegisterRequest bob2 = new RegisterRequest("bob", "pass2", "bob2@gamil");
        assertThrows(DataAccessException.class, () -> userService.register(bob2)); // I don't understand how this works
    }



    // login
    // positive
    @Test
    void login() throws DataAccessException{
        RegisterRequest req = new RegisterRequest("bob", "pass", "email@email");
        userService.register(req);

        LoginRequest loginRequest = new LoginRequest("bob", "pass");
        LoginResult loginResult = userService.login(loginRequest);

        assertNotNull(loginResult.authToken());
        assertEquals("bob", loginResult.username());
    }

    // negative
    // bad request
    @Test
    void loginMissingUsername(){
        LoginRequest req = new LoginRequest(null, "pass");
        assertThrows(DataAccessException.class, () -> userService.login(req));
    }

    @Test
    // bad request
    void loginMissingPassword(){
        LoginRequest req = new LoginRequest("bob", null);
        assertThrows(DataAccessException.class, () -> userService.login(req));
    }

    // unauthorized
    @Test
    void loginWrongUsername() {
        LoginRequest req = new LoginRequest("bill", "pass");
        assertThrows(DataAccessException.class, () -> userService.login(req));
    }

    // unauthorized
    @Test
    void loginWrongPassword() throws DataAccessException {
        RegisterRequest req = new RegisterRequest("bob", "pass", "email@email");
        userService.register(req);

        LoginRequest loginRequest = new LoginRequest("bob", "no");
        assertThrows(DataAccessException.class, () -> userService.login(loginRequest));
    }



    // logout
    // positive
    @Test
    void logout() throws DataAccessException {
        RegisterRequest req = new RegisterRequest("bob", "pass", "email@email");
        RegisterResult res = userService.register(req);

        LogoutRequest logoutRequest = new LogoutRequest(res.authToken());
        userService.logout(logoutRequest);

        // make sure the token no longer exists
        assertNull(authDao.getAuth(res.authToken()));
    }

    // negative
    // unauthorized
    @Test
    void logoutWrongAuth(){
        LogoutRequest req = new LogoutRequest("no");
        assertThrows(DataAccessException.class, () -> userService.logout(req));
    }

}




