package chess;

import java.util.Collection;

public class PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves{

    }






    private ChessPosition checkUpDown(ChessBoard board, ChessPosition firstPos, ChessPiece piece, int count){
        // check bounds
        if(count > 0 && (firstPos.getColumn() + count) >= 8){
            throw new InvalidMoveException("Trying to move above bounds of the board.");
            return firstPos;
        } else if (count < 0 && (firstPos.getColumn() + count) <= 0){
            throw new InvalidMoveException("Trying ot move below bounds of the board.");
            return firstPos;
        } else if (count == 0){
            throw new InvalidMoveException("Move count can't be zero.");
            return firstPos;
        }

        ChessPosition finalPos = new ChessPosition(firstPos.getRow(), firstPos.getColumn() + count);

        if(board.getPiece(finalPos) == null){
            return finalPos;
        }

        ChessPiece killPiece = board.getPiece(finalPos); // piece isn't actually killed. I just couldn't come up with a better name.
        if(killPiece.getTeamColor() == piece.getTeamColor()){
            throw new InvalidMoveException("Trying to move into a position occupied by same team");
        } else {
            return finalPos;
        }
    }

    private ChessPosition checkLeftRight(ChessBoard board, ChessPosition firstPos, ChessPiece piece, int count){
        // check bounds
        if(count > 0 && (firstPos.getRow() + count) >= 8){
            throw new InvalidMoveException("Trying to move right of bounds of the board.");
            return firstPos;
        } else if (count < 0 && (firstPos.getRow() + count) <= 8){
            throw new InvalidMoveException("Trying to move right of bounds of the board.");
            return firstPos;
        } else if (count == 0){
            throw new InvalidMoveException("Move count can't be zero.");
            return firstPos;
        }

        ChessPosition finalPos = new ChessPosition(firstPos.getRow() + count, firstPos.getColumn());

        if(board.getPiece(finalPos) == null){
            return finalPos;
        }

        ChessPiece killPiece = board.getPiece(finalPos); // piece isn't actually killed. I just couldn't come up with a better name.
        if(killPiece.getTeamColor() == piece.getTeamColor()){
            throw new InvalidMoveException("Trying to move into a position occupied by same team");
        } else{
            return finalPos;
        }
    }

    private ChessPosition checkDiagUpRightDownLeft(ChessBoard board, ChessPosition firstPos, ChessPiece piece, int count){
        // check bounds
        if(count == 0){
            throw new InvalidMoveException("Move count can't be zero.");
        }

        ChessPosition finalPos = new ChessPosition(firstPos.getRow() + count,firstPos.getColumn() + count);
        if(finalPos.getRow() >= 8) {throw new InvalidMoveException("Trying to move above bounds of the board.");}
        else if (finalPos.getRow() < 0) {throw new InvalidMoveException("Trying to move below bounds of the board.");}
        else if (finalPos.getColumn() >= 8) {throw new InvalidMoveException("Trying to move right of bounds of the board.");}
        else if (finalPos.getColumn() < 0) {throw new InvalidMoveException("Trying to move left of bounds of the board.");}

        if(board.getPiece(finalPos) == null){
            return finalPos;
        }

        ChessPiece killPiece = board.getPiece(finalPos); // piece isn't actually killed. I just couldn't come up with a better name.
        if(killPiece.getTeamColor() == piece.getTeamColor()){
            throw new InvalidMoveException("Trying to move into a position occupied by same team");
        } else{
            return finalPos;
        }
    }

    private ChessPosition checkDiagUpLeftDownRight(ChessBoard board, ChessPosition firstPos, ChessPiece piece, int count){
        // check bounds
        if(count == 0){
            throw new InvalidMoveException("Move count can't be zero.");
        }

        ChessPosition finalPos = new ChessPosition(firstPos.getRow() - count,firstPos.getColumn() + count);
        if(finalPos.getRow() >= 8) {throw new InvalidMoveException("Trying to move above bounds of the board.");}
        else if (finalPos.getRow() < 0) {throw new InvalidMoveException("Trying to move below bounds of the board.");}
        else if (finalPos.getColumn() >= 8) {throw new InvalidMoveException("Trying to move right of bounds of the board.");}
        else if (finalPos.getColumn() < 0) {throw new InvalidMoveException("Trying to move left of bounds of the board.");}

        if(board.getPiece(finalPos) == null){
            return finalPos;
        }

        ChessPiece killPiece = board.getPiece(finalPos); // piece isn't actually killed. I just couldn't come up with a better name.
        if(killPiece.getTeamColor() == piece.getTeamColor()){
            throw new InvalidMoveException("Trying to move into a position occupied by same team");
        } else{
            return finalPos;
        }
    }

    /**
     * Checks possible moves for Knight pieces. Count can be 1-8, beginning with top right and going clockwise to top left.
     * @param board a chessBoard object with the piece on it
     * @param firstPos starting position of the piece
     * @param piece the piece to move
     * @param count which of the valid 8 spaces to move (1 for top-right, clockwise until 8 for top-left)
     * @return a valid position
     */
    private ChessPosition checkKnightDirections(ChessBoard board, ChessPosition firstPos, ChessPiece piece, int count){
        // check bounds
        if(count == 0){
            throw new InvalidMoveException("Move count can't be zero.");
        } else if(count < 0 || count > 8){
            throw new InvalidMoveException("Move count " + count + " out of bounds 1 - 8 for Knight pieces");
        }

        ChessPosition finalPos;
        int firstRow = firstPos.getRow();
        int firstCol = firstPos.getColumn();

        switch(count){
            case 1:
                finalPos = new ChessPosition(firstRow + 2, firstCol + 1);
                break;
            case 2:
                finalPos = new ChessPosition(firstRow + 1, firstCol + 2);
                break;
            case 3:
                finalPos = new ChessPosition(firstRow - 1, firstCol + 2);
                break;
            case 4:
                finalPos = new ChessPosition(firstRow - 2, firstCol + 1);
                break;
            case 5:
                finalPos = new ChessPosition(firstRow - 2, firstCol - 1);
                break;
            case 6:
                finalPos = new ChessPosition(firstRow - 1, firstCol - 2);
                break;
            case 7:
                finalPos = new ChessPosition(firstRow + 1, firstCol - 2);
                break;
            case 8:
                finalPos = new ChessPosition(firstRow + 2, firstCol - 1);
                break;
            default:
                throw new InvalidMoveException("Move count invalid.");
        }

        if(finalPos.getRow() >= 8) {throw new InvalidMoveException("Trying to move above bounds of the board.");}
        else if (finalPos.getRow() < 0) {throw new InvalidMoveException("Trying to move below bounds of the board.");}
        else if (finalPos.getColumn() >= 8) {throw new InvalidMoveException("Trying to move right of bounds of the board.");}
        else if (finalPos.getColumn() < 0) {throw new InvalidMoveException("Trying to move left of bounds of the board.");}

        if(board.getPiece(finalPos) == null){
            return finalPos;
        }

        ChessPiece killPiece = board.getPiece(finalPos); // piece isn't actually killed. I just couldn't come up with a better name.
        if(killPiece.getTeamColor() == piece.getTeamColor()){
            throw new InvalidMoveException("Trying to move into a position occupied by same team");
        } else{
            return finalPos;
        }

    }
}
