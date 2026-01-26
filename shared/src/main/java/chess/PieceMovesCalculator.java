package chess;

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
