package chess;

import java.util.ArrayList;
import java.util.Collection;

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
        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
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
        if(kingPos == null) throw new RuntimeException("No king found on board.");

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
        Collection<ChessPosition> teamPositions = new ArrayList<>();
        for (int i = 1; i <= 8; i ++){
            for (int j = 1; j <= 8; j++){
                ChessPiece potentialPiece = board.getPiece(new ChessPosition(i,j));
                if(potentialPiece != null && potentialPiece.getTeamColor() == teamColor){
                    // get king position, if applicable
                    if(kingPos == null && potentialPiece.getPieceType() == ChessPiece.PieceType.KING){
                        kingPos = new ChessPosition(i,j);
                        continue;
                    }
                    teamPositions.add(new ChessPosition(i,j));
                }
            }
        }

        for(ChessPosition pos : teamPositions){
            
        }

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
        throw new RuntimeException("Not implemented");
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
}
