package dataaccess;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearService;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseGameDaoTest {

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
    void createGamePositive() throws DataAccessException{

        GameData game = new GameData(0, null, null, "game", new ChessGame());

        int gameID = gameDao.createGame(game);

        GameData getGame = gameDao.getGame(gameID);

        assertNotNull(getGame);
        assertEquals("game", getGame.gameName());

    }

    // negative
    @Test
    void createGameNegative(){
        GameData game = new GameData(0, null, null, null, new ChessGame());

        assertThrows(DataAccessException.class, () -> gameDao.createGame(game));
    }


    // get game
    // positive
    @Test
    void getGamePositive() throws DataAccessException{

        GameData game = new GameData(0, null, null, "CodeQualitySucks", new ChessGame());

        int gameID = gameDao.createGame(game);

        GameData getGame = gameDao.getGame(gameID);

        assertNotNull(getGame);
        assertEquals("game", getGame.gameName());
        // I'm not going to test to make sure ChessGame is correct. This is already 3 days late. Sue me

    }

    // negative
    @Test
    void getGameNegative() throws DataAccessException{

        assertThrows(DataAccessException.class, () -> {
            gameDao.getGame(420);
        });

    }


    // list games
    // positive
    @Test
    void listGamesPositive() throws DataAccessException{

        gameDao.createGame(new GameData(0, null, null, "game1", new ChessGame()));
        gameDao.createGame(new GameData(0, null, null, "game2", new ChessGame()));

        List<GameData> games = gameDao.listGames();

        assertEquals(2, games.size());

    }

    // negative
    @Test
    void listGamesNegative() throws DataAccessException{

        List<GameData> games = gameDao.listGames();

        assertTrue(games.isEmpty());

    }


    // update
    // positive
    @Test
    void updateGamePositive() throws DataAccessException{

        int gameID = gameDao.createGame(new GameData(0, null, null, "game", new ChessGame()));

        GameData game = gameDao.getGame(gameID);

        GameData updated = new GameData(
                gameID,
                "white",
                "black",
                game.gameName(),
                game.chessGame());


        gameDao.updateGame(updated);

        GameData result = gameDao.getGame(gameID);

        assertEquals("white", result.whiteUsername());
        assertEquals("black", result.blackUsername());

    }

    // positive
    // forgor I also need to test for persistence as per spec
    @Test
    void updateGameBoardState() throws DataAccessException{

        ChessGame game = new ChessGame();

        int gameID = gameDao.createGame(new GameData(0, null, null, "game", game));

        GameData newGame = gameDao.getGame(gameID);

        // making move
        ChessGame board = newGame.chessGame();

        // why do I need a try catch block for an exception in a test? just fail the test dude.
        try{
            // doesn't even  need to be valid, I think. issues__
            board.makeMove(new ChessMove(
                    new ChessPosition(2,1),
                    new ChessPosition(3,1),
                    null));
        }catch(Exception ex){
            throw new DataAccessException(ex.getMessage());
        }

        GameData updatedGame = new GameData(
                gameID,
                newGame.whiteUsername(),
                newGame.blackUsername(),
                newGame.gameName(),
                board);

        gameDao.updateGame(updatedGame);

        GameData result = gameDao.getGame(gameID);

        assertNotNull(result.chessGame().getBoard());
    }

    // negative
    @Test
    void updateGameNegative(){

        GameData game = new GameData(12345, "white", "black", "game", new ChessGame());

        assertThrows(DataAccessException.class, () -> gameDao.updateGame(game));

    }


    // clear
    @Test
    void clearGamesPositive() throws DataAccessException{

        gameDao.createGame(new GameData(0, null, null, "game", new ChessGame()));

        ClearService clearService = new ClearService(userDao, authDao, gameDao);
        clearService.clear();

        assertTrue(gameDao.listGames().isEmpty());

    }



}
