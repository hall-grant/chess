package dataaccess;

import chess.ChessGame;
import com.google.gson.GsonBuilder;
import model.GameData;
import model.UserData;

import com.google.gson.Gson;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import java.sql.Types.*;

import static java.sql.Types.VARCHAR;

public class DatabaseGameDao {

    private final Gson gson = new GsonBuilder().serializeNulls().create();

    public int createGame(GameData gameData) throws DataAccessException{
        String gameCode = gson.toJson(gameData.chessGame()); // serialize
//        String whiteUsername;
//        if(gameData.whiteUsername() != null){
//            whiteUsername = "'" + gameData.whiteUsername() + "'";
//        } else{
//            whiteUsername = "NULL";
//        }
//
//        String blackUsername;
//        if (gameData.blackUsername() == null) {
//            blackUsername = "NULL";
//        } else {
//            blackUsername = "'" + gameData.blackUsername() + "'";
//        }


        // shouldn't assign gameIDs
        String command = "INSERT INTO games(whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";

        // stack overflow never fails, right?
        // use prepared statement to fix all the errors with string concatenation
        try (Connection connection = DatabaseManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(command, Statement.RETURN_GENERATED_KEYS)){


            if(gameData.whiteUsername() != null){
                statement.setString(1, gameData.whiteUsername());
            }else{
                statement.setNull(1, VARCHAR);
            }

            if(gameData.blackUsername() != null){
                statement.setString(2, gameData.blackUsername());
            }else{
                statement.setNull(2, VARCHAR);
            }

            statement.setString(3, gameData.gameName());
            statement.setString(4, gson.toJson(gameData.chessGame()));

            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if(resultSet.next()){
                return resultSet.getInt(1);
            }

            throw new DataAccessException("Getting gameID failed");


//
//
//            statement.executeUpdate(command, Statement.RETURN_GENERATED_KEYS); // should be able to pull gameID
//
//
//
//            try (ResultSet resultSet = statement.getGeneratedKeys()){
//                if(resultSet.next()){
//                    return resultSet.getInt(1); // returns gameID
//                }else{
//                    throw new DataAccessException("Getting gameID failed");
//                }
//            }

        } catch(SQLException ex){
            ex.printStackTrace();
            throw new DataAccessException("Creation of game failed", ex);
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
        PreparedStatement statement = connection.prepareStatement(command);
        ResultSet resultSet = statement.executeQuery()){

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
                }
                gameList.add(new GameData(gID, whiteUsername, blackUsername, name, game));
            }

            return gameList;

        } catch(SQLException ex){
            ex.printStackTrace();
            throw new DataAccessException("Listing games failed");
        }

    }

    public void updateGame(GameData game) throws DataAccessException{

        String command = "UPDATE games SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?";

        try(Connection connection = DatabaseManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(command)){

            if(game.whiteUsername() != null){
                statement.setString(1, game.whiteUsername());
            } else{
                statement.setNull(1, VARCHAR);
            }

            if(game.blackUsername() != null){
                statement.setString(2, game.blackUsername());
            }else{
                statement.setNull(2, VARCHAR);
            }

            statement.setString(3, game.gameName());
            statement.setString(4, gson.toJson(game.chessGame()));
            statement.setInt(5, game.gameID());

            if(statement.executeUpdate() <= 0){
                throw new DataAccessException("Game doesn't exist");
            }


        } catch(SQLException ex){
            ex.printStackTrace();
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

