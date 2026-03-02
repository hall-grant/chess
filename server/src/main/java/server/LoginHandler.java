package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.LoginRequest;
import service.LoginResult;
import service.UserService;

import java.util.Map;

public class LoginHandler {
    private final Gson gson;
    private final UserService service;

    public LoginHandler(UserService userService){
        this.service = userService;
        gson = new Gson();
    }

    public void handle(Context ctx){
        try{
            LoginRequest req = gson.fromJson(ctx.body(), LoginRequest.class);
            LoginResult res = service.login(req);
            ctx.status(200);
            ctx.json(res);
        }catch (DataAccessException ex){
            if(ex.getMessage().equals("bad request")){
                ctx.status(400);
                ctx.json(Map.of("message", "Error: bad request"));
            } else if (ex.getMessage().equals("already taken")){
                ctx.status(403);
                ctx.json(Map.of("message", "Error: already taken"));
            }else if (ex.getMessage().equals("unauthorized")){
                ctx.status(401);
                ctx.json(Map.of("message", "Error: unauthorized"));
            }else{
                ctx.status(500);
                ctx.json(Map.of("message", "Error: " + ex.getMessage()));
            }
        }
    }

}
