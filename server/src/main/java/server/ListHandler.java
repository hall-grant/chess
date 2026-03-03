package server;

import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.GameService;
import service.ListRequest;
import service.ListResult;

import java.util.Map;

public class ListHandler {
    private final GameService gameService;

    public ListHandler(GameService gameService){
        this.gameService = gameService;
    }

    public void handle(Context ctx){
        try{
            ListRequest req = new ListRequest(ctx.header("Authorization"));
            ListResult res = gameService.list(req);

            ctx.status(200);
            ctx.json(res);
        } catch(DataAccessException ex){
            if(ex.getMessage().equals("unauthorized")){
                ctx.status(401);
                ctx.json(Map.of("message", "Error: unauthorized"));
            }else{
                ctx.status(500);
                ctx.json(Map.of("message", "Error: " + ex.getMessage()));
            }
        }
    }

}
