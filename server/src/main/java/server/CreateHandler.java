package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.CreateRequest;
import service.CreateResult;
import service.GameService;

import java.util.Map;

public class CreateHandler {
    private final GameService gameService;
    private final Gson gson;

    public CreateHandler(GameService gameService){
        this.gameService = gameService;
        gson = new Gson();
    }

    public void handle(Context ctx){
        try{
            Map body = gson.fromJson(ctx.body(), Map.class);

            String gameName = null;
            /*
            if(body == null){
                gameName = null; // needed
            } else{
                gameName = body.get("gameName").toString();
            }
            */
            if (body != null && body.get("gameName") != null) {
                gameName = body.get("gameName").toString();
            }

            CreateRequest req = new CreateRequest(ctx.header("Authorization"), gameName);

            CreateResult res = gameService.create(req);

            ctx.status(200);
            ctx.json(res);
        } catch(DataAccessException ex){
            if(ex.getMessage().equals("unauthorized")){
                ctx.status(401);
                ctx.json(Map.of("message", "Error: unauthorized"));
            } else if (ex.getMessage().equals("bad request")){
                ctx.status(400);
                ctx.json(Map.of("message", "Error: bad request"));
            } else{
                ctx.status(500);
                ctx.json(Map.of("message", "Error: " + ex.getMessage()));
            }
        }

    }

}
