package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessBoard {

    private final ChessGame.TeamColor teamColor;
    private final chess.ChessBoard board;


    private static final String darkTileColor = SET_BG_COLOR_DARK_WOOD;
    private static final String lightTileColor = SET_BG_COLOR_LIGHT_WOOD;

    private static final String black = SET_TEXT_COLOR_BLACK;
    private static final String white = SET_TEXT_COLOR_WHITE;


    public ChessBoard(ChessGame.TeamColor teamColor, chess.ChessBoard board){
        this.teamColor = teamColor;
        this.board = board;
    }

    public static void main(String[] args){

        var board = new chess.ChessBoard();
        board.resetBoard();

        var whiteBoard = new ChessBoard(ChessGame.TeamColor.WHITE, board);
        var blackBoard = new ChessBoard(ChessGame.TeamColor.BLACK, board);

        System.out.println("White:\n");
        whiteBoard.draw();

        System.out.println("Black:\n");
        blackBoard.draw();

    }

    public void draw(){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        drawColumnNames();

        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        drawRows();

        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        drawColumnNames();

    }


    private void drawColumnNames(){

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("   ");
        char[] letters;


        if(teamColor == ChessGame.TeamColor.WHITE){
            letters = new char[] {'A','B','C','D','E','F','G','H'};
        }else{
            letters = new char[] {'H','G','F','E','D','C','B','A'};
        }

        for(char letter : letters){
            stringBuilder.append(" ").append(letter).append(" ");
        }

        System.out.print(stringBuilder);
        System.out.print("\n");
    }


    private void drawRows(){

        for(int rowNum = 0; rowNum < 8; rowNum++){

            int row;
            if(teamColor == ChessGame.TeamColor.WHITE){
                row = 8 - rowNum;
            } else{
                row = 1 + rowNum;
            }

            System.out.print("\n");
            System.out.print(SET_BG_COLOR_BLACK);
            System.out.print(SET_TEXT_COLOR_WHITE);
            System.out.print(" " + row + " ");

            for(int colNum = 0; colNum < 8; colNum++){

                int col;
                if(teamColor == ChessGame.TeamColor.WHITE){
                    col = 1 + colNum;
                }else{
                    col = 8 - colNum;
                }

                if((rowNum + colNum) % 2 == 0){
                    System.out.print(lightTileColor);
                }else{
                    System.out.print(darkTileColor);
                }

                ChessPiece piece = board.getPiece(new ChessPosition(row, col));


                // draw the tile
                if(piece == null){
                    System.out.print(EMPTY);
                }else{
                    if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                        System.out.print(white);
                    }else{
                        System.out.print(black);
                    }

                    // pieces are easier to differentiate when not filled in. Always using WHITE_ versions of pieces.
                    ChessPiece.PieceType pieceType = piece.getPieceType();
                    switch(pieceType){
                        case KING:
                            System.out.print(WHITE_KING);
                            break;
                        case QUEEN:
                            System.out.print(WHITE_QUEEN);
                            break;
                        case BISHOP:
                            System.out.print(WHITE_BISHOP);
                            break;
                        case KNIGHT:
                            System.out.print(WHITE_KNIGHT);
                            break;
                        case ROOK:
                            System.out.print(WHITE_ROOK);
                            break;
                        case PAWN:
                            System.out.print(WHITE_PAWN);
                            break;
                        default:
                            throw new Error("Wrong piece type. can't print");
                    }

                }

            }


        }
    }






}


// var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);