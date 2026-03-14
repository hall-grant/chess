package service;

import dataaccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.records.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    private DatabaseGameDao gameDao;
    private GameService gameService;

    private String authToken;

    @BeforeEach
    void setup() throws DataAccessException {
        gameDao = new DatabaseGameDao();
        DatabaseAuthTokenDao authDao = new DatabaseAuthTokenDao();
        DatabaseUserDao userDao = new DatabaseUserDao();
        gameService = new GameService(gameDao, authDao);
        UserService userService = new UserService(userDao, authDao);

        RegisterRequest registerRequest = new RegisterRequest("bob", "pass", "email@email");
        RegisterResult registerResult = userService.register(registerRequest);

        authToken = registerResult.authToken();
    }


    // create
    // positive
    @Test
    void create() throws DataAccessException{
        CreateRequest req = new CreateRequest(authToken, "game");
        CreateResult res = gameService.create(req);

        assertEquals(1, res.gameID()); // why can't it be zero? I'm still salty about this
    }

    // negative
    // unauthorized
    @Test
    void createMissingAuth(){
        CreateRequest req = new CreateRequest(null, "game");
        assertThrows(DataAccessException.class, () -> gameService.create(req));
    }

    // bad request
    @Test
    void createMissingName(){
        CreateRequest req = new CreateRequest(authToken, null);
        assertThrows(DataAccessException.class, () -> gameService.create(req));
    }



    // list
    // positive
    @Test
    void list() throws DataAccessException{
        CreateRequest req = new CreateRequest(authToken, "game1");
        gameService.create(req);

        ListRequest listRequest = new ListRequest(authToken);
        ListResult listResult = gameService.list(listRequest);

        assertEquals(1, listResult.games().size());

        gameService.create(new CreateRequest(authToken, "game2"));

        listResult = gameService.list(listRequest);

        assertEquals(2, listResult.games().size());

        assertEquals("game1", listResult.games().getFirst().gameName());
        assertEquals("game2", listResult.games().getLast().gameName());
    }

    // negative
    // unauthorized
    @Test
    void listUnauthorized(){
        ListRequest req = new ListRequest(null);
        assertThrows(DataAccessException.class, () -> gameService.list(req));
    }



    // join
    // positive
    @Test
    void join() throws DataAccessException{
        CreateRequest req = new CreateRequest(authToken, "game");
        CreateResult res = gameService.create(req);

        JoinRequest joinRequest = new JoinRequest(authToken, res.gameID(), "WHITE");
        gameService.join(joinRequest);

        assertEquals("bob", gameDao.getGame(res.gameID()).whiteUsername());
    }

    // negative
    // unauthorized
    @Test
    void joinMissingAuth() throws DataAccessException{
        CreateRequest createRequest = new CreateRequest(authToken, "game");
        CreateResult createResult = gameService.create(createRequest);

        JoinRequest req = new JoinRequest(null, createResult.gameID(), "BLACK");
        assertThrows(DataAccessException.class, () -> gameService.join(req));
    }

    // bad request
    @Test
    void joinBadColor() throws DataAccessException{
        CreateRequest createRequest = new CreateRequest(authToken, "game");
        CreateResult createResult = gameService.create(createRequest);

        JoinRequest req = new JoinRequest(authToken, createResult.gameID(), "no");
        assertThrows(DataAccessException.class, () -> gameService.join(req));
    }

    @Test
    // already taken
    void joinAlreadyTaken() throws DataAccessException{
        CreateRequest createRequest = new CreateRequest(authToken, "game");
        CreateResult createResult = gameService.create(createRequest);

        // join same color twice
        gameService.join(new JoinRequest(authToken, createResult.gameID(), "WHITE"));

        assertThrows(DataAccessException.class, () -> gameService.join(new JoinRequest(authToken, createResult.gameID(), "WHITE")));
    }

    // 500 error
    @Test
    void joinBadID(){
        JoinRequest req = new JoinRequest(authToken, 10, "BLACK");
        assertThrows(DataAccessException.class, () -> gameService.join(req));
    }

}
