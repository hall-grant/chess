package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor turn;
    private ChessBoard board;

    public ChessGame() {
        this.turn = TeamColor.WHITE;
        this.board = new ChessBoard();
        this.board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Sets which team's turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        ChessPiece checkPiece = board.getPiece(startPosition);

        if (checkPiece == null) {
            return validMoves;
        }

        for (ChessMove move : checkPiece.pieceMoves(board, startPosition)) {
            ChessGame cloneGame = new ChessGame();
            cloneGame.setBoard(board.clone());
            cloneGame.setTeamTurn(turn);

            try {
                movePiece(cloneGame.getBoard(), move);
            } catch (InvalidMoveException ex) {
                continue;
            }

            if (!cloneGame.isInCheck(checkPiece.getTeamColor())) {
                validMoves.add(move);
            }
        }

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(isInCheckmate(turn)) throw new InvalidMoveException("Checkmate");
        if(isInStalemate(turn)) throw new InvalidMoveException("Stalemate");


        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());

        // no valid moves for piece
        if(validMoves.isEmpty()) throw new InvalidMoveException("Invalid move: no valid moves for target piece.");

        // check if valid move
        for(ChessMove checkMove : validMoves){
            if(checkMove.equals(move)){
                ChessPiece initialPiece = board.getPiece(move.getStartPosition());
                board.addPiece(move.getStartPosition(), null);

                if(move.getPromotionPiece() == null) board.addPiece(move.getEndPosition(), new ChessPiece(turn, initialPiece.getPieceType()));
                else board.addPiece(move.getEndPosition(), new ChessPiece(turn, move.getPromotionPiece()));
            }
        }

        if(turn == TeamColor.WHITE) turn = TeamColor.BLACK;
        else if (turn == TeamColor.BLACK) turn = TeamColor.WHITE;


    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // get position of king
        ChessPosition kingPos = null;
        for(int i = 1; i <= 8; i ++){
            if (kingPos != null) break;
            for(int j = 1; j <= 8; j++){
                ChessPiece potentialKing = board.getPiece(new ChessPosition(i,j));
                if(potentialKing != null && potentialKing.getTeamColor() == teamColor && potentialKing.getPieceType() == ChessPiece.PieceType.KING){
                    kingPos = new ChessPosition(i, j);
                    break;
                }
            }
        }

        // king not found
        if(kingPos == null) /*throw new RuntimeException("No king found on board.");*/ return false;

        // get positions of all opposing pieces
        Collection<ChessPosition> enemyPositions = new ArrayList<>();
        for(int i = 1; i <= 8; i ++){
            for(int j = 1; j <= 8; j++){
                ChessPiece potentialPiece = board.getPiece(new ChessPosition(i,j));
                if(potentialPiece != null && potentialPiece.getTeamColor() != teamColor){
                    enemyPositions.add(new ChessPosition(i,j));
                }
            }
        }

        // check whether any piece has king position in valid moves
        for (ChessPosition ep : enemyPositions){
            ChessPiece enemyPiece = board.getPiece(ep);
            Collection<ChessPosition> enemyMoves = new ArrayList<>();

            for(ChessMove move : enemyPiece.pieceMoves(board, ep)){
                enemyMoves.add(move.getEndPosition());
            }

            for(ChessPosition enemyMovePos : enemyMoves){
                if(enemyMovePos.equals(kingPos)){
                    return true;
                }
            }

        }
        return false;

    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor)) return false;

        // clone board, check if any moves get out of check
        // this seems extremely inefficient, but whatever

        // get positions of all pieces on teamColor
        // also get king's position.
        ChessPosition kingPos = null;
        Collection<ChessPosition> teamPositions = new ArrayList<>(); // also contains king
        for (int i = 1; i <= 8; i ++){
            for (int j = 1; j <= 8; j++){
                ChessPiece potentialPiece = board.getPiece(new ChessPosition(i,j));
                if(potentialPiece != null && potentialPiece.getTeamColor() == teamColor){
                    // get king position, if applicable
                    if(kingPos == null && potentialPiece.getPieceType() == ChessPiece.PieceType.KING){
                        kingPos = new ChessPosition(i,j);
                    }
                    teamPositions.add(new ChessPosition(i,j));
                }
            }
        }

        // move through each valid move to check whether anything gets king out of check.
        for(ChessPosition potentialPos : teamPositions){
            Collection<ChessMove> potentialCol = validMoves(potentialPos);
            if(potentialCol == null || !potentialCol.isEmpty()) return false;
        }

        return true;

        // ChessBoard checkBoard = board.clone();

    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        // for this assigment, only checks whether the king is not in checkmate but team at play has no moves

        if(isInCheckmate(teamColor)) return false;
        if(isInCheck(teamColor)) return false;

        // get positions of all pieces on teamColor
        // also get king's position.
        ChessPosition kingPos = null;
        Collection<ChessPosition> teamPositions = new ArrayList<>(); // also contains king
        for (int i = 1; i <= 8; i ++){
            for (int j = 1; j <= 8; j++){
                ChessPiece potentialPiece = board.getPiece(new ChessPosition(i,j));
                if(potentialPiece != null && potentialPiece.getTeamColor() == teamColor){
                    // get king position, if applicable
                    if(kingPos == null && potentialPiece.getPieceType() == ChessPiece.PieceType.KING){
                        kingPos = new ChessPosition(i,j);
                    }
                    teamPositions.add(new ChessPosition(i,j));
                }
            }
        }


        for(ChessPosition checkPos : teamPositions){
            Collection<ChessMove> potentialMoves = validMoves(checkPos);
            if(potentialMoves == null || !potentialMoves.isEmpty()) return false;
        }

        return true;

    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return turn == chessGame.turn && Objects.equals(getBoard(), chessGame.getBoard());
    }

    @Override
    public int hashCode() {
        return Objects.hash(turn, getBoard());
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "turn=" + turn +
                ", board=" + board +
                '}';
    }

    private void movePiece(ChessBoard board, ChessMove move) throws InvalidMoveException{
        ChessPosition startPos = move.getStartPosition();
        ChessPosition endPos = move.getEndPosition();
        ChessPiece piece = board.getPiece(startPos);

        if(piece == null) throw new InvalidMoveException("Given position has no piece");

        // remove piece from start
        board.addPiece(startPos, null);

        // add piece to end (includes promotion)
        if(move.getPromotionPiece() != null){
            board.addPiece(endPos, new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
        } else{
            board.addPiece(endPos, new ChessPiece(piece.getTeamColor(), piece.getPieceType()));
        }

    }


}

/*
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        ChessPiece checkPiece = board.getPiece(startPosition);

        // case where startPosition has the incorrect color of piece
        // if(checkPiece.getTeamColor() != turn) return validMoves;

        // case where startPosition doesn't have a piece
        if(checkPiece == null){
            return null;
        }

        for(ChessMove move : checkPiece.pieceMoves(board, startPosition)){
            ChessGame cloneGame = new ChessGame();
            ChessBoard clone = board.clone();
            cloneGame.setBoard(clone);
            cloneGame.setTeamTurn(turn);

            try{
                movePiece(cloneGame.getBoard(), move);
            } catch (InvalidMoveException ex){
                throw new RuntimeException(ex);
            }

            if(!cloneGame.isInCheck(turn)) validMoves.add(move);

        }

        return validMoves;

    }
 */