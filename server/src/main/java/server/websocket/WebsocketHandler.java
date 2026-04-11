package server.websocket;


import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.DataAccessException;
import dataaccess.DatabaseAuthTokenDao;
import dataaccess.DatabaseGameDao;
import io.javalin.websocket.*;

import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class WebsocketHandler {
    private final Gson gson;
    private final ConnectionManager connectionManager;

    private final DatabaseAuthTokenDao authDao;
    private final DatabaseGameDao gameDao;

    private final Map<WsContext, Integer> gameCtxMap;

    public WebsocketHandler(DatabaseAuthTokenDao authDao, DatabaseGameDao gameDao){
        this.gson = new GsonBuilder().serializeNulls().create();
        this.connectionManager = new ConnectionManager();
        this.gameCtxMap = new ConcurrentHashMap<>();

        this.authDao = authDao;
        this.gameDao = gameDao;
    }

    public void handleMessage(WsMessageContext ctx){
        UserGameCommand command;
        try{
            command = gson.fromJson(ctx.message(), UserGameCommand.class);

            if(command == null){
                sendError(ctx, "Error: invalid command");
                return;
            }

            switch(command.getCommandType()){
                case CONNECT -> handleConnect(ctx, command);
                case LEAVE -> handleLeave(ctx, command);
                case RESIGN -> handleResign(ctx, command);
                case MAKE_MOVE -> handleMakeMove(ctx, command);
                default -> sendError(ctx, "Error: bad command");
            }

        }catch(Exception ex){
            sendError(ctx, "Error: invalid message");
        }
    }


    private void handleConnect(WsContext ctx, UserGameCommand command){

        if(command.getAuthToken() == null){
            sendError(ctx, "Error: missing authToken");
            return;
        }
        if(command.getGameID() == null){
            sendError(ctx, "Error: missing gameID");
            return;
        }

        AuthData auth;
        GameData game;

        try{
            auth = authDao.getAuth(command.getAuthToken());
            game = gameDao.getGame(command.getGameID());
        }catch (DataAccessException ex){
            sendError(ctx, "Error: invalid");
            return;
        }

        if(auth == null){
            sendError(ctx, "Error: missing authToken."); // . to distinguish from above
            return;
        }
        if(game == null){
            sendError(ctx, "Error: missing gameID.");
            return;
        }

        gameCtxMap.put(ctx, command.getGameID());
        connectionManager.add(command.getGameID(), ctx);

        sendGame(ctx, game);
        connectionManager.broadcastExclude(
                ctx,
                command.getGameID(),
                new NotificationMessage(auth.userName() + " is now connected"));

    }


    private void handleLeave(WsContext ctx, UserGameCommand command){

        if(!gameCtxMap.containsKey(ctx)){
            sendError(ctx, "Error: not connected");
            return;
        }
        int gameID = gameCtxMap.remove(ctx); // returns value of ctx

        AuthData auth;
        GameData game;
        try{
            auth = authDao.getAuth(command.getAuthToken());
            game = gameDao.getGame(gameID);
        }catch(DataAccessException ex){
            sendError(ctx, "Error: invalid");
            return;
        }

        if(auth == null || game == null){
            sendError(ctx, "Error: invalid.");
            return;
        }

        connectionManager.remove(gameID, ctx);

        try{
            gameDao.updateGame(killPlayer(game, auth.userName()));
        }catch(DataAccessException ex){
            sendError(ctx, "Error: couldn't update");
            return;
        }

        connectionManager.broadcastExclude(ctx, gameID, new NotificationMessage(auth.userName() + " left"));

    }


    private void handleResign(WsContext ctx, UserGameCommand command){
        if(!gameCtxMap.containsKey(ctx)){
            sendError(ctx, "Error: not connected");
            return;
        }
        int gameID = gameCtxMap.get(ctx);

        AuthData auth;
        GameData game;

        try{
            auth = authDao.getAuth(command.getAuthToken());
            game = gameDao.getGame(gameID);
        }catch(DataAccessException ex){
            sendError(ctx, "Error: invalid");
            return;
        }

        if(auth == null || game == null){
            sendError(ctx, "Error: invalid.");
            return;
        }

        // Spec doesn't mention marking game as finished.
        connectionManager.broadcastAll(gameID, new NotificationMessage(auth.userName() + " resigned"));

    }


    private void handleMakeMove(WsContext ctx, UserGameCommand command){
        if(!gameCtxMap.containsKey(ctx)){
            sendError(ctx, "Error: not connected");
            return;
        }
        int gameID = gameCtxMap.get(ctx);

        AuthData auth;
        GameData game;

        try{
            auth = authDao.getAuth(command.getAuthToken());
            game = gameDao.getGame(gameID);
        } catch (DataAccessException ex) {
            sendError(ctx, "Error: invalid make move");
            return;
        }

        if(auth == null || game == null){
            sendError(ctx, "Error: invalid make move.");
            return;
        }

        ChessGame chessGame = game.chessGame();
        ChessMove chessMove = grabMove(command);
        if(chessMove == null){
            sendError(ctx, "Error: no move");
            return;
        }

        try{
            chessGame.makeMove(chessMove);
        }catch(InvalidMoveException ex){
            sendError(ctx, "Error: invalid move");
            return;
        }

        GameData newGame = new GameData(
                game.gameID(),
                game.whiteUsername(),
                game.blackUsername(),
                game.gameName(),
                chessGame);

        try{
            gameDao.updateGame(newGame);
        } catch (DataAccessException ex) {
            sendError(ctx, "Error: couldn't update game");
            return;
        }

        connectionManager.broadcastAll(gameID, new LoadGameMessage(newGame));
        connectionManager.broadcastExclude(ctx, gameID, new NotificationMessage(auth.userName() + " made move"));

        sendGameNotification(gameID, newGame);

    }



    private void sendError(WsContext ctx, String message){
        ErrorMessage error = new ErrorMessage(message);
        ctx.send(gson.toJson(error));
    }

    private void sendGame(WsContext ctx, GameData game){

    }

    private GameData killPlayer(GameData game, String username){

    }
}







/*


    public WebsocketHandler(){
        this.cons = new ConnectionManager();
        this.gson = new GsonBuilder().serializeNulls().create();
    }

    public void handleConnect(WsContext ctx){
        System.out.println("Websocket connected");
        // ctx.enableAutomaticPings();
    }

    public void handleClose(WsContext ctx){
        System.out.println("Websocket closed");
    }

    public void handleMessage(WsMessageContext ctx){
        try{
            UserGameCommand command = gson.fromJson(ctx.message(), UserGameCommand.class);

            if(command == null){
                // send error
                sendError(ctx, "Error: invalid");
                return;
            }
            switch(command.getCommandType()){
                case CONNECT:
                    handleConnectCommand(ctx, command);
            }

        }
    }



    private void handleConnectCommand(WsContext ctx, UserGameCommand command){

        Integer gameID = command.getGameID();
        if(gameID == null){
            sendError(ctx, "Error: invalid gameID");
            return;
        }




    }



    private void sendError(WsContext ctx, String message){
        ErrorMessage error = new ErrorMessage(message);
        ctx.send(gson.toJson(error));
    }

 */