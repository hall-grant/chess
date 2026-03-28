package client;

import org.junit.jupiter.api.*;
import server.Server;
import records.*;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clearDatabase() throws Exception{
        serverFacade.clear();
    }

    @Test
    public void registerPositive() throws Exception{
        RegisterRequest req = new RegisterRequest("reg1", "pass", "email");
        RegisterResult res = serverFacade.register(req);

        assertNotNull(res.authToken());
    }

    @Test
    public void registerNegative() throws Exception{
        var req = new RegisterRequest("reg1", "pass", "email");
        var res = serverFacade.register(req);

        assertThrows(Exception.class, () -> serverFacade.register(req)); // register twice
    }

}
