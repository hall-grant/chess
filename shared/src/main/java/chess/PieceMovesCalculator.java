package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMovesCalculator {

    // private ChessBoard board;
    // private ChessPosition pos;

    public static Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition pos){
        Collection<ChessMove> collection = new ArrayList<>();

        ChessPiece piece = board.getPiece(pos);

        switch(piece.getPieceType()){
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



    private static ChessPosition offset(ChessPosition startPos, int rowOffset, int colOffset){
        int r = startPos.getRow();
        int c = startPos.getColumn();
        if(r + rowOffset > 8 || r + rowOffset < 1 || c + colOffset > 8 || c + colOffset < 1){
            return null;
        }
        return new ChessPosition(startPos.getRow() + rowOffset, startPos.getColumn() + colOffset);
    }




    private static Collection<ChessMove> calculateRookMoves(ChessBoard board, ChessPosition startPos, ChessPiece piece){
        Collection<ChessMove> collection = new ArrayList<>();

        // up
        for(int i = 1; i <= 8; i++){
            ChessPosition checkPos = offset(startPos, 0, i);
            if(checkPos == null) break; // out of bounds
            // no piece on square
            if(board.getPiece(checkPos) == null) collection.add(new ChessMove(startPos, checkPos, null));
                // piece occupying target square is same color. abort.
            else if(board.getPiece(checkPos).getTeamColor() == piece.getTeamColor()) break;
                // piece occupying target square is different color.
            else if(board.getPiece(checkPos).getTeamColor() != piece.getTeamColor()){
                collection.add(new ChessMove(startPos, checkPos, null));
                break;
            }
        }

        // down
        for(int i = 1; i <= 8; i++){
            ChessPosition checkPos = offset(startPos, 0, -i);
            if(checkPos == null) break; // out of bounds
            // no piece on square
            if(board.getPiece(checkPos) == null) collection.add(new ChessMove(startPos, checkPos, null));
                // piece occupying target square is same color. abort.
            else if(board.getPiece(checkPos).getTeamColor() == piece.getTeamColor()) break;
                // piece occupying target square is different color.
            else if(board.getPiece(checkPos).getTeamColor() != piece.getTeamColor()){
                collection.add(new ChessMove(startPos, checkPos, null));
                break;
            }
        }

        // right
        for(int i = 1; i <= 8; i++){
            ChessPosition checkPos = offset(startPos, i, 0);
            if(checkPos == null) break; // out of bounds
            // no piece on square
            if(board.getPiece(checkPos) == null) collection.add(new ChessMove(startPos, checkPos, null));
                // piece occupying target square is same color. abort.
            else if(board.getPiece(checkPos).getTeamColor() == piece.getTeamColor()) break;
                // piece occupying target square is different color.
            else if(board.getPiece(checkPos).getTeamColor() != piece.getTeamColor()){
                collection.add(new ChessMove(startPos, checkPos, null));
                break;
            }
        }

        // left
        for(int i = 1; i <= 8; i++){
            ChessPosition checkPos = offset(startPos, -i, 0);
            if(checkPos == null) break; // out of bounds
            // no piece on square
            if(board.getPiece(checkPos) == null) collection.add(new ChessMove(startPos, checkPos, null));
                // piece occupying target square is same color. abort.
            else if(board.getPiece(checkPos).getTeamColor() == piece.getTeamColor()) break;
                // piece occupying target square is different color.
            else if(board.getPiece(checkPos).getTeamColor() != piece.getTeamColor()){
                collection.add(new ChessMove(startPos, checkPos, null));
                break;
            }
        }

        return collection;
    }


    private static Collection<ChessMove> calculateBishopMoves(ChessBoard board, ChessPosition startPos, ChessPiece piece){
        Collection<ChessMove> collection = new ArrayList<>();

        // up-right
        for(int i = 1; i <= 8; i++){
            ChessPosition checkPos = offset(startPos, i, i);
            if(checkPos == null) break; // out of bounds
            // no piece on square
            if(board.getPiece(checkPos) == null) collection.add(new ChessMove(startPos, checkPos, null));
                // piece occupying target square is same color. abort.
            else if(board.getPiece(checkPos).getTeamColor() == piece.getTeamColor()) break;
                // piece occupying target square is different color.
            else if(board.getPiece(checkPos).getTeamColor() != piece.getTeamColor()){
                collection.add(new ChessMove(startPos, checkPos, null));
                break;
            }
        }

        // up-left
        for(int i = 1; i <= 8; i++){
            ChessPosition checkPos = offset(startPos, i, -i);
            if(checkPos == null) break; // out of bounds
            // no piece on square
            if(board.getPiece(checkPos) == null) collection.add(new ChessMove(startPos, checkPos, null));
                // piece occupying target square is same color. abort.
            else if(board.getPiece(checkPos).getTeamColor() == piece.getTeamColor()) break;
                // piece occupying target square is different color.
            else if(board.getPiece(checkPos).getTeamColor() != piece.getTeamColor()){
                collection.add(new ChessMove(startPos, checkPos, null));
                break;
            }
        }

        // down-right
        for(int i = 1; i <= 8; i++){
            ChessPosition checkPos = offset(startPos, -i, i);
            if(checkPos == null) break; // out of bounds
            // no piece on square
            if(board.getPiece(checkPos) == null) collection.add(new ChessMove(startPos, checkPos, null));
                // piece occupying target square is same color. abort.
            else if(board.getPiece(checkPos).getTeamColor() == piece.getTeamColor()) break;
                // piece occupying target square is different color.
            else if(board.getPiece(checkPos).getTeamColor() != piece.getTeamColor()){
                collection.add(new ChessMove(startPos, checkPos, null));
                break;
            }
        }

        // down-left
        for(int i = 1; i <= 8; i++){
            ChessPosition checkPos = offset(startPos, -i, -i);
            if(checkPos == null) break; // out of bounds
            // no piece on square
            if(board.getPiece(checkPos) == null) collection.add(new ChessMove(startPos, checkPos, null));
                // piece occupying target square is same color. abort.
            else if(board.getPiece(checkPos).getTeamColor() == piece.getTeamColor()) break;
                // piece occupying target square is different color.
            else if(board.getPiece(checkPos).getTeamColor() != piece.getTeamColor()){
                collection.add(new ChessMove(startPos, checkPos, null));
                break;
            }
        }

        return collection;
    }


    private static Collection<ChessMove> calculateQueenMoves(ChessBoard board, ChessPosition startPos, ChessPiece piece){
        Collection<ChessMove> collection = new ArrayList<>();

        collection.addAll(calculateRookMoves(board, startPos, piece));
        collection.addAll(calculateBishopMoves(board, startPos, piece));

        return collection;
    }


    private static Collection<ChessMove> calculateKingMoves(ChessBoard board, ChessPosition startPos, ChessPiece piece){
        Collection<ChessMove> collection = new ArrayList<>();

        ChessPosition checkPos;

        // up
        checkPos = offset(startPos, 1, 0);
        if(checkPos != null) { // if not out of bounds
            // no piece on square
            if (board.getPiece(checkPos) == null) collection.add(new ChessMove(startPos, checkPos, null));
                // piece occupying target square is different color.
            else if (board.getPiece(checkPos).getTeamColor() != piece.getTeamColor()) {
                collection.add(new ChessMove(startPos, checkPos, null));
            }
        }

        // down
        checkPos = offset(startPos, -1, 0);
        if(checkPos != null) { // if not out of bounds
            // no piece on square
            if (board.getPiece(checkPos) == null) collection.add(new ChessMove(startPos, checkPos, null));
                // piece occupying target square is different color.
            else if (board.getPiece(checkPos).getTeamColor() != piece.getTeamColor()) {
                collection.add(new ChessMove(startPos, checkPos, null));
            }
        }

        // right
        checkPos = offset(startPos, 0, 1);
        if(checkPos != null) { // if not out of bounds
            // no piece on square
            if (board.getPiece(checkPos) == null) collection.add(new ChessMove(startPos, checkPos, null));
                // piece occupying target square is different color.
            else if (board.getPiece(checkPos).getTeamColor() != piece.getTeamColor()) {
                collection.add(new ChessMove(startPos, checkPos, null));
            }
        }

        // left
        checkPos = offset(startPos, 0, -1);
        if(checkPos != null) { // if not out of bounds
            // no piece on square
            if (board.getPiece(checkPos) == null) collection.add(new ChessMove(startPos, checkPos, null));
                // piece occupying target square is different color.
            else if (board.getPiece(checkPos).getTeamColor() != piece.getTeamColor()) {
                collection.add(new ChessMove(startPos, checkPos, null));
            }
        }

        // up-right
        checkPos = offset(startPos, 1, 1);
        if(checkPos != null) { // if not out of bounds
            // no piece on square
            if (board.getPiece(checkPos) == null) collection.add(new ChessMove(startPos, checkPos, null));
                // piece occupying target square is different color.
            else if (board.getPiece(checkPos).getTeamColor() != piece.getTeamColor()) {
                collection.add(new ChessMove(startPos, checkPos, null));
            }
        }

        // up-left
        checkPos = offset(startPos, 1, -1);
        if(checkPos != null) { // if not out of bounds
            // no piece on square
            if (board.getPiece(checkPos) == null) collection.add(new ChessMove(startPos, checkPos, null));
                // piece occupying target square is different color.
            else if (board.getPiece(checkPos).getTeamColor() != piece.getTeamColor()) {
                collection.add(new ChessMove(startPos, checkPos, null));
            }
        }

        // down-right
        checkPos = offset(startPos, -1, 1);
        if(checkPos != null) { // if not out of bounds
            // no piece on square
            if (board.getPiece(checkPos) == null) collection.add(new ChessMove(startPos, checkPos, null));
                // piece occupying target square is different color.
            else if (board.getPiece(checkPos).getTeamColor() != piece.getTeamColor()) {
                collection.add(new ChessMove(startPos, checkPos, null));
            }
        }

        // down-left
        checkPos = offset(startPos, -1, -1);
        if(checkPos != null) { // if not out of bounds
            // no piece on square
            if (board.getPiece(checkPos) == null) collection.add(new ChessMove(startPos, checkPos, null));
                // piece occupying target square is different color.
            else if (board.getPiece(checkPos).getTeamColor() != piece.getTeamColor()) {
                collection.add(new ChessMove(startPos, checkPos, null));
            }
        }

        return collection;
    }


    private static Collection<ChessMove> calculateKnightMoves(ChessBoard board, ChessPosition startPos, ChessPiece piece){
        Collection<ChessMove> collection = new ArrayList<>();

        ChessPosition checkPos;

        // up 2, right 1
        checkPos = offset(startPos, 2, 1);
        if(checkPos != null) { // if not out of bounds
            // no piece on square
            if (board.getPiece(checkPos) == null) collection.add(new ChessMove(startPos, checkPos, null));
                // piece occupying target square is different color.
            else if (board.getPiece(checkPos).getTeamColor() != piece.getTeamColor()) {
                collection.add(new ChessMove(startPos, checkPos, null));
            }
        }

        // up 1, right 2
        checkPos = offset(startPos, 1, 2);
        if(checkPos != null) { // if not out of bounds
            // no piece on square
            if (board.getPiece(checkPos) == null) collection.add(new ChessMove(startPos, checkPos, null));
                // piece occupying target square is different color.
            else if (board.getPiece(checkPos).getTeamColor() != piece.getTeamColor()) {
                collection.add(new ChessMove(startPos, checkPos, null));
            }
        }

        // down 1, right 2
        checkPos = offset(startPos, -1, 2);
        if(checkPos != null) { // if not out of bounds
            // no piece on square
            if (board.getPiece(checkPos) == null) collection.add(new ChessMove(startPos, checkPos, null));
                // piece occupying target square is different color.
            else if (board.getPiece(checkPos).getTeamColor() != piece.getTeamColor()) {
                collection.add(new ChessMove(startPos, checkPos, null));
            }
        }

        // down 2, right 1
        checkPos = offset(startPos, -2, 1);
        if(checkPos != null) { // if not out of bounds
            // no piece on square
            if (board.getPiece(checkPos) == null) collection.add(new ChessMove(startPos, checkPos, null));
                // piece occupying target square is different color.
            else if (board.getPiece(checkPos).getTeamColor() != piece.getTeamColor()) {
                collection.add(new ChessMove(startPos, checkPos, null));
            }
        }

        // down 2, left 1
        checkPos = offset(startPos, -2, -1);
        if(checkPos != null) { // if not out of bounds
            // no piece on square
            if (board.getPiece(checkPos) == null) collection.add(new ChessMove(startPos, checkPos, null));
                // piece occupying target square is different color.
            else if (board.getPiece(checkPos).getTeamColor() != piece.getTeamColor()) {
                collection.add(new ChessMove(startPos, checkPos, null));
            }
        }

        // down 1, left 2
        checkPos = offset(startPos, -1, -2);
        if(checkPos != null) { // if not out of bounds
            // no piece on square
            if (board.getPiece(checkPos) == null) collection.add(new ChessMove(startPos, checkPos, null));
                // piece occupying target square is different color.
            else if (board.getPiece(checkPos).getTeamColor() != piece.getTeamColor()) {
                collection.add(new ChessMove(startPos, checkPos, null));
            }
        }

        // up 1, left 2
        checkPos = offset(startPos, 1, -2);
        if(checkPos != null) { // if not out of bounds
            // no piece on square
            if (board.getPiece(checkPos) == null) collection.add(new ChessMove(startPos, checkPos, null));
                // piece occupying target square is different color.
            else if (board.getPiece(checkPos).getTeamColor() != piece.getTeamColor()) {
                collection.add(new ChessMove(startPos, checkPos, null));
            }
        }

        // up 2, left 1
        checkPos = offset(startPos, 2, -1);
        if(checkPos != null) { // if not out of bounds
            // no piece on square
            if (board.getPiece(checkPos) == null) collection.add(new ChessMove(startPos, checkPos, null));
                // piece occupying target square is different color.
            else if (board.getPiece(checkPos).getTeamColor() != piece.getTeamColor()) {
                collection.add(new ChessMove(startPos, checkPos, null));
            }
        }


        return collection;
    }


    private static Collection<ChessMove> calculatePawnMoves(ChessBoard board, ChessPosition startPos, ChessPiece piece){
        Collection<ChessMove> collection = new ArrayList<>();

        // moving
        int up;
        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            up = 1;
        } else if(piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            up = -1;
        } else{ throw new Error("ChessMovesCalculator: calculatePawnMoves: everything is wrong");} // probably isn't needed

        ChessPosition upOne = offset(startPos, up, 0);
        if(upOne != null) {
            if (board.getPiece(upOne) == null){

                // pawn promotion
                if(upOne.getRow() == 8 || upOne.getRow() == 1){
                    collection.add(new ChessMove(startPos, upOne, ChessPiece.PieceType.ROOK));
                    collection.add(new ChessMove(startPos, upOne, ChessPiece.PieceType.BISHOP));
                    collection.add(new ChessMove(startPos, upOne, ChessPiece.PieceType.KNIGHT));
                    collection.add(new ChessMove(startPos, upOne, ChessPiece.PieceType.QUEEN));
                }else {

                    // non-promotion
                    collection.add(new ChessMove(startPos, upOne, null));

                    // if on starting square
                    if((piece.getTeamColor() == ChessGame.TeamColor.WHITE && startPos.getRow() == 2) ||
                            (piece.getTeamColor() == ChessGame.TeamColor.BLACK && startPos.getRow() == 7)){
                        ChessPosition upTwo = offset(startPos, up + up, 0);
                        if(upTwo != null && board.getPiece(upTwo) == null){
                            collection.add(new ChessMove(startPos, upTwo, null));
                        }
                    }

                }
            }
        }

        // capturing left
        if(startPos.getColumn() >= 2){
            ChessPosition checkPos = offset(startPos, up, -1);
            if(checkPos != null){
                if(board.getPiece(checkPos) != null && board.getPiece(checkPos).getTeamColor() != piece.getTeamColor()){
                    if((piece.getTeamColor() == ChessGame.TeamColor.WHITE && checkPos.getRow() == 8) ||
                            (piece.getTeamColor() == ChessGame.TeamColor.BLACK && checkPos.getRow() == 1)){
                        collection.add(new ChessMove(startPos, checkPos, ChessPiece.PieceType.ROOK));
                        collection.add(new ChessMove(startPos, checkPos, ChessPiece.PieceType.BISHOP));
                        collection.add(new ChessMove(startPos, checkPos, ChessPiece.PieceType.KNIGHT));
                        collection.add(new ChessMove(startPos, checkPos, ChessPiece.PieceType.QUEEN));
                    }

                    // non-promotion
                    else{collection.add(new ChessMove(startPos, checkPos, null));}
                }
            }
        }

        // capturing right
        if(startPos.getColumn() <= 7){
            ChessPosition checkPos = offset(startPos, up, 1);
            if(checkPos != null){
                if(board.getPiece(checkPos) != null && board.getPiece(checkPos).getTeamColor() != piece.getTeamColor()){
                    if((piece.getTeamColor() == ChessGame.TeamColor.WHITE && checkPos.getRow() == 8) ||
                            (piece.getTeamColor() == ChessGame.TeamColor.BLACK && checkPos.getRow() == 1)){
                        collection.add(new ChessMove(startPos, checkPos, ChessPiece.PieceType.ROOK));
                        collection.add(new ChessMove(startPos, checkPos, ChessPiece.PieceType.BISHOP));
                        collection.add(new ChessMove(startPos, checkPos, ChessPiece.PieceType.KNIGHT));
                        collection.add(new ChessMove(startPos, checkPos, ChessPiece.PieceType.QUEEN));
                    }

                    // non-promotion
                    else{collection.add(new ChessMove(startPos, checkPos, null));}
                }
            }
        }


        return collection;
    }


}
