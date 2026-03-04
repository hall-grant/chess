package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.GameService;
import service.JoinRequest;
import service.JoinResult;

import java.util.Map;

public class JoinHandler {
    private final GameService gameService;
    private final Gson gson;

    public JoinHandler(GameService gameService){
        this.gameService = gameService;
        gson = new Gson();
    }

    public void handle(Context ctx){
        try{
            Map body = gson.fromJson(ctx.body(), Map.class);

            String playerColor = null;

            // why is int stuff so complex?
            int gameID = 0;
            boolean hasID = false;
            if(body != null){
                if(body.get("gameID") != null){
                    // gameID = (int) body.get("gameID");
                    gameID = ((Double) body.get("gameID")).intValue();
                    hasID = true;
                }

                if(body.get("playerColor") != null){ // have to check
                    playerColor = body.get("playerColor").toString();
                }
            }

            if(!hasID){
                throw new DataAccessException("bad request");
            }
            JoinRequest req = new JoinRequest(ctx.header("Authorization"), gameID, playerColor);

            JoinResult res = gameService.join(req);

            ctx.status(200);
            ctx.json(res);

        }catch(DataAccessException ex){
            if(ex.getMessage().equals("unauthorized")){
                ctx.status(401);
                ctx.json(Map.of("message", "Error: unauthorized"));
            } else if(ex.getMessage().equals("bad request")){
                ctx.status(400);
                ctx.json(Map.of("message", "Error: bad request"));
            } else if(ex.getMessage().equals("already taken")){
                ctx.status(403);
                ctx.json(Map.of("message", "Error: already taken"));
            } else{
                ctx.status(500);
                ctx.json(Map.of("message", "Error: " + ex.getMessage()));
            }
        }
    }

}
