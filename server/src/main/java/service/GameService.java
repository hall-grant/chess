package service;

import chess.ChessGame;
import dataaccess.AuthTokenDao;
import dataaccess.DataAccessException;
import dataaccess.GameDao;
import model.GameData;

import java.util.ArrayList;
import java.util.List;

public class GameService {

    private final GameDao gameDao;
    private final AuthTokenDao authDao;
    private int gameID;

    public GameService(GameDao gameDao, AuthTokenDao authDao){
        this.gameDao = gameDao;
        this.authDao = authDao;
        gameID = 0;
    }

    public CreateResult create(CreateRequest r) throws DataAccessException{
        // invalid auth
        if(authDao.getAuth(r.authToken()) == null){
            throw new DataAccessException("unauthorized");
        }

        if(r.gameName() == null){
            throw new DataAccessException("bad request");
        }

        int gameID = this.gameID;
        this.gameID++;

        gameDao.createGame(new GameData(gameID, null, null, r.gameName(), new ChessGame()));

        return new CreateResult(gameID);

    }


    public ListResult list(ListRequest r) throws DataAccessException{
        if(authDao.getAuth(r.authToken()) == null){
            throw new DataAccessException("unauthorized");
        }


        List<GameReturn> returns = new ArrayList<>();
        for(GameData game : gameDao.listGames()){
            returns.add(new GameReturn(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()));
        }

        return new ListResult(returns);

    }


}
