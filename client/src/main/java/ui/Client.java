package ui;

import chess.ChessBoard;
import chess.ChessGame;
import records.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {

    private final Scanner scanner = new Scanner(System.in);
    private final ServerFacade server;
    private List<GameReturn> games = new ArrayList<>(); // mapping

    public Client(int port){
        server = new ServerFacade(port);
    }

    public static void main(String[] args){
        Client client = new Client(8080);
        client.run();
    }

    private String authToken = null;

    public void run(){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.println("loop started");



        while (true){
            if(authToken == null){
                if(preLogin()){
                    break;
                }
            }else{
                if(postLogin()){
                    break;
                }
            }
        }

    }


    private Boolean preLogin() {
        System.out.println("Hello. Pre-login:");

        String message =
                "1 - Login\n" +
                "2 - Register\n" +
                "3 - Help\n" +
                "4 - quit\n";
        System.out.print(message);

        String input = scanner.nextLine();

        switch(input){
            case "1":
                login();
                break;
            case "2":
                register();
                break;
            case "3":
                preLoginHelp();
                break;
            case "4":
                return true;
            default:
                System.out.println("Invalid command. Enter 1, 2, 3, or 4.");
        }
        return false;
    }

    private void preLoginHelp() {
        System.out.println("helping");
    }

    private void login() {
        System.out.println("logging in");
        try{
            System.out.print("Username: ");
            String username = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            LoginRequest req = new LoginRequest(username, password);

            var res = server.login(req);

            authToken = res.authToken();

            System.out.println("Logged in");
        } catch(Exception ex){
            System.out.println(ex.getMessage()); // debugging
        }
    }

    private void register() {
        System.out.println("registering");
        try{
            System.out.print("Username: ");
            String username = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            RegisterRequest req = new RegisterRequest(username, password, email);

            var res = server.register(req);

            authToken = res.authToken();

            System.out.println("registered");
        }catch(Exception ex){
            System.out.println(ex.getMessage()); // change this later.
        }
    }

    private boolean postLogin() {

        System.out.println("Hello. Post-login:");

        String message =
                "1 - logout\n" +
                "2 - list games\n" +
                "3 - create a game\n" +
                "4 - join a game\n" +
                "5 - observe a game\n" +
                "6 - help\n";

        System.out.print(message);

        String input = scanner.nextLine();

        switch(input){
            case "1":
                logout();
                break;
            case "2":
                list();
                break;
            case "3":
                create();
                break;
            case "4":
                join();
                break;
            case "5":
                observe();
                break;
            case "6":
                postLoginHelp();
                break;
            default:
                System.out.println("Invalid command. Enter 1, 2, 3, 4, 5, or 6.");
        }

        return false;
    }

    private void postLoginHelp() {

    }

    private void observe() {

    }

    private void logout() {
        System.out.println("logging out");

        try{
            LogoutRequest req = new LogoutRequest(authToken);
            authToken = null;

            var res = server.logout(req);

            System.out.println("logged out");
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    private void list() {
        System.out.println("listing games");

        try{
            var res = server.list(new ListRequest(authToken));

            games = res.games();

            if(games.isEmpty()){
                System.out.println("no games :(");
                return;
            }

            for(int i = 0; i < games.size(); i++){
                GameReturn game = games.get(i);
                System.out.println((i + 1) + " - " + game.gameName() +
                        ": White player: " + game.whiteUsername() +
                        " Black player: " + game.blackUsername());
            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    private void create() {
        System.out.println("creating game");

        try{
            System.out.print("Enter game name: ");
            String gameName = scanner.nextLine();

            if(gameName.isBlank()){
                System.out.println("Invalid game name");
                return;
            }


            CreateRequest req = new CreateRequest(authToken, gameName);

            var res = server.create(req);

            System.out.println("Game " + gameName + " created with ID " + res.gameID() + ".");

        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    private void join() {
        System.out.println("joinging a game");

        try{
            if(games == null || games.isEmpty()){
                System.out.println("List games first. If no games available, create one.");
                return;
            }

            System.out.print("Enter game id: ");
            int gameId;
            try{
                // gameId = (int) scanner.nextLine();
                gameId = Integer.parseInt(scanner.nextLine());

                if(gameId < 1 || gameId > games.size()){
                    System.out.println("Invalid game id.");
                    return;
                }
            }catch(Exception ex){
                System.out.println("Invalid game id");
                return;
            }


            int gameIdReal = games.get(gameId - 1).gameID();

            System.out.print("Enter color [white/black] or [w/b]: ");
            String color = scanner.nextLine().toLowerCase();

            if(color.equals("w")){
                color = "white";
            }else if(color.equals("b")){
                color = "black";
            }

            if(!color.equals("white") && !color.equals("black")){
                System.out.println("Invalid color");
                return;
            }


            JoinRequest req = new JoinRequest(authToken, gameIdReal, color.toUpperCase());

            var res = server.join(req);

            System.out.println("Joined game " + games.get(gameId - 1).gameName() + " as " + color + ".");


            if(color.equals("white")){
                drawBoard(ChessGame.TeamColor.WHITE);
            }else{
                drawBoard(ChessGame.TeamColor.BLACK);
            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }


    // change in phase 6
    private void drawBoard(ChessGame.TeamColor teamColor){
        chess.ChessBoard board = new ChessBoard();
        board.resetBoard();

        ui.ChessBoard printBoard = new ui.ChessBoard(teamColor, board);
        printBoard.draw();
    }
}


