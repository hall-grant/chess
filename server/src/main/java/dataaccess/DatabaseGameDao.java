package dataaccess;

import chess.ChessGame;
import com.google.gson.GsonBuilder;
import model.GameData;
import model.UserData;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class DatabaseGameDao {

    private final Gson gson = new GsonBuilder().serializeNulls().create();

    public int createGame(GameData gameData) throws DataAccessException{
        String gameCode = gson.toJson(gameData.chessGame()); // serialize
        String whiteUsername;
        String blackUsername;
        if(gameData.whiteUsername() != null){
            whiteUsername = "'" + gameData.whiteUsername() + "'";
        } else{
            whiteUsername = "NULL";
        }

        if (gameData.blackUsername() == null) {
            blackUsername = "NULL";
        } else {
            blackUsername = "'" + gameData.blackUsername() + "'";
        }


        // shouldn't assign gameIDs
        String command = "INSERT INTO games(whiteUsername, blackUsername, gameName, game) VALUES ("
                + whiteUsername + ", " // quotes not needed
                + blackUsername + ", '"
                + gameData.gameName() + "', '"
                + gameCode + "')";

        try (Connection connection = DatabaseManager.getConnection();
        Statement statement = connection.createStatement()){

            statement.executeUpdate(command, Statement.RETURN_GENERATED_KEYS); // should be able to pull gameID

            try (ResultSet resultSet = statement.getGeneratedKeys()){
                if(resultSet.next()){
                    return resultSet.getInt(1); // returns gameID
                }else{
                    throw new DataAccessException("Getting gameID failed");
                }
            }

        } catch(SQLException ex){
            throw new DataAccessException("Creation of game failed");
        }
    }

    public GameData getGame(int gameID) throws DataAccessException{
        String command = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games WHERE gameID = "
                + gameID;

        try(Connection connection = DatabaseManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(command)){

            if(resultSet.next()){
                int gID = resultSet.getInt("gameID");
                String whiteUsername = resultSet.getString("whiteUsername");
                String blackUsername = resultSet.getString("blackUsername");
                String name = resultSet.getString("gameName");

                ChessGame game = gson.fromJson(resultSet.getString("game"), ChessGame.class);

                return new GameData(gID, whiteUsername, blackUsername, name, game);
            }
            throw new DataAccessException("Game with gameID doesn't exist");

        } catch(SQLException ex){
            throw new DataAccessException("Getting game failed");
        }
    }

    public List<GameData> listGames() throws DataAccessException{
        List<GameData> gameList = new ArrayList<>();

        String command = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games";

        try(Connection connection = DatabaseManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(command)){

            while(resultSet.next()){ // goes
                int gID = resultSet.getInt("gameID");
                String whiteUsername = resultSet.getString("whiteUsername");
                String blackUsername = resultSet.getString("blackUsername");
                String name = resultSet.getString("gameName");

                ChessGame game;
                String gameCode = resultSet.getString("game");
                if(gameCode == null || gameCode.isEmpty()){
                    game = new ChessGame();
                } else {
                    game = gson.fromJson(gameCode, ChessGame.class);
                    gameList.add(new GameData(gID, whiteUsername, blackUsername, name, game));
                }
            }

            return gameList;

        } catch(SQLException ex){
            throw new DataAccessException("Listing games failed");
        }

    }

    public void updateGame(GameData game) throws DataAccessException{

        String gameCode = gson.toJson(game.chessGame()); // serialize
        String whiteUsername;
        String blackUsername;
        if(game.whiteUsername() != null){
            whiteUsername = "'" + game.whiteUsername() + "'";
        } else{
            whiteUsername = "NULL";
        }

        if (game.blackUsername() == null) {
            blackUsername = "NULL";
        } else {
            blackUsername = "'" + game.blackUsername() + "'";
        }

        String command = "UPDATE games SET " // can't set id because of auto-increment
                + "whiteUsername = " + whiteUsername + ", "
                + "blackUsername = " + blackUsername + ", "
                + "gameName = '" + game.gameName() + "', "
                + "game = '" + gameCode + "' " + "WHERE "
                + "gameID = " + game.gameID();

        try (Connection connection = DatabaseManager.getConnection();
        Statement statement = connection.createStatement()){

            if(statement.executeUpdate(command) <= 0){ // returns 0 if nothing returned
                throw new DataAccessException("Game doesn't exist");
            }

        } catch(SQLException ex){
            throw new DataAccessException("Updating game failed");
        }

    }

    public void clear() throws DataAccessException{
        String command = "DELETE FROM games";

        try (Connection connection = DatabaseManager.getConnection();
        Statement statement = connection.createStatement()){
            statement.executeUpdate(command);
        } catch(SQLException ex){
            throw new DataAccessException("Clearing games failed");
        }
    }

}

