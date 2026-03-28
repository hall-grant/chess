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

//    public static void main(String[] args){
//        Client client = new Client(8080);
//        client.run();
//    }

    private String authToken = null;

    public void run(){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        // out.println("loop started");



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
        System.out.println("Welcome. Enter a command: ");

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
        System.out.println("\nType a command [1, 2, 3, or 4]:\n" +
                "1 - Register: \n  Create a new account with provided username, password, and email.\n\n" +
                "2 - Login: \n  Login into existing account with username and password.\n\n" +
                "3 - Help: \n  Displays this message.\n\n" +
                "4 - Quit: \n  Exit this program.\n\n\n"
        );
    }

    private void login() {
        // System.out.println("logging in");
        try{
            System.out.print("Username: ");
            String username = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            LoginRequest req = new LoginRequest(username, password);

            var res = server.login(req);

            authToken = res.authToken();

            System.out.println("Logged in successfully.");
        } catch(Exception ex){
            System.out.println("Error: Something went wrong"); // debugging
        }
    }

    private void register() {
        // System.out.println("registering");
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

            System.out.println("Registered successfully.");
        }catch(Exception ex){
            System.out.println("Error: Something went wrong"); // change this later.
        }
    }

    private boolean postLogin() {

        // System.out.println("Hello. Post-login:");

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
        System.out.println("\nType a command [1, 2, 3, or 4]:\n\n" +
                "1 - Logout: \n  Logs you out and returns to login message.\n\n" +
                "2 - List games: \n  List all active games with an associated ID.\n\n" +
                "3 - Create a game: \n  Create a new chess game with the name provided.\n\n" +
                "4 - Join a game: \n  Join an existing game. Join game as white or black.\n\n" +
                "5 - Observe a game: \n  Spectate an existing game.\n\n" +
                "6 - Help: \n  Display this help screen.\n\n\n"
        );
    }

    // according to spec, this is technically all I need. Come back in phase6
    private void observe() {
        // System.out.println("observing");


        if(games == null || games.isEmpty()){
            System.out.println("Please list games.");
            return;
        }

        System.out.print("Enter game ID: ");
        int gameId;

        try{
            gameId = Integer.parseInt(scanner.nextLine());

            if(gameId < 1 || gameId > games.size()){
                System.out.println("Invalid game ID.");
                return;
            }
        }catch(Exception ex){
            System.out.println("Invalid game ID.");
            return;
        }

        System.out.println("Observing game " + games.get(gameId - 1).gameName() + " from white's perspective");

        drawBoard(ChessGame.TeamColor.WHITE);


    }

    private void logout() {
        // System.out.println("logging out");

        try{
            LogoutRequest req = new LogoutRequest(authToken);
            authToken = null;

            var res = server.logout(req);

            System.out.println("Logged out successfully");
        }catch(Exception ex){
            System.out.println("Error: Something went wrong");
        }
    }

    private void list() {
        // System.out.println("listing games");

        try{
            var res = server.list(new ListRequest(authToken));

            games = res.games();

            if(games.isEmpty()){
                System.out.println("No games available.");
                return;
            }

            for(int i = 0; i < games.size(); i++){
                GameReturn game = games.get(i);
                System.out.println("[" + (i + 1) + "] - " + game.gameName() +
                        ": White player: " + game.whiteUsername() +
                        " Black player: " + game.blackUsername());
            }
            System.out.println();
        }catch(Exception ex){
            System.out.println("Error: Something went wrong");
        }
    }

    private void create() {
        // System.out.println("creating game");

        try{
            System.out.print("Enter game name: ");
            String gameName = scanner.nextLine();

            if(gameName.isBlank()){
                System.out.println("Invalid game name");
                return;
            }


            CreateRequest req = new CreateRequest(authToken, gameName);

            var res = server.create(req);

            System.out.println("Game " + gameName + " created.");

        }catch(Exception ex){
            System.out.println("Error: Something went wrong");
        }
    }

    private void join() {
        // System.out.println("joinging a game");

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
                System.out.println("Invalid game ID");
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
            System.out.println("Error: Something went wrong");
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


/*
    private void observe() {
        System.out.println("observing");

        try{
            if(games == null || games.isEmpty()){
                System.out.println("Please list games.");
                return;
            }

            System.out.print("Enter game ID: ");
            int gameId;

            try{
                gameId = Integer.parseInt(scanner.nextLine());

                if(gameId < 1 || gameId > games.size()){
                    System.out.println("Invalid game ID.");
                    return;
                }
            }catch(Exception ex){
                System.out.println("Invalid game ID.");
                return;
            }


            int gameIdReal = games.get(gameId - 1).gameID();

            JoinRequest req = new JoinRequest(authToken, gameIdReal, null);

            var res = server.join(req);

            System.out.println("Observing game " + games.get(gameId - 1).gameName() + " from white's perspective");

            drawBoard(ChessGame.TeamColor.WHITE);

        }catch(Exception ex){
            System.out.println("Error: Something went wrong");
        }
    }
 */
