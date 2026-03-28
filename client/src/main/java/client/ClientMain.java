package client;

import chess.*;
import ui.Client;

public class ClientMain {
    public static void main(String[] args) {
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Client: " + piece);

        int port = 8080;
        if(args.length > 0){
            try{
                port = Integer.parseInt(args[0]);
            }catch(Exception ex){
                System.out.println("Invalid port. Defaulting to 8080");
            }
        }
        System.out.println("Starting client on port " + port);

        Client client = new Client(port);
        client.run();
    }
}
