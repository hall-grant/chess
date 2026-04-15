package server.websocket;


import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dataaccess.DataAccessException;
import dataaccess.DatabaseAuthTokenDao;
import dataaccess.DatabaseGameDao;
import io.javalin.websocket.*;

import model.AuthData;
import model.GameData;
import websocket.commands.MoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        try{
            // for formatting chessMove within the ctx without jankiness
            JsonObject jObj = gson.fromJson(ctx.message(), JsonObject.class);
            if(jObj == null){
                sendError(ctx, "Error: invalid command");
                return;
            }

            String commandType = jObj.get("commandType").getAsString();

            switch(commandType){
                case "CONNECT" -> handleConnect(ctx, gson.fromJson(jObj, UserGameCommand.class));
                case "LEAVE" -> handleLeave(ctx, gson.fromJson(jObj, UserGameCommand.class));
                case "RESIGN" -> handleResign(ctx, gson.fromJson(jObj, UserGameCommand.class));
                case "MAKE_MOVE" -> handleMakeMove(ctx, gson.fromJson(jObj, MoveCommand.class));
                default -> sendError(ctx, "Error: bad command");
            }
        } catch (Exception ex) {
            sendError(ctx, "Error: invalid message");
        }
    }


    // I don't see why this is even needed. Doesn't do anything.
    public void onConnect(WsContext ctx){
        System.out.println("Websocket connected");
    }


    public void handleConnect(WsContext ctx, UserGameCommand command){

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

        ctx.send(gson.toJson(new LoadGameMessage(game)));
        connectionManager.broadcastExclude(
                ctx,
                command.getGameID(),
                new NotificationMessage(auth.userName() + " is now connected"));

    }


    public void handleClose(WsContext ctx){
        Integer gameID = gameCtxMap.remove(ctx); // Integer type for nullable, remove returns value of ctx
        if(gameID != null){
            connectionManager.remove(gameID, ctx);
        }
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

        // make sure observers can't resign
        if(!auth.userName().equals(game.whiteUsername()) && !auth.userName().equals(game.blackUsername())){
            sendError(ctx,"Error: observer can't resign");
            return;
        }

        // check if game is already resigned
        if(game.gameOver()){
            sendError(ctx, "Error: game already resigned");
            return;
        }


        // actually set game to gameOver
        ChessGame chessGame = game.chessGame();
        GameData newGame = new GameData(
                game.gameID(),
                game.whiteUsername(),
                game.blackUsername(),
                game.gameName(),
                chessGame, true);

        try{
            gameDao.updateGame(newGame);
        }catch(DataAccessException ex){
            sendError(ctx, "Error: couldn't update game");
            return;
        }


        connectionManager.broadcastAll(gameID, new NotificationMessage(auth.userName() + " resigned"));

    }


    private void handleMakeMove(WsContext ctx, MoveCommand command){
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

        // make sure observers can't make moves
        if(!auth.userName().equals(game.whiteUsername()) && !auth.userName().equals(game.blackUsername())){
            sendError(ctx,"Error: observer can't make moves");
            return;
        }

        // make sure correct turn
        if(auth.userName().equals(game.whiteUsername())){
            if(game.chessGame().getTeamTurn() != ChessGame.TeamColor.WHITE){
                sendError(ctx, "Error: incorrect turn");
                return;
            }
        }else{
            if(game.chessGame().getTeamTurn() != ChessGame.TeamColor.BLACK){
                sendError(ctx, "Error: incorrect turn");
                return;
            }
        }

        if(game.gameOver()){
            sendError(ctx, "Error: game already resigned");
            return;
        }


        ChessGame chessGame = game.chessGame();
        ChessMove chessMove = command.getMove();
        if(chessMove == null){
            sendError(ctx, "Error: no move");
            return;
        }

        // make sure not moving opponent piece or no piece
        ChessPiece piece = game.chessGame().getBoard().getPiece(chessMove.getStartPosition());
        ChessGame.TeamColor pColor;
        if(auth.userName().equals(game.whiteUsername())){
            pColor = ChessGame.TeamColor.WHITE;
        } else {
            pColor = ChessGame.TeamColor.BLACK;
        }
        if(piece == null || piece.getTeamColor() != pColor){
            sendError(ctx, "Error: can't move opponent's piece or no piece");
            return;
        }


        try{
            chessGame.makeMove(chessMove);
        }catch(InvalidMoveException ex){
            sendError(ctx, "Error: invalid move");
            return;
        }

        GameData newGame = new GameData(game.gameID(),
                game.whiteUsername(),
                game.blackUsername(),
                game.gameName(),
                chessGame, false);

        try{
            gameDao.updateGame(newGame);
        } catch (DataAccessException ex) {
            sendError(ctx, "Error: couldn't update game");
            return;
        }


        ctx.send(gson.toJson(new LoadGameMessage(newGame)));

        connectionManager.broadcastExclude(ctx, gameID, new LoadGameMessage(newGame));
        connectionManager.broadcastExclude(ctx, gameID, new NotificationMessage(auth.userName() + " made move"));

        sendGameNotification(gameID, newGame);

    }



    private void sendError(WsContext ctx, String message){
        ErrorMessage error = new ErrorMessage(message);
        ctx.send(gson.toJson(error));
    }

    private GameData killPlayer(GameData game, String username){
        String white = game.whiteUsername();
        String black = game.blackUsername();

        if (username == null){
            return game;
        }
        if(username.equals(white)){
            return new GameData(game.gameID(), null, black, game.gameName(), game.chessGame(), false);
        }
        if (username.equals(black)) {
            return new GameData(game.gameID(), white, null, game.gameName(), game.chessGame(), false);
        }
        return game;
    }

    private void sendGameNotification(int gameID, GameData game){
        ChessGame chessGame = game.chessGame();

        // Spec doesn't say to broadcast messages specific to each party
        if(chessGame.isInCheckmate(ChessGame.TeamColor.WHITE)){
            connectionManager.broadcastAll(gameID, new NotificationMessage("White is in checkmate."));
        }
        else if(chessGame.isInCheckmate(ChessGame.TeamColor.BLACK)){
            connectionManager.broadcastAll(gameID, new NotificationMessage("Black is in checkmate."));
        }
        else if(chessGame.isInStalemate(ChessGame.TeamColor.WHITE)){
            connectionManager.broadcastAll(gameID, new NotificationMessage("White is in stalemate."));
        }
        else if(chessGame.isInStalemate(ChessGame.TeamColor.BLACK)){
            connectionManager.broadcastAll(gameID, new NotificationMessage("Black is in stalemate."));
        }
        else if(chessGame.isInCheck(ChessGame.TeamColor.WHITE)){
            connectionManager.broadcastAll(gameID, new NotificationMessage("White is in check."));
        }
        else if(chessGame.isInCheck(ChessGame.TeamColor.BLACK)){
            connectionManager.broadcastAll(gameID, new NotificationMessage("Black is in check."));
        }
    }

}

