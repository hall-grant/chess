package chess;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;

public class PieceMovesCalculator {
    /*
    public PieceMovesCalculator(ChessBoard board, ChessPosition position){

    }*/

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
            System.out.println("Something has gone horribly, horribly wrong.\nInvalid pieceType.");
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
                collection.add(new ChessMove(firstPos, upOne, null));
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
                collection.add(new ChessMove(firstPos, upLeft, null));
            }
        }
        // diagonal right
        ChessPosition upRight = offset(firstPos, 1, 1);
        if(upRight != null){
            if(board.getPiece(upRight) != null && board.getPiece(upRight).getTeamColor() == ChessGame.TeamColor.BLACK){
                collection.add(new ChessMove(firstPos, upRight, null));
            }
        }


        // black pawns
        if(piece.getTeamColor() == ChessGame.TeamColor.BLACK){
            ChessPosition downOne = offset(firstPos, -1, 0);
            if(downOne != null && board.getPiece(downOne) == null){
                collection.add(new ChessMove(firstPos, downOne, null));
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
                collection.add(new ChessMove(firstPos, downLeft, null));
            }
        }

        // diagonal right
        ChessPosition downRight = offset(firstPos, -1, 1);
        if(downRight != null){
            if(board.getPiece(downRight) != null && board.getPiece(downRight).getTeamColor() == ChessGame.TeamColor.WHITE){
                collection.add(new ChessMove(firstPos, downRight, null));
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


/*
    private static Collection<ChessMove> pawnCalculator(ChessBoard board, ChessPosition firstPos, ChessPiece piece){
        Collection<ChessMove> collection = new ArrayList<>();
        ChessPosition checkedPos;

        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            // moving forward 2 spaces on its first move (white)
                // basically, if the piece hasn't moved yet and there isn't another piece in the way, you can move 2.
            if(firstPos.getRow() == 1 && board.getPiece(new ChessPosition(firstPos.getRow() + 1, firstPos.getColumn())) == null){
                checkedPos = checkUpDown(board, firstPos, piece, 2);
                if(checkedPos != firstPos) collection.add(new ChessMove(firstPos, checkedPos, piece.getPieceType()));
            }
            // moving forward 1 space (white)
            checkedPos = checkUpDown(board, firstPos, piece, 1);
            if(checkedPos != firstPos) collection.add(new ChessMove(firstPos, checkedPos, piece.getPieceType()));

            // capturing as white
            if(firstPos.getRow() <= 6){
                // capturing upper left piece
                if(firstPos.getColumn() >= 1){
                    ChessPiece killedPiece = board.getPiece(new ChessPosition(firstPos.getRow() + 1, firstPos.getColumn() - 1));
                    if(killedPiece != null && killedPiece.getTeamColor() != ChessGame.TeamColor.WHITE){
                        // collection.add(new ChessPosition(firstPos.getRow() + 1, firstPos.getColumn() - 1));
                        collection.add(new ChessMove(firstPos, new ChessPosition(firstPos.getRow() + 1, firstPos.getColumn() - 1), piece.getPieceType()));
                    }
                }

                // capturing upper right piece
                if(firstPos.getColumn() <= 6){
                    ChessPiece killedPiece = board.getPiece(new ChessPosition(firstPos.getRow() + 1, firstPos.getColumn() + 1));
                    if(killedPiece != null && killedPiece.getTeamColor() != ChessGame.TeamColor.WHITE){
                        // collection.add(new ChessPosition(firstPos.getRow() + 1, firstPos.getColumn() + 1));
                        collection.add(new ChessMove(firstPos, new ChessPosition(firstPos.getRow() + 1, firstPos.getColumn() + 1), piece.getPieceType()));
                    }
                }

                // En passant (but not right now)

            }

        }

        else if(piece.getTeamColor() == ChessGame.TeamColor.BLACK){
            // moving forward 2 spaces on its first move (black)
                // basically, if the piece hasn't moved yet and there isn't another piece in the way, you can move 2.
            if(firstPos.getRow() == 6 && board.getPiece(new ChessPosition(firstPos.getRow() + 1, firstPos.getColumn())) == null){
                checkedPos = checkUpDown(board, firstPos, piece, -2);
                if(checkedPos != firstPos) collection.add(new ChessMove(firstPos, checkedPos, piece.getPieceType()));
            }
            // moving forward 1 space (black)
            checkedPos = checkUpDown(board, firstPos, piece, -1);
            if(checkedPos != firstPos) collection.add(new ChessMove(firstPos, checkedPos, piece.getPieceType()));

            // capturing as black
            if(firstPos.getRow() >= 1){
                // capturing lower left piece
                if(firstPos.getColumn() >= 1){
                    ChessPiece killedPiece = board.getPiece(new ChessPosition(firstPos.getRow() - 1, firstPos.getColumn() - 1));
                    if(killedPiece != null && killedPiece.getTeamColor() != ChessGame.TeamColor.BLACK){
                        // collection.add(new ChessPosition(firstPos.getRow() - 1, firstPos.getColumn() - 1));
                        collection.add(new ChessMove(firstPos, new ChessPosition(firstPos.getRow() - 1, firstPos.getColumn() - 1), piece.getPieceType()));
                    }
                }

                // capturing lower right piece
                if(firstPos.getColumn() <= 6){
                    ChessPiece killedPiece = board.getPiece(new ChessPosition(firstPos.getRow() - 1, firstPos.getColumn() + 1));
                    if(killedPiece != null && killedPiece.getTeamColor() != ChessGame.TeamColor.BLACK){
                        // collection.add(new ChessPosition(firstPos.getRow() - 1, firstPos.getColumn() + 1));
                        collection.add(new ChessMove(firstPos, new ChessPosition(firstPos.getRow() - 1, firstPos.getColumn() + 1), piece.getPieceType()));
                    }
                }

                // En passant (but not right now)

            }
        }

        return collection;
    }


    private static Collection<ChessMove> rookCalculator(ChessBoard board, ChessPosition firstPos, ChessPiece piece){
        Collection<ChessMove> collection = new ArrayList<>();

        // check up
        for(int i = 0; i <= 7 - firstPos.getRow(); i++){
            ChessPosition checkedPos = new ChessPosition(firstPos.getRow() + i + 1, firstPos.getColumn());
            // if no piece is in the way
            if(board.getPiece(checkedPos) != null){
                // if piece is enemy
                if(board.getPiece(checkedPos).getTeamColor() != piece.getTeamColor()){
                    collection.add(new ChessMove(firstPos, checkedPos, piece.getPieceType()));
                }
                break;
            }
            collection.add(new ChessMove(firstPos, checkedPos, piece.getPieceType()));
        }

        // check down
        for(int i = 0; i <= firstPos.getRow(); i++){
            ChessPosition checkedPos = new ChessPosition(firstPos.getRow() - (i + 1), firstPos.getColumn());
            // if no piece is in the way
            if(board.getPiece(checkedPos) != null){
                // if piece is enemy
                if(board.getPiece(checkedPos).getTeamColor() != piece.getTeamColor()){
                    collection.add(new ChessMove(firstPos, checkedPos, piece.getPieceType()));
                }
                break;
            }
            collection.add(new ChessMove(firstPos, checkedPos, piece.getPieceType()));
        }

        // check left
        for(int i = 0; i <= firstPos.getColumn(); i++){
            ChessPosition checkedPos = new ChessPosition(firstPos.getRow(), firstPos.getColumn() - (i + 1));
            // if no piece is in the way
            if(board.getPiece(checkedPos) != null){
                // if piece is enemy
                if(board.getPiece(checkedPos).getTeamColor() != piece.getTeamColor()) {
                    collection.add(new ChessMove(firstPos, checkedPos, piece.getPieceType()));
                }
                break;
            }
            collection.add(new ChessMove(firstPos, checkedPos, piece.getPieceType()));
        }

        // check right
        for(int i = 0; i <= 7 - firstPos.getColumn(); i++){
            ChessPosition checkedPos = new ChessPosition(firstPos.getRow(), firstPos.getColumn() + i + 1);
            // if no piece is in the way
            if(board.getPiece(checkedPos) != null){
                // if piece is enemy
                if(board.getPiece(checkedPos).getTeamColor() != piece.getTeamColor()){
                    collection.add(new ChessMove(firstPos, checkedPos, piece.getPieceType()));
                }
                break;
            }
            collection.add(new ChessMove(firstPos, checkedPos, piece.getPieceType()));
        }


        return collection;
    }


    private static Collection<ChessMove> bishopCalculator(ChessBoard board, ChessPosition firstPos, ChessPiece piece){
        Collection<ChessMove> collection = new ArrayList<>();

        // check up-right
        for(int i = 1; i < 8; i++){
            ChessPosition checkedPos = checkDiagUpRightDownLeft(board, firstPos, piece, i);
            if(checkedPos == firstPos){
                break;
            }
            collection.add(new ChessMove(firstPos, checkedPos, piece.getPieceType()));
        }

        // check down-left
        for(int i = 1; i < 8; i++){
            ChessPosition checkedPos = checkDiagUpRightDownLeft(board, firstPos, piece, -i);
            if(checkedPos == firstPos){
                break;
            }
            collection.add(new ChessMove(firstPos, checkedPos, piece.getPieceType()));
        }

        // check up-left
        for(int i = 1; i < 8; i++){
            ChessPosition checkedPos = checkDiagUpLeftDownRight(board, firstPos, piece, i);
            if(checkedPos == firstPos){
                break;
            }
            collection.add(new ChessMove(firstPos, checkedPos, piece.getPieceType()));
        }

        // check down-right
        for(int i = 1; i < 8; i++){
            ChessPosition checkedPos = checkDiagUpLeftDownRight(board, firstPos, piece, -i);
            if(checkedPos == firstPos){
                break;
            }
            collection.add(new ChessMove(firstPos, checkedPos, piece.getPieceType()));
        }

        return collection;

    }


    private static Collection<ChessMove> queenCalculator(ChessBoard board, ChessPosition firstPos, ChessPiece piece){
        Collection<ChessMove> collection = new ArrayList<>();

        collection.addAll(rookCalculator(board, firstPos, piece));
        collection.addAll(bishopCalculator(board, firstPos, piece));

        return collection;
    }


    private static Collection<ChessMove> kingCalculator(ChessBoard board, ChessPosition firstPos, ChessPiece piece){
        Collection<ChessMove> collection = new ArrayList<>();

        ChessPosition checkedPos;

        checkedPos = checkUpDown(board, firstPos, piece, 1);
        if(checkedPos != firstPos) collection.add(new ChessMove(firstPos, checkedPos, piece.getPieceType()));
        checkedPos = checkUpDown(board, firstPos, piece, -1);
        if(checkedPos != firstPos) collection.add(new ChessMove(firstPos, checkedPos, piece.getPieceType()));
        checkedPos = checkLeftRight(board, firstPos, piece, 1);
        if(checkedPos != firstPos) collection.add(new ChessMove(firstPos, checkedPos, piece.getPieceType()));
        checkedPos = checkLeftRight(board, firstPos, piece, -1);
        if(checkedPos != firstPos) collection.add(new ChessMove(firstPos, checkedPos, piece.getPieceType()));
        checkedPos = checkDiagUpLeftDownRight(board, firstPos, piece, 1);
        if(checkedPos != firstPos) collection.add(new ChessMove(firstPos, checkedPos, piece.getPieceType()));
        checkedPos = checkDiagUpLeftDownRight(board, firstPos, piece, -1);
        if(checkedPos != firstPos) collection.add(new ChessMove(firstPos, checkedPos, piece.getPieceType()));
        checkedPos = checkDiagUpRightDownLeft(board, firstPos, piece, 1);
        if(checkedPos != firstPos) collection.add(new ChessMove(firstPos, checkedPos, piece.getPieceType()));
        checkedPos = checkDiagUpRightDownLeft(board, firstPos, piece, -1);
        if(checkedPos != firstPos) collection.add(new ChessMove(firstPos, checkedPos, piece.getPieceType()));

        return collection;

    }


    private static Collection<ChessMove> knightCalculator(ChessBoard board, ChessPosition firstPos, ChessPiece piece){
        Collection<ChessMove> collection = new ArrayList<>();

        for(int i = 1; i <= 8; i++){
            ChessPosition checkedPos = checkKnightDirections(board, firstPos, piece, i);
            if(checkedPos != firstPos) collection.add(new ChessMove(firstPos, checkedPos, piece.getPieceType()));
        }

        return collection;
    }




    private static ChessPosition checkUpDown(ChessBoard board, ChessPosition firstPos, ChessPiece piece, int count){
        // check bounds
        if(count > 0 && (firstPos.getColumn() + count) >= 8){
            return firstPos;
        } else if (count < 0 && (firstPos.getColumn() + count) <= 0){
            return firstPos;
        } else if (count == 0){
            return firstPos;
        }

        ChessPosition finalPos = new ChessPosition(firstPos.getRow(), firstPos.getColumn() + count);

        if(board.getPiece(finalPos) == null){
            return finalPos;
        }

        ChessPiece killPiece = board.getPiece(finalPos); // piece isn't actually killed. I just couldn't come up with a better name.
        if(killPiece.getTeamColor() == piece.getTeamColor()){
            return firstPos;
        } else {
            return finalPos;
        }
    }

    private static ChessPosition checkLeftRight(ChessBoard board, ChessPosition firstPos, ChessPiece piece, int count){
        // check bounds
        if(count > 0 && (firstPos.getRow() + count) >= 8){
            return firstPos;
        } else if (count < 0 && (firstPos.getRow() + count) <= 8){
            return firstPos;
        } else if (count == 0){
            return firstPos;
        }

        ChessPosition finalPos = new ChessPosition(firstPos.getRow() + count, firstPos.getColumn());

        if(board.getPiece(finalPos) == null){
            return finalPos;
        }

        ChessPiece killPiece = board.getPiece(finalPos); // piece isn't actually killed. I just couldn't come up with a better name.
        if(killPiece.getTeamColor() == piece.getTeamColor()){
            return firstPos;
        } else{
            return finalPos;
        }
    }

    private static ChessPosition checkDiagUpRightDownLeft(ChessBoard board, ChessPosition firstPos, ChessPiece piece, int count){
        // check bounds
        if(count == 0){
            return firstPos;
        }

        ChessPosition finalPos = new ChessPosition(firstPos.getRow() + count,firstPos.getColumn() + count);
        if(finalPos.getRow() >= 8) {return firstPos;}
        else if (finalPos.getRow() < 0) {return firstPos;}
        else if (finalPos.getColumn() >= 8) {return firstPos;}
        else if (finalPos.getColumn() < 0) {return firstPos;}

        if(board.getPiece(finalPos) == null){
            return finalPos;
        }

        ChessPiece killPiece = board.getPiece(finalPos); // piece isn't actually killed. I just couldn't come up with a better name.
        if(killPiece.getTeamColor() == piece.getTeamColor()){
            return firstPos;
        } else{
            return finalPos;
        }
    }

    private static ChessPosition checkDiagUpLeftDownRight(ChessBoard board, ChessPosition firstPos, ChessPiece piece, int count){
        // check bounds
        if(count == 0){
            return firstPos;
        }

        ChessPosition finalPos = new ChessPosition(firstPos.getRow() - count,firstPos.getColumn() + count);
        if(finalPos.getRow() >= 8) {return firstPos;}
        else if (finalPos.getRow() < 0) {return firstPos;}
        else if (finalPos.getColumn() >= 8) {return firstPos;}
        else if (finalPos.getColumn() < 0) {return firstPos;}

        if(board.getPiece(finalPos) == null){
            return finalPos;
        }

        ChessPiece killPiece = board.getPiece(finalPos); // piece isn't actually killed. I just couldn't come up with a better name.
        if(killPiece.getTeamColor() == piece.getTeamColor()){
            return firstPos;
        } else{
            return finalPos;
        }
    }


     */

/*
 * Checks possible moves for Knight pieces. Count can be 1-8, beginning with top right and going clockwise to top left.
 * @param board a chessBoard object with the piece on it
 * @param firstPos starting position of the piece
 * @param piece the piece to move
 * @param count which of the valid 8 spaces to move (1 for top-right, clockwise until 8 for top-left)
 * @return a valid position or the initial position if invalid.
 */
    /*
    private static ChessPosition checkKnightDirections(ChessBoard board, ChessPosition firstPos, ChessPiece piece, int count){
        // check bounds
        if(count == 0){
            return firstPos;
        } else if(count < 0 || count > 8){
            return firstPos;
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
                return firstPos;
        }

        if(finalPos.getRow() >= 8) {return firstPos;}
        else if (finalPos.getRow() < 0) {return firstPos;}
        else if (finalPos.getColumn() >= 8) {return firstPos;}
        else if (finalPos.getColumn() < 0) {return firstPos;}

        if(board.getPiece(finalPos) == null){
            return finalPos;
        }

        ChessPiece killPiece = board.getPiece(finalPos); // piece isn't actually killed. I just couldn't come up with a better name.
        if(killPiece.getTeamColor() == piece.getTeamColor()){
            return firstPos;
        } else{
            return finalPos;
        }

    }


     */
