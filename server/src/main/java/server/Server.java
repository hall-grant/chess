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
        DatabaseUserDao userDao = new DatabaseUserDao();
        AuthTokenDao authDao = new AuthTokenDao();
        GameDao gameDao = new GameDao();



        // Services and handlers
        // clear
        ClearService clearService = new ClearService(userDao, authDao, gameDao);
        ClearHandler clearHandler = new ClearHandler(clearService);

        // register
        UserService userService = new UserService(userDao, authDao);
        RegisterHandler registerHandler = new RegisterHandler(userService);

        // login
        LoginHandler loginHandler = new LoginHandler(userService);

        // logout
        LogoutHandler logoutHandler = new LogoutHandler(userService);

        // create
        GameService gameService = new GameService(gameDao, authDao);
        CreateHandler gameHandler = new CreateHandler(gameService);

        // list
        ListHandler listHandler = new ListHandler(gameService);

        // join
        JoinHandler joinHandler = new JoinHandler(gameService);



        // Endpoints
        javalin.delete("/db", ctx -> clearHandler.handle(ctx));         // clear
        javalin.post("/user", ctx -> registerHandler.handle(ctx));      // register
        javalin.post("/session", ctx -> loginHandler.handle(ctx));      // login
        javalin.delete("/session", ctx -> logoutHandler.handle(ctx));   // logout
        javalin.post("/game", ctx -> gameHandler.handle(ctx));          // create
        javalin.get("/game", ctx -> listHandler.handle(ctx));           // list
        javalin.put("/game", ctx -> joinHandler.handle(ctx));           // join

    }

    public int run(int desiredPort) {

        try {
            DatabaseManager.createDatabase();

            DatabaseManager.createUserTable();
            DatabaseManager.createAuthTable();
            DatabaseManager.createGamesTable(); // stub for now
        } catch(DataAccessException ex){
            throw new RuntimeException("Database initialization fail", ex);
        }

        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
