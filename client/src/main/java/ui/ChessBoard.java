package ui;

import chess.ChessGame;

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

        System.out.println("Black\n");
        blackBoard.draw();

    }

    public void draw(){
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
        System.out.println("\n");
    }






}


// var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);