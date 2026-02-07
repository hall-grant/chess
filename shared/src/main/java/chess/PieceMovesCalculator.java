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


/*package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMovesCalculator {

    public static Collection<ChessMove> chessMoveCollection(ChessBoard board, ChessPosition position){
        Collection<ChessMove> collection = new ArrayList<>();

        ChessPiece piece = board.getPiece(position);

        // Collection<ChessPosition> pawnCol = pawnCalculator(board, position, piece);

        if      (piece.getPieceType() == ChessPiece.PieceType.PAWN) collection.addAll(pawnCalculator(board, position, piece));
        else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) collection.addAll(rookCalculator(board, position, piece));
        else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) collection.addAll(bishopCalculator(board, position, piece));
        else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) collection.addAll(queenCalculator(board, position, piece));
        else if (piece.getPieceType() == ChessPiece.PieceType.KING) collection.addAll(kingCalculator(board, position, piece));
        else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) collection.addAll(knightCalculator(board, position, piece));
        else{
            System.out.println("Invalid pieceType.");
        }

        return collection;
    }

    // general purpose, should be much more readable
    private static ChessPosition offset(ChessPosition start, int row, int col){
        int r = start.getRow() + row;
        int c = start.getColumn() + col;
        if(r >= 1 && r <= 8 && c >= 1 && c <= 8) return new ChessPosition(r, c);
        return null;
    }

    private static Collection<ChessMove> pawnCalculator(ChessBoard board, ChessPosition firstPos, ChessPiece piece){
        Collection<ChessMove> collection = new ArrayList<>();

        // white pawns
        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            ChessPosition upOne = offset(firstPos, 1, 0);
            if(upOne != null && board.getPiece(upOne) == null){


                // promotions
                if(upOne.getRow() == 8){
                    collection.add(new ChessMove(firstPos, upOne, ChessPiece.PieceType.QUEEN));
                    collection.add(new ChessMove(firstPos, upOne, ChessPiece.PieceType.KNIGHT));
                    collection.add(new ChessMove(firstPos, upOne, ChessPiece.PieceType.BISHOP));
                    collection.add(new ChessMove(firstPos, upOne, ChessPiece.PieceType.ROOK));
                } else{
                    collection.add(new ChessMove(firstPos, upOne, null));
                }

                if(firstPos.getRow() == 2){
                    ChessPosition upTwo = offset(firstPos, 2, 0);
                    if(upTwo != null && board.getPiece(upTwo) == null){
                        collection.add(new ChessMove(firstPos, upTwo, null));
                    }
                }
            }
        }

        // white diagonals
        // diagonal left
        ChessPosition upLeft = offset(firstPos, 1, -1);
        if(upLeft != null){
            if (board.getPiece(upLeft) != null && board.getPiece(upLeft).getTeamColor() == ChessGame.TeamColor.BLACK){
                // promotions
                if(upLeft.getRow() == 8){
                    collection.add(new ChessMove(firstPos, upLeft, ChessPiece.PieceType.QUEEN));
                    collection.add(new ChessMove(firstPos, upLeft, ChessPiece.PieceType.KNIGHT));
                    collection.add(new ChessMove(firstPos, upLeft, ChessPiece.PieceType.BISHOP));
                    collection.add(new ChessMove(firstPos, upLeft, ChessPiece.PieceType.ROOK));
                }else {
                    collection.add(new ChessMove(firstPos, upLeft, null));
                }
            }
        }
        // diagonal right
        ChessPosition upRight = offset(firstPos, 1, 1);
        if(upRight != null){
            if(board.getPiece(upRight) != null && board.getPiece(upRight).getTeamColor() == ChessGame.TeamColor.BLACK){
                // promotions
                if(upRight.getRow() == 8){
                    collection.add(new ChessMove(firstPos, upRight, ChessPiece.PieceType.QUEEN));
                    collection.add(new ChessMove(firstPos, upRight, ChessPiece.PieceType.KNIGHT));
                    collection.add(new ChessMove(firstPos, upRight, ChessPiece.PieceType.BISHOP));
                    collection.add(new ChessMove(firstPos, upRight, ChessPiece.PieceType.ROOK));
                }else {
                    collection.add(new ChessMove(firstPos, upRight, null));
                }
            }
        }


        // black pawns
        if(piece.getTeamColor() == ChessGame.TeamColor.BLACK){
            ChessPosition downOne = offset(firstPos, -1, 0);
            if(downOne != null && board.getPiece(downOne) == null){

                // promotions
                if(downOne.getRow() == 1){
                    collection.add(new ChessMove(firstPos, downOne, ChessPiece.PieceType.QUEEN));
                    collection.add(new ChessMove(firstPos, downOne, ChessPiece.PieceType.KNIGHT));
                    collection.add(new ChessMove(firstPos, downOne, ChessPiece.PieceType.BISHOP));
                    collection.add(new ChessMove(firstPos, downOne, ChessPiece.PieceType.ROOK));
                } else{
                    collection.add(new ChessMove(firstPos, downOne, null));
                }


                if(firstPos.getRow() == 7){
                    ChessPosition downTwo = offset(firstPos, -2, 0);
                    if(downTwo != null && board.getPiece(downTwo) == null){
                        collection.add(new ChessMove(firstPos, downTwo, null));
                    }
                }
            }
        }

        // black diagonals
        // diagonal left
        ChessPosition downLeft = offset(firstPos, -1, -1);
        if(downLeft != null){
            if (board.getPiece(downLeft) != null && board.getPiece(downLeft).getTeamColor() == ChessGame.TeamColor.WHITE){
                // promotions
                if(downLeft.getRow() == 1){
                    collection.add(new ChessMove(firstPos, downLeft, ChessPiece.PieceType.QUEEN));
                    collection.add(new ChessMove(firstPos, downLeft, ChessPiece.PieceType.KNIGHT));
                    collection.add(new ChessMove(firstPos, downLeft, ChessPiece.PieceType.BISHOP));
                    collection.add(new ChessMove(firstPos, downLeft, ChessPiece.PieceType.ROOK));
                }else {
                    collection.add(new ChessMove(firstPos, downLeft, null));
                }
            }
        }

        // diagonal right
        ChessPosition downRight = offset(firstPos, -1, 1);
        if(downRight != null){
            if(board.getPiece(downRight) != null && board.getPiece(downRight).getTeamColor() == ChessGame.TeamColor.WHITE){
                // promotions
                if(downRight.getRow() == 1){
                    collection.add(new ChessMove(firstPos, downRight, ChessPiece.PieceType.QUEEN));
                    collection.add(new ChessMove(firstPos, downRight, ChessPiece.PieceType.KNIGHT));
                    collection.add(new ChessMove(firstPos, downRight, ChessPiece.PieceType.BISHOP));
                    collection.add(new ChessMove(firstPos, downRight, ChessPiece.PieceType.ROOK));
                }else {
                    collection.add(new ChessMove(firstPos, downRight, null));
                }
            }
        }



        return collection;
    }


    private static Collection<ChessMove> rookCalculator(ChessBoard board, ChessPosition firstPos, ChessPiece piece){
        Collection<ChessMove> collection = new ArrayList<>();

        // up
        for (int i = 1; i <= 8; i++){
            ChessPosition n = offset(firstPos, i, 0);
            if(n == null) break;

            if(board.getPiece(n) == null) collection.add(new ChessMove(firstPos, n, null));
            else{
                if(board.getPiece(n).getTeamColor() != piece.getTeamColor()) collection.add(new ChessMove(firstPos, n, null));
                break;
            }
        }

        // down
        for (int i = 1; i <= 8; i++){
            ChessPosition n = offset(firstPos, -i, 0);
            if(n == null) break;

            if(board.getPiece(n) == null) collection.add(new ChessMove(firstPos, n, null));
            else{
                if(board.getPiece(n).getTeamColor() != piece.getTeamColor()) collection.add(new ChessMove(firstPos, n, null));
                break;
            }
        }

        // right
        for (int i = 1; i <= 8; i++){
            ChessPosition n = offset(firstPos, 0, i);
            if(n == null) break;

            if(board.getPiece(n) == null) collection.add(new ChessMove(firstPos, n, null));
            else{
                if(board.getPiece(n).getTeamColor() != piece.getTeamColor()) collection.add(new ChessMove(firstPos, n, null));
                break;
            }
        }

        // left
        for (int i = 1; i <= 8; i++){
            ChessPosition n = offset(firstPos, 0, -i);
            if(n == null) break;

            if(board.getPiece(n) == null) collection.add(new ChessMove(firstPos, n, null));
            else{
                if(board.getPiece(n).getTeamColor() != piece.getTeamColor()) collection.add(new ChessMove(firstPos, n, null));
                break;
            }
        }

        return collection;

    }


    private static Collection<ChessMove> bishopCalculator(ChessBoard board, ChessPosition firstPos, ChessPiece piece){
        Collection<ChessMove> collection = new ArrayList<>();

        // up right
        for(int i = 1; i <= 8; i++){
            ChessPosition n = offset(firstPos, i, i);
            if(n == null) break;

            if(board.getPiece(n) == null) collection.add(new ChessMove(firstPos, n, null));
            else{
                if(board.getPiece(n).getTeamColor() != piece.getTeamColor()) collection.add(new ChessMove(firstPos, n, null));
                break;
            }
        }

        // up left
        for(int i = 1; i <= 8; i++){
            ChessPosition n = offset(firstPos, i, -i);
            if(n == null) break;

            if(board.getPiece(n) == null) collection.add(new ChessMove(firstPos, n, null));
            else{
                if(board.getPiece(n).getTeamColor() != piece.getTeamColor()) collection.add(new ChessMove(firstPos, n, null));
                break;
            }
        }

        // down righit
        for(int i = 1; i <= 8; i++){
            ChessPosition n = offset(firstPos, -i, i);
            if(n == null) break;

            if(board.getPiece(n) == null) collection.add(new ChessMove(firstPos, n, null));
            else{
                if(board.getPiece(n).getTeamColor() != piece.getTeamColor()) collection.add(new ChessMove(firstPos, n, null));
                break;
            }
        }

        // down left
        for(int i = 1; i <= 8; i++){
            ChessPosition n = offset(firstPos, -i, -i);
            if(n == null) break;

            if(board.getPiece(n) == null) collection.add(new ChessMove(firstPos, n, null));
            else{
                if(board.getPiece(n).getTeamColor() != piece.getTeamColor()) collection.add(new ChessMove(firstPos, n, null));
                break;
            }
        }

        return collection;

    }


    private static Collection<ChessMove> queenCalculator(ChessBoard board, ChessPosition firstPos, ChessPiece piece){
        Collection<ChessMove> collection = new ArrayList<>();

        collection.addAll(rookCalculator(board, firstPos, piece));
        collection.addAll(bishopCalculator(board, firstPos, piece));

        return collection;

    }


    private static Collection<ChessMove> kingCalculator(ChessBoard board, ChessPosition firstPos, ChessPiece piece) {
        Collection<ChessMove> collection = new ArrayList<>();

        ChessPosition n;

        // up
        n = offset(firstPos, 1, 0);
        if(n != null) {
            if (board.getPiece(n) == null) collection.add(new ChessMove(firstPos, n, null));
            else {
                if (board.getPiece(n).getTeamColor() != piece.getTeamColor()) collection.add(new ChessMove(firstPos, n, null));
            }
        }

        // down
        n = offset(firstPos, -1, 0);
        if(n != null) {
            if (board.getPiece(n) == null) collection.add(new ChessMove(firstPos, n, null));
            else {
                if (board.getPiece(n).getTeamColor() != piece.getTeamColor()) collection.add(new ChessMove(firstPos, n, null));
            }
        }

        // right
        n = offset(firstPos, 0, 1);
        if(n != null) {
            if (board.getPiece(n) == null) collection.add(new ChessMove(firstPos, n, null));
            else {
                if (board.getPiece(n).getTeamColor() != piece.getTeamColor()) collection.add(new ChessMove(firstPos, n, null));
            }
        }

        // left
        n = offset(firstPos, 0, -1);
        if(n != null) {
            if (board.getPiece(n) == null) collection.add(new ChessMove(firstPos, n, null));
            else {
                if (board.getPiece(n).getTeamColor() != piece.getTeamColor()) collection.add(new ChessMove(firstPos, n, null));
            }
        }

        // up right
        n = offset(firstPos, 1, 1);
        if(n != null) {
            if (board.getPiece(n) == null) collection.add(new ChessMove(firstPos, n, null));
            else {
                if (board.getPiece(n).getTeamColor() != piece.getTeamColor()) collection.add(new ChessMove(firstPos, n, null));
            }
        }

        // up left
        n = offset(firstPos, 1, -1);
        if(n != null) {
            if (board.getPiece(n) == null) collection.add(new ChessMove(firstPos, n, null));
            else {
                if (board.getPiece(n).getTeamColor() != piece.getTeamColor()) collection.add(new ChessMove(firstPos, n, null));
            }
        }

        // down right
        n = offset(firstPos, -1, 1);
        if(n != null) {
            if (board.getPiece(n) == null) collection.add(new ChessMove(firstPos, n, null));
            else {
                if (board.getPiece(n).getTeamColor() != piece.getTeamColor()) collection.add(new ChessMove(firstPos, n, null));
            }
        }

        // down left
        n = offset(firstPos, -1, -1);
        if(n != null) {
            if (board.getPiece(n) == null) collection.add(new ChessMove(firstPos, n, null));
            else {
                if (board.getPiece(n).getTeamColor() != piece.getTeamColor()) collection.add(new ChessMove(firstPos, n, null));
            }
        }

        return collection;
    }


    private static Collection<ChessMove> knightCalculator(ChessBoard board, ChessPosition firstPos, ChessPiece piece) {
        Collection<ChessMove> collection = new ArrayList<>();

        ChessPosition n;

        // position 1 (up 2 right 1)
        n = offset(firstPos, 2, 1);
        if (n != null) {
            if (board.getPiece(n) == null || board.getPiece(n).getTeamColor() != piece.getTeamColor()) {
                collection.add(new ChessMove(firstPos, n, null));
            }
        }

        // position 2 (up 1 right 2)
        n = offset(firstPos, 1, 2);
        if (n != null) {
            if (board.getPiece(n) == null || board.getPiece(n).getTeamColor() != piece.getTeamColor()) {
                collection.add(new ChessMove(firstPos, n, null));
            }
        }

        n = offset(firstPos, -1, 2);
        if (n != null) {
            if (board.getPiece(n) == null || board.getPiece(n).getTeamColor() != piece.getTeamColor()) {
                collection.add(new ChessMove(firstPos, n, null));
            }
        }

        n = offset(firstPos, -2, 1);
        if (n != null) {
            if (board.getPiece(n) == null || board.getPiece(n).getTeamColor() != piece.getTeamColor()) {
                collection.add(new ChessMove(firstPos, n, null));
            }
        }

        n = offset(firstPos, -2, -1);
        if (n != null) {
            if (board.getPiece(n) == null || board.getPiece(n).getTeamColor() != piece.getTeamColor()) {
                collection.add(new ChessMove(firstPos, n, null));
            }
        }

        n = offset(firstPos, -1, -2);
        if (n != null) {
            if (board.getPiece(n) == null || board.getPiece(n).getTeamColor() != piece.getTeamColor()) {
                collection.add(new ChessMove(firstPos, n, null));
            }
        }

        n = offset(firstPos, 1, -2);
        if (n != null) {
            if (board.getPiece(n) == null || board.getPiece(n).getTeamColor() != piece.getTeamColor()) {
                collection.add(new ChessMove(firstPos, n, null));
            }
        }

        // position 8
        n = offset(firstPos, 2, -1);
        if (n != null) {
            if (board.getPiece(n) == null || board.getPiece(n).getTeamColor() != piece.getTeamColor()) {
                collection.add(new ChessMove(firstPos, n, null));
            }
        }

        return collection;

    }





}
*/