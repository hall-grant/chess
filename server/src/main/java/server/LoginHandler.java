package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.records.LoginRequest;
import service.records.LoginResult;
import service.UserService;

import java.util.Map;

public class LoginHandler {
    private final Gson gson;
    private final UserService service;

    public LoginHandler(UserService userService){
        this.service = userService;
        gson = new GsonBuilder().serializeNulls().create();
    }

    public void handle(Context ctx){
        try{
            LoginRequest req = gson.fromJson(ctx.body(), LoginRequest.class);
            LoginResult res = service.login(req);
            ctx.status(200);
            ctx.result(gson.toJson(res));
        }catch (DataAccessException ex){
            if(ex.getMessage().equals("bad request")){
                ctx.status(400);
                ctx.result(gson.toJson(Map.of("message", "Error: bad request")));
            } else if (ex.getMessage().equals("already taken")){
                ctx.status(403);
                ctx.result(gson.toJson(Map.of("message", "Error: already taken")));
            }else if (ex.getMessage().equals("unauthorized")){
                ctx.status(401);
                ctx.result(gson.toJson(Map.of("message", "Error: unauthorized")));
            }else{
                ctx.status(500);
                ctx.result(gson.toJson(Map.of("message", "Error: " + ex.getMessage())));
            }
        }
    }

}
