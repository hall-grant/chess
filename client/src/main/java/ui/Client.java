package ui;

import records.RegisterRequest;
import records.RegisterResult;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    private final Scanner scanner = new Scanner(System.in);
    private final ServerFacade server;

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
        authToken = "p";
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
                "4 - play a game\n" +
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

    }

    private void list() {

    }

    private void create() {

    }

    private void join() {

    }


}
