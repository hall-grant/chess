package dataaccess;

import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class GameDao {
    private final Map<Integer, GameData> games = new HashMap<>();

    public void createGame(GameData gameData) throws DataAccessException{
        if(games.containsKey(gameData.gameID())){
            throw new DataAccessException("Game already exists");
        }
        games.put(gameData.gameID(), gameData);
    }

    public GameData getGame(int gameID) throws DataAccessException{
        if(games.containsKey(gameID)){
            return games.get(gameID);
        }
        throw new DataAccessException("Game with gameID doesn't exist");
    }

    public List<GameData> listGames(){
        List<GameData> gameList = new ArrayList<>();
        gameList.addAll(games.values());
        return gameList;
    }

    public void updateGame(GameData game) throws DataAccessException{
        if(!games.containsKey(game.gameID())){
            throw new DataAccessException("Game doesn't exist");
        }
        games.put(game.gameID(), game);
    }


    public void clear(){
        games.clear();
    }



}
