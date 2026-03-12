package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUserDao {

    public void createUser(UserData user) throws DataAccessException{
        // first bit
        String command = "INSERT INTO users(username, password, email) VAlUES ";

        // hash password
        String hashPass = BCrypt.hashpw(user.password(), BCrypt.gensalt());

        command = command + "(" + user.username() + ", " + hashPass + ", " + user.email() + ")";

        try(Connection connection = DatabaseManager.getConnection();
        Statement statement = connection.createStatement()){

            statement.executeUpdate(command);

        }catch(SQLException ex){
            throw new DataAccessException("Creation of new user failed");
        }
    }

    public UserData getUser(String username) throws DataAccessException{
        String command = "SELECT username, hashed_password, email FROM Users WHERE username = '" + username + "'";

        try (Connection connection = DatabaseManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(command)){
            // pointer starts before first row, so checks if has at least one row
            if(resultSet.next()){
                String uname = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");

                return new UserData(uname, password, email);
            } else{
                return null;
            }
        } catch(SQLException ex){
            throw new DataAccessException("Getting user failed");
        }
    }

    public List<UserData> listUsers() throws DataAccessException {
        List<UserData> users = new ArrayList<>();

        String command = "SELECT username, password, email FROM users";

        try(Connection connection = DatabaseManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(command)){

            // iterate while there is a row
            while(resultSet.next()){
                String uname = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");

                users.add(new UserData(uname, password, email));
            }
            return users;
        } catch(SQLException ex){
            throw new DataAccessException("Listing users failed");
        }
    }

    public void clear() throws DataAccessException {
        String command = "DELETE FROM users";

        try(Connection connection = DatabaseManager.getConnection();
        Statement statement = connection.createStatement()){
            statement.executeUpdate(command);
        } catch(SQLException ex){
            throw new DataAccessException("Clearing users failed");
        }
    }

}
