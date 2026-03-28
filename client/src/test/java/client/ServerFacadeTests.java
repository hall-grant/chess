package client;

import org.junit.jupiter.api.*;
import server.Server;
import records.*;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade sf;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        sf = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clearDatabase() throws Exception{
        sf.clear();
    }

    @Test
    public void registerPositive() throws Exception{
        RegisterRequest req = new RegisterRequest("reg1", "pass", "email");
        RegisterResult res = sf.register(req);

        assertNotNull(res.authToken());
    }

    @Test
    public void registerNegative() throws Exception{
        var req = new RegisterRequest("reg1", "pass", "email");
        var res = sf.register(req);

        assertThrows(Exception.class, () -> sf.register(req)); // register twice
    }


    @Test
    public void loginPositive() throws Exception{
        sf.register(new RegisterRequest("log1", "pass", "email"));

        var res = sf.login(new LoginRequest("log1", "pass"));

        assertNotNull(res.authToken());
    }

    @Test
    public void loginNegative() throws Exception{
        assertThrows(Exception.class, () -> sf.login(new LoginRequest("no", "no")));
    }

}
