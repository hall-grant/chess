package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.records.CreateRequest;
import service.records.CreateResult;
import service.GameService;

import java.util.Map;

public class CreateHandler {
    private final GameService gameService;
    private final Gson gson;

    public CreateHandler(GameService gameService){
        this.gameService = gameService;
        gson = new GsonBuilder().serializeNulls().create();
    }

    public void handle(Context ctx){
        try{

            String authToken = ctx.header("Authorization");
            if(authToken == null){
                ctx.status(401);
                ctx.result(gson.toJson(Map.of("message", "Error: unauthorized")));
                return;
            }

            Map body = gson.fromJson(ctx.body(), Map.class);

            String gameName = null;

            if (body != null && body.get("gameName") != null) {
                gameName = body.get("gameName").toString();
                // System.out.println("gameName = " + body.get("gameName"));
            }

            CreateRequest req = new CreateRequest(ctx.header("Authorization"), gameName);

            CreateResult res = gameService.create(req);

            ctx.status(200);
            ctx.result(gson.toJson(res));
        } catch(DataAccessException ex){
            if(ex.getMessage().equals("unauthorized")){
                ctx.status(401);
                ctx.result(gson.toJson(Map.of("message", "Error: unauthorized")));
            } else if (ex.getMessage().equals("bad request")){
                ctx.status(400);
                ctx.result(gson.toJson(Map.of("message", "Error: bad request")));
            } else{
                ctx.status(500);
                ctx.result(gson.toJson(Map.of("message", "Error: " + ex.getMessage())));
            }
        }

    }

}


