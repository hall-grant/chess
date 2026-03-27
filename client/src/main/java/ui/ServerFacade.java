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

//    public LoginResult login(LoginRequest req){
//
//    }
//
//    public LogoutResult logout(LogoutRequest req){
//
//    }
//
//    public ListResult list(ListRequest req){
//
//    }
//
//    public CreateResult create(CreateRequest req){
//
//    }
//
//    public JoinResult join(JoinRequest req){
//
//    }
//
//    public ClearResult clear(){
//
//    }

}
