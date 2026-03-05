package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.LogoutRequest;
import service.LogoutResult;
import service.UserService;

import java.util.Map;

public class LogoutHandler {

    private final UserService userService;
    private final Gson gson;

    public LogoutHandler(UserService userService){
        this.userService = userService;
        gson = new Gson();
    }

    public void handle(Context ctx){
        try{
            LogoutRequest req = new LogoutRequest(ctx.header("Authorization"));
            LogoutResult res = userService.logout(req);

            ctx.status(200);
            ctx.result(gson.toJson(res));
        }catch(DataAccessException ex){
            if(ex.getMessage().equals("unauthorized")){
                ctx.status(401);
                ctx.result(gson.toJson(Map.of("message", "Error: unauthorized")));
            } else{
                ctx.status(500);
                ctx.result(gson.toJson(Map.of("message", "Error: " + ex.getMessage())));
            }
        }
    }

}
