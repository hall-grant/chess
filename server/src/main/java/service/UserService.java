package service;

import dataaccess.AuthTokenDao;
import dataaccess.DataAccessException;
import dataaccess.DatabaseUserDao;
import dataaccess.UserDao;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import service.records.*;

import java.util.UUID;

public class UserService {

    private final DatabaseUserDao userDao;
    private final AuthTokenDao authDao;

    public UserService(DatabaseUserDao userDao, AuthTokenDao authDao) {
        this.userDao = userDao;
        this.authDao = authDao;
    }

    public RegisterResult register(RegisterRequest r) throws DataAccessException {
        if(r.username() != null && r.password() != null && r.email() != null){
            if(userDao.getUser(r.username()) == null){
                // creating user data
                UserData user = new UserData(r.username(), r.password(), r.email());
                userDao.createUser(user);

                // creating auth data
                String authToken = UUID.randomUUID().toString();
                // make sure uuid is random:
                while(authDao.getAuth(authToken) != null){
                    authToken = UUID.randomUUID().toString();
                }
                AuthData newAuth = new AuthData(authToken, r.username());
                authDao.createAuth(newAuth);

                return new RegisterResult(r.username(), authToken);
            }
            throw new DataAccessException("already taken");
        }
        throw new DataAccessException("bad request");
    }


    public LoginResult login(LoginRequest r) throws DataAccessException{
        if(r == null || r.username () == null || r.password() == null){
            throw new DataAccessException("bad request");
        }

        // check if username is correct
        UserData user = userDao.getUser(r.username());
        if(user == null){
            throw new DataAccessException("unauthorized");
        }

        // compare hashed password
        // user.password().equals(r.password())
        if(!BCrypt.checkpw(r.password(), user.password())){
            throw new DataAccessException("unauthorized");
        }

        // return an auth
        String authToken = UUID.randomUUID().toString();
        // make sure it's unique
        while(authDao.getAuth(authToken) != null){
            authToken = UUID.randomUUID().toString();
        }

        AuthData authData = new AuthData(authToken, r.username());
        authDao.createAuth(authData);

        return new LoginResult(r.username(), authToken);

    }


    public LogoutResult logout(LogoutRequest r) throws DataAccessException{
        if(r != null && r.authToken() != null){
            AuthData auth = authDao.getAuth(r.authToken());
            if(auth != null){
                authDao.deleteAuth(r.authToken());
                return new LogoutResult();
            } else{
                throw new DataAccessException("unauthorized");
            }
        }

        throw new DataAccessException("unauthorized");

    }


}
