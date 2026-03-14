package service;

import chess.ChessGame;
import com.google.gson.GsonBuilder;
import dataaccess.DataAccessException;
import dataaccess.DatabaseAuthTokenDao;
import dataaccess.DatabaseGameDao;
import dataaccess.GameDao;
import model.AuthData;
import model.GameData;
import service.records.*;

import java.util.ArrayList;
import java.util.List;



import com.google.gson.Gson; // for testing.

public class GameService {

    private final DatabaseGameDao gameDao;
    private final DatabaseAuthTokenDao authDao;
    // private int gameID;

    public GameService(DatabaseGameDao gameDao, DatabaseAuthTokenDao authDao){
        this.gameDao = gameDao;
        this.authDao = authDao;
        // gameID = 1; // can't start at 0? why would you be like this, tests?
    }

    public CreateResult create(CreateRequest r) throws DataAccessException{
        // invalid auth
        if(authDao.getAuth(r.authToken()) == null){
            throw new DataAccessException("unauthorized");
        }

        if(r.gameName() == null){
            throw new DataAccessException("bad request");
        }

//        int gameID = this.gameID;
//        this.gameID++;

        GameData nGame = new GameData(0, null, null, r.gameName(), new ChessGame());

        int gameID = gameDao.createGame(nGame); // returns gameID from database

        return new CreateResult(gameID);

    }


    public ListResult list(ListRequest r) throws DataAccessException{
        if(authDao.getAuth(r.authToken()) == null){
            throw new DataAccessException("unauthorized");
        }


        List<GameReturn> returns = new ArrayList<>();
        for(GameData game : gameDao.listGames()){
            returns.add(new GameReturn(game.gameID(), game.gameName(), game.whiteUsername(), game.blackUsername()));
        }

        Gson gson = new GsonBuilder().serializeNulls().create(); // to fix null serialization error
        // System.out.println(gson.toJson(new ListResult(returns)));

//        for(GameReturn gameReturn : returns){
//            System.out.println(gameReturn);
//        }

        return new ListResult(returns);

    }


    public JoinResult join(JoinRequest r) throws DataAccessException{
        if(authDao.getAuth(r.authToken()) == null){
            throw new DataAccessException("unauthorized");
        }

        if(r.gameID() == null || r.playerColor() == null){
            throw new DataAccessException("bad request");
        }

        if(!r.playerColor().equals("WHITE") && !r.playerColor().equals("BLACK")){
            throw new DataAccessException("bad request");
        }

        // should throw if no game with id
        GameData game = gameDao.getGame(r.gameID());
        GameData gameResult;

        if(r.playerColor().equals("WHITE")){

            if(game.whiteUsername() != null){
                throw new DataAccessException("already taken");
            }
            AuthData auth = authDao.getAuth(r.authToken());
            gameResult = new GameData(game.gameID(), auth.userName(), game.blackUsername(), game.gameName(), game.chessGame());

        } else{

            if(game.blackUsername() != null){
                throw new DataAccessException("already taken");
            }
            AuthData auth = authDao.getAuth(r.authToken());
            gameResult = new GameData(game.gameID(), game.whiteUsername(), auth.userName(), game.gameName(), game.chessGame());

        }

        gameDao.updateGame(gameResult);

        return new JoinResult();

    }


}
