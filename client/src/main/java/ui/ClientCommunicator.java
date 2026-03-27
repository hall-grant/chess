package ui;

import java.net.URI;
import com.google.gson.Gson;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.net.http.HttpClient;
import java.util.Map;

import records.*;

public class ClientCommunicator {

    // private final ServerFacade server;
    private final Gson gson = new Gson();
    private final HttpClient client;
    private final String serverUrl;

    public ClientCommunicator(String serverUrl){
        client = HttpClient.newHttpClient();
        this.serverUrl = serverUrl;
    }

    // should I be using DataAccessException?
    public RegisterResult register(RegisterRequest req) throws Exception{
        System.out.println("Entering CC register method");

        try {
            var builder = HttpRequest.newBuilder().uri(URI.create(serverUrl + "/user"));

            builder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(req)));

            HttpRequest request = builder.build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status: " + response.statusCode());
            System.out.println("Body: " + response.body());

            if (response.statusCode() != 200) {
                Map error = gson.fromJson(response.body(), Map.class);
                throw new Exception((String) error.get("message"));
            }

            return gson.fromJson(response.body(), RegisterResult.class);
        }catch(Exception ex){
            ex.printStackTrace(); // debugging
            throw ex;
        }
    }

    public LoginResult login(LoginRequest req) throws Exception{
        System.out.println("entering CC login method");

        try{
            var builder = HttpRequest.newBuilder().uri(URI.create(serverUrl + "/session"));

            builder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(req)));

            HttpRequest request = builder.build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status: " + response.statusCode());
            System.out.println("Body: " + response.body());

            if(response.statusCode() != 200){
                Map error = gson.fromJson(response.body(), Map.class);
                throw new Exception((String) error.get("message"));
            }

            return gson.fromJson(response.body(), LoginResult.class);

        } catch(Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    public LogoutResult logout(LogoutRequest req) throws Exception{
        System.out.println("entering CC logout method");

        try{
            var builder = HttpRequest.newBuilder().uri(URI.create(serverUrl + "/session"));

            builder.header("Authorization", req.authToken());

            builder.DELETE();
            HttpRequest request = builder.build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status: " + response.statusCode());
            System.out.println("Body: " + response.body());

            if(response.statusCode() != 200){
                Map error = gson.fromJson(response.body(), Map.class);
                throw new Exception((String) error.get("message"));
            }

            return gson.fromJson(response.body(), LogoutResult.class);
        }catch(Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

}
