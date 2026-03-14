package service;

import dataaccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.records.CreateRequest;
import service.records.RegisterRequest;
import service.records.RegisterResult;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClearServiceTest {
    private DatabaseGameDao gameDao;
    private DatabaseUserDao userDao;

    private UserService userService;
    private GameService gameService;
    private ClearService clearService;

    @BeforeEach
    void setup(){
        gameDao = new DatabaseGameDao();
        userDao = new DatabaseUserDao();
        DatabaseAuthTokenDao authDao = new DatabaseAuthTokenDao();

        userService = new UserService(userDao, authDao);
        gameService = new GameService(gameDao, authDao);
        clearService = new ClearService(userDao, authDao, gameDao);
    }

    @Test
    void clear() throws DataAccessException{
        RegisterRequest registerRequest = new RegisterRequest("bob", "pass", "email@email");
        RegisterResult registerResult = userService.register(registerRequest);

        CreateRequest req = new CreateRequest(registerResult.authToken(), "game");
        gameService.create(req);

        clearService.clear();
        assertEquals(0, userDao.listUsers().size());
        assertEquals(0, gameDao.listGames().size());
    }

}
