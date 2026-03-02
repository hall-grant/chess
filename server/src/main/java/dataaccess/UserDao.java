package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class UserDao {

    private Map<String, UserData> users = new HashMap<>();

    public void createUser(UserData user) throws DataAccessException{
        if(users.containsKey(user.username())){
            throw new DataAccessException("Username alrady taken");
        }
        users.put(user.username(), user);
    }

    public UserData getUser(String username){
        if(users.containsKey(username)){
            return users.get(username);
        }
        return null;
    }





    public void clear(){
        users.clear();
    }

}
