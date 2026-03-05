package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMovesCalculator {

    // private ChessBoard board;
    // private ChessPosition pos;

    public static Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> collection = new ArrayList<>();

        ChessPiece piece = board.getPiece(pos);

        switch (piece.getPieceType()) {
            case PAWN:
                collection.addAll(calculatePawnMoves(board, pos, piece));
                break;
            case ROOK:
                collection.addAll(calculateRookMoves(board, pos, piece));
                break;
            case BISHOP:
                collection.addAll(calculateBishopMoves(board, pos, piece));
                break;
            case QUEEN:
                collection.addAll(calculateQueenMoves(board, pos, piece));
                break;
            case KING:
                collection.addAll(calculateKingMoves(board, pos, piece));
                break;
            case KNIGHT:
                collection.addAll(calculateKnightMoves(board, pos, piece));
                break;
            default:
                throw new Error("ChessMovesCalculator: Illegal pieceType");
        }

        return collection;
    }


    private static ChessPosition offset(ChessPosition startPos, int rowOffset, int colOffset) {
        int r = startPos.getRow();
        int c = startPos.getColumn();
        if (r + rowOffset > 8 || r + rowOffset < 1 || c + colOffset > 8 || c + colOffset < 1) {
            return null;
        }
        return new ChessPosition(startPos.getRow() + rowOffset, startPos.getColumn() + colOffset);
    }


    private static Collection<ChessMove> calculateRookMoves(ChessBoard board, ChessPosition startPos, ChessPiece piece) {
        Collection<ChessMove> collection = new ArrayList<>();
        //                              up,     down,    right,  left
        int [][] possibleDirections = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for(int[] direction : possibleDirections){
            for(int i = 1; i <= 8; i++){
                ChessPosition newPos = offset(startPos, direction[0] * i, direction[1] * i);
                if(!checkAddMove(collection, board, piece, startPos, newPos, true)){
                    break;
                }
            }
        }

        return collection;
    }


    private static Collection<ChessMove> calculateBishopMoves(ChessBoard board, ChessPosition startPos, ChessPiece piece) {
        Collection<ChessMove> collection = new ArrayList<>();

        int [][] possibleDirections = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        for(int[] direction : possibleDirections){
            for(int i = 1; i <= 8; i++){
                ChessPosition newPos = offset(startPos, direction[0] * i, direction[1] * i);
                if(!checkAddMove(collection, board, piece, startPos, newPos, true)){
                    break;
                }
            }
        }

        return collection;
    }


    private static Collection<ChessMove> calculateQueenMoves(ChessBoard board, ChessPosition startPos, ChessPiece piece) {
        Collection<ChessMove> collection = new ArrayList<>();

        collection.addAll(calculateRookMoves(board, startPos, piece));
        collection.addAll(calculateBishopMoves(board, startPos, piece));

        return collection;
    }


    private static Collection<ChessMove> calculateKingMoves(ChessBoard board, ChessPosition startPos, ChessPiece piece) {
        Collection<ChessMove> collection = new ArrayList<>();

        int[][] possibleDirections = {{1,0}, {-1,0}, {0,1}, {0,-1}, {1,1}, {1,-1}, {-1,1}, {-1,-1}};

        for(int[] direction : possibleDirections){
            // no need for inner loop
            ChessPosition newPos = offset(startPos, direction[0], direction[1]);
            checkAddMove(collection, board, piece, startPos, newPos, false); // king doesn't continue after one check
        }

        return collection;
    }


    private static Collection<ChessMove> calculateKnightMoves(ChessBoard board, ChessPosition startPos, ChessPiece piece) {
        Collection<ChessMove> collection = new ArrayList<>();

        int[][] possibleDirections = {{2,1}, {1,2}, {-1,2}, {-2,1}, {-2,-1}, {-1,-2}, {1,-2}, {2,-1}};
        for(int[] direction : possibleDirections){
            ChessPosition newPos = offset(startPos, direction[0], direction[1]);
            checkAddMove(collection, board, piece, startPos, newPos, false); // can't continue after move
        }

        return collection;
    }


    private static Collection<ChessMove> calculatePawnMoves(ChessBoard board, ChessPosition startPos, ChessPiece piece) {
        Collection<ChessMove> collection = new ArrayList<>();

        // moving
        int up;
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            up = 1;
        } else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            up = -1;
        } else {
            throw new Error("ChessMovesCalculator: calculatePawnMoves: everything is wrong");
        } // probably isn't needed

        ChessPosition upOne = offset(startPos, up, 0);
        if (upOne != null && board.getPiece(upOne) == null) {
            addRegularMove(collection, startPos, upOne);

            // up 2 if on start
            int startRow;
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                startRow = 2;
            } else{
                startRow = 7;
            }

            if(startPos.getRow() == startRow){
                ChessPosition upTwo = offset(startPos, up * 2, 0);
                if(upTwo != null && board.getPiece(upTwo) == null){
                    addRegularMove(collection, startPos, upTwo);
                }
            }

        }


        // capturing

        int[] captureDirections = {-1, 1};
        for(int colDir : captureDirections){
            ChessPosition killPos = offset(startPos, up, colDir);
            if(killPos == null){
                continue;
            }
            ChessPiece killPiece = board.getPiece(killPos);
            if(killPiece != null && killPiece.getTeamColor() != piece.getTeamColor()){
                addRegularMove(collection, startPos, killPos);
            }
        }


        return collection;
    }


    private static boolean checkAddMove(Collection<ChessMove> moves,
                                        ChessBoard board,
                                        ChessPiece piece,
                                        ChessPosition startPos,
                                        ChessPosition checkPos,
                                        boolean canContinue){
        if(checkPos == null){
            return false;
        }
        ChessPiece checkPiece = board.getPiece(checkPos);
        if(checkPiece == null){
            moves.add(new ChessMove(startPos, checkPos, null));
            return true;
        } else if (checkPiece.getTeamColor() != piece.getTeamColor()) {
            moves.add(new ChessMove(startPos, checkPos, null));
            return !canContinue;
        }
        return false;
    }


    private static void addRegularMove(Collection<ChessMove> collection,
                                       ChessPosition startPos,
                                       ChessPosition endPos){
        if(endPos == null){ // out of bounds
            return;
        }
        int row = endPos.getRow();
        if(row == 1 || row == 8){
            collection.add(new ChessMove(startPos, endPos, ChessPiece.PieceType.ROOK));
            collection.add(new ChessMove(startPos, endPos, ChessPiece.PieceType.BISHOP));
            collection.add(new ChessMove(startPos, endPos, ChessPiece.PieceType.KNIGHT));
            collection.add(new ChessMove(startPos, endPos, ChessPiece.PieceType.QUEEN));
        } else{
            collection.add(new ChessMove(startPos, endPos, null));
        }
    }

}
