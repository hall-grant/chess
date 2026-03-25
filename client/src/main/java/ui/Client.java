package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    public static void main(String[] args){
        Client client = new Client();
        client.run();
    }

    private String authToken = null;

    public void run(){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.println("loop started");
        Scanner scanner = new Scanner(System.in);

        String result = "";


        // break later. Maybe have one or both of the loops return a boolean to kill the loop
        while (true){
            if(authToken == null){
                if(preLogin(scanner)){
                    break;
                }
            }else{
                if(postLogin(scanner)){
                    break;
                }
            }
        }

    }


    private Boolean preLogin(Scanner scanner) {
        System.out.println("Hello. Pre-login:");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("1 - Login\n")
                .append("2 - Register\n")
                .append("3 - Help\n")
                .append("4 - quit\n");
        System.out.print(stringBuilder);

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
    }

    private void register() {
        System.out.println("registering");
    }

    private boolean postLogin(Scanner scanner) {

        return false;
    }

}
