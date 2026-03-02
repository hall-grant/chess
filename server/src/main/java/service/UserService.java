package service;

import dataaccess.AuthTokenDao;
import dataaccess.DataAccessException;
import dataaccess.UserDao;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {

    private final UserDao userDao;
    private final AuthTokenDao authDao;

    public UserService(UserDao userDao, AuthTokenDao authDao) {
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


}
