package server.websocket;

import com.google.gson.Gson;
import io.javalin.websocket.WsContext;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private final Map<Integer, Set<WsContext>> connections = new ConcurrentHashMap<>();
    private final Gson gson = new Gson();

    public void add(int gameID, WsContext ctx) {

        Set<WsContext> c = connections.get(gameID);
        if(c == null){
            c = ConcurrentHashMap.newKeySet();
            connections.put(gameID, c);
        }
        c.add(ctx); // should be assigned to same object, not copy
    }

    public void remove(int gameID, WsContext ctx) {

        Set<WsContext> set = connections.get(gameID);
        if(set != null){
            set.remove(ctx);
        }
    }

    public void broadcastAll(int gameID, Object message) {

        Set<WsContext> set = connections.get(gameID);
        if(set == null){
            return;
        }

        String string = gson.toJson(message);

        for (var c  : set){
            if(c.session.isOpen()){
                c.send(string);
            }
        }
    }

    public void broadcastExclude(WsContext ctx, int gameID, Object message){

        Set<WsContext> set = connections.get(gameID);
        if(set == null){
            return;
        }

        String string = gson.toJson(message);
        for (var c : set){
            if(c.session.isOpen()){
                if(!c.equals(ctx)){ // c != ctx
                    c.send(string);
                }
            }
        }
    }

}