package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDao {

    private final Map<String, UserData> users = new HashMap<>();

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

    public List<UserData> listUsers(){
        return new ArrayList<>(users.values());
    }





    public void clear(){
        users.clear();
    }

}
