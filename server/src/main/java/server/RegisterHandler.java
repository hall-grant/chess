package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.records.RegisterRequest;
import service.records.RegisterResult;
import service.UserService;

import java.util.Map;


public class RegisterHandler {
    private final UserService service;
    private final Gson gson;

    public RegisterHandler(UserService service){
        this.service = service;
        gson = new GsonBuilder().serializeNulls().create();
    }

    public void handle(Context ctx){
        try{
            RegisterRequest req = gson.fromJson(ctx.body(), RegisterRequest.class); // context body, class type of RegisterRequest
            RegisterResult res = service.register(req);
            ctx.status(200); // OK
            ctx.result(gson.toJson(res));

        }catch (DataAccessException ex){
            if(ex.getMessage().equals("bad request")){
                ctx.status(400);
                ctx.result(gson.toJson(Map.of("message", "Error: bad request")));
            } else if (ex.getMessage().equals("already taken")){
                ctx.status(403);
                ctx.result(gson.toJson(Map.of("message", "Error: already taken")));
            } else{
                ctx.status(500);
                ctx.result(gson.toJson(Map.of("message", "Error: " + ex.getMessage())));
            }
        }
    }

}
