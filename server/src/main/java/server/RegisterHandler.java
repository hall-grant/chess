package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.RegisterRequest;
import service.RegisterResult;
import service.UserService;

import java.util.Map;


public class RegisterHandler {
    private final UserService service;
    private final Gson gson;

    public RegisterHandler(UserService service){
        this.service = service;
        gson = new Gson();
    }

    public void handle(Context ctx){
        try{
            RegisterRequest req = gson.fromJson(ctx.body(), RegisterRequest.class); // context body, class type of RegisterRequest
            RegisterResult res = service.register(req);
            ctx.status(200); // OK
            ctx.json(res);

        }catch (DataAccessException ex){
            if(ex.getMessage().equals("bad request")){
                ctx.status(400);
                ctx.json(Map.of("message","Error: bad request"));
            } else if (ex.getMessage().equals("already taken")){
                ctx.status(403);
                ctx.json(Map.of("message","Error: already taken"));
            } else{
                ctx.status(500);
                ctx.json(Map.of("message", "Error: " + ex.getMessage()));
            }
        }
    }

}
