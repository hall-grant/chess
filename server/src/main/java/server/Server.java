package server;

import dataaccess.*;

import io.javalin.*;

import service.*;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.

        // DAOs
        UserDao userDao = new UserDao();
        AuthTokenDao authDao = new AuthTokenDao();
        GameDao gameDao = new GameDao();



        // Services and handlers
        ClearService clearService = new ClearService(userDao, authDao, gameDao);
        ClearHandler clearHandler = new ClearHandler(clearService);

        UserService userService = new UserService(userDao, authDao);
        RegisterHandler registerHandler = new RegisterHandler(userService);

        LoginHandler loginHandler = new LoginHandler(userService);

        LogoutHandler logoutHandler = new LogoutHandler(userService);

        GameService gameService = new GameService(gameDao, authDao);
        CreateHandler gameHandler = new CreateHandler(gameService);



        // Endpoints
        javalin.delete("/db", ctx -> clearHandler.handle(ctx));         // clear
        javalin.post("/user", ctx -> registerHandler.handle(ctx));      // register
        javalin.post("/session", ctx -> loginHandler.handle(ctx));      // login
        javalin.delete("/session", ctx -> logoutHandler.handle(ctx));   // logout
        javalin.post("/game", ctx -> gameHandler.handle(ctx));          // create

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
