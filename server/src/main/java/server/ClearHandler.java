package server;

import dataaccess.DataAccessException;
import io.javalin.*;
import io.javalin.http.Context;
import service.ClearResult;
import service.ClearService;

import java.util.Map;

public class ClearHandler {
    private final ClearService service;

    public ClearHandler(ClearService service){
        this.service = service;
    }

    public void handle(Context ctx){
        try{
            ClearResult res = service.clear();

            ctx.status(200); // ok
            ctx.json(res); // empty object
        }catch(DataAccessException ex){
            ctx.status(500);
            ctx.json(Map.of("message", "Error: " + ex.getMessage()));
        }
    }
}
