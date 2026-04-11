package websocket.commands;

import chess.ChessMove;

// Making a version of UserGameCommand with a ChessMove field for WebsocketHandler.

public class MoveCommand extends UserGameCommand{
    private final ChessMove chessMove;

    public MoveCommand(String authToken, int gameID, ChessMove chessMove){
        super(CommandType.MAKE_MOVE, authToken, gameID);

        this.chessMove = chessMove;
    }

    public ChessMove getMove(){
        return chessMove;
    }
}
