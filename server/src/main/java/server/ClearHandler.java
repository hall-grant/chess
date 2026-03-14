package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.records.ClearResult;
import service.ClearService;

import java.util.Map;

public class ClearHandler {
    private final ClearService service;
    private final Gson gson;

    public ClearHandler(ClearService service){
        this.service = service;
        gson = new GsonBuilder().serializeNulls().create();
    }

    public void handle(Context ctx){
        try{
            ClearResult res = service.clear();

            ctx.status(200); // ok
            ctx.result(gson.toJson(res)); // empty object
        }catch(DataAccessException ex){
            ctx.status(500);
            ctx.result(gson.toJson(Map.of("message", "Error: " + ex.getMessage())));
        }
    }
}
