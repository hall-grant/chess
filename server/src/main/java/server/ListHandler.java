package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.GameService;
import service.records.ListRequest;
import service.records.ListResult;

import java.util.Map;

public class ListHandler {
    private final GameService gameService;
    private final Gson gson;

    public ListHandler(GameService gameService){
        this.gameService = gameService;
        gson = new GsonBuilder().serializeNulls().create();
    }

    public void handle(Context ctx){
        try{
            ListRequest req = new ListRequest(ctx.header("Authorization"));
            ListResult res = gameService.list(req);

            ctx.status(200);
            ctx.result(gson.toJson(res));
        } catch(DataAccessException ex){
            if(ex.getMessage().equals("unauthorized")){
                ctx.status(401);
                ctx.result(gson.toJson(Map.of("message", "Error: unauthorized")));
            }else{
                ctx.status(500);
                ctx.result(gson.toJson(Map.of("message", "Error: " + ex.getMessage())));
            }
        }
    }

}
