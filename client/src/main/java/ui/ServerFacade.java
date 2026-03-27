package ui;

import model.AuthData;

import records.*;

public class ServerFacade {
    private final ClientCommunicator clientCommunicator;

    public ServerFacade(int port){
        this.clientCommunicator = new ClientCommunicator("http://localhost:" + port);
    }

    public RegisterResult register(RegisterRequest req) throws Exception {
        return clientCommunicator.register(req);
    }

    public LoginResult login(LoginRequest req) throws Exception{
        return clientCommunicator.login(req);
    }

    public LogoutResult logout(LogoutRequest req) throws Exception{
        return clientCommunicator.logout(req);
    }

    public ListResult list(ListRequest req) throws Exception{
        return clientCommunicator.list(req);
    }

    public CreateResult create(CreateRequest req) throws Exception{

    }

    public JoinResult join(JoinRequest req) throws Exception{
        return clientCommunicator.join(req);
    }

//    public ClearResult clear(){
//
//    }

}
