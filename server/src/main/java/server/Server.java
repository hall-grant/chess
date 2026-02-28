package server;

import dataaccess.*;

import io.javalin.*;

import service.ClearService;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        UserDao userDao = new UserDao();
        AuthTokenDao authDao = new AuthTokenDao();
        GameDao gameDao = new GameDao();

        ClearService clearService = new ClearService(userDao, authDao, gameDao);
        ClearHandler clearHandler = new ClearHandler(clearService);

        // javalin.delete("/db", clearHandler);
        javalin.delete("/db", clearHandler::handle);

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
