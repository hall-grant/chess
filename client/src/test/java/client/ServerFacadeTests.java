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
        sf.register(req);

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


    @Test
    void logoutPositive() throws Exception{
        var reg = sf.register(new RegisterRequest("logout1", "pass", "email"));

        var res = sf.logout(new LogoutRequest(reg.authToken()));

        assertNotNull(res);
    }

    @Test
    void logoutNegative() throws Exception{
        assertThrows(Exception.class, () -> sf.logout(new LogoutRequest("no"))); // doesn't exist
    }


    @Test
    void createPositive() throws Exception{
        var reg = sf.register(new RegisterRequest("create", "pass", "email"));
        var res = sf.create(new CreateRequest(reg.authToken(), "game"));

        assertNotNull(res);
    }

    @Test
    void createNegative() throws Exception{
        assertThrows(Exception.class, () -> sf.create(new CreateRequest("no", "no")));
    }


    @Test
    void listPositive() throws Exception{
        var reg = sf.register(new RegisterRequest("lsit", "pass", "email"));
        sf.create(new CreateRequest(reg.authToken(), "game"));

        var res = sf.list(new ListRequest(reg.authToken()));

        assertNotNull(res.games());
        assertEquals(1, res.games().size());
    }

    @Test
    void listNegative() throws Exception{
        assertThrows(Exception.class, () -> sf.list(new ListRequest("no")));
    }


    @Test
    void joinPositive() throws Exception{
        var reg = sf.register(new RegisterRequest("join", "pass", "email"));

        var createRes = sf.create(new CreateRequest(reg.authToken(), "game"));
        var res = sf.join(new JoinRequest(reg.authToken(), createRes.gameID(), "WHITE"));

        assertNotNull(res);
    }

    @Test
    void joinNegative() throws Exception{
        var reg = sf.register(new RegisterRequest("join", "pass", "email"));

        assertThrows(Exception.class, () -> sf.join(new JoinRequest(reg.authToken(), 69, "WHITE")));
        assertThrows(Exception.class, () -> sf.join(new JoinRequest("no", 1, "WHITE")));
        assertThrows(Exception.class, () -> sf.join(new JoinRequest(reg.authToken(), 1, "YELLOW")));
    }


    @Test
    // idk if this is even required
    void clearPositive() throws Exception{
        var reg = sf.register(new RegisterRequest("clear", "pass", "email"));

        sf.clear();

        assertThrows(Exception.class, () -> sf.login(new LoginRequest("clear", "pass")));
    }



}
