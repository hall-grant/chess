package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class AuthTokenDao {

    private Map<String, AuthData> auths = new HashMap<>();

    public void createAuth(AuthData auth) throws DataAccessException{
        if(!auths.containsKey(auth.userName())){
            auths.put(auth.authToken(), auth);
        }
        throw new DataAccessException("Auth already exists");
    }

    public AuthData getAuth(String authToken) throws DataAccessException{
        if(auths.containsKey(authToken)){
            return auths.get(authToken);

        }
        throw new DataAccessException("AuthToken doesn't exist");
    }

    public void deleteAuth(String authToken) throws DataAccessException{
        if(!auths.containsKey(authToken)){
            throw new DataAccessException("AuthToken doesn't exist");
        }
        auths.remove(authToken);

    }




    public void clear(){
        auths.clear();
    }

}
