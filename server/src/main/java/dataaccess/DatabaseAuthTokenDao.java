package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class DatabaseAuthTokenDao {

    // private final Map<String, AuthData> auths = new HashMap<>();

    public void createAuth(AuthData auth) throws DataAccessException{

        String command = "INSERT INTO authTokens(authToken, username) VALUES ('"
                + auth.authToken() + "', '"
                + auth.userName() + "')";

        try(Connection connection = DatabaseManager.getConnection();
        Statement statement = connection.createStatement()){
            statement.executeUpdate(command);
        } catch(SQLException ex){
            throw new DataAccessException("Creation of auth failed");
        }
    }

    public AuthData getAuth(String authToken) throws DataAccessException{

        String command = "SELECT authToken, username FROM authTokens WHERE authToken = '" + authToken + "'";

        try(Connection connection = DatabaseManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(command)){

            if(resultSet.next()){
                String auth = resultSet.getString("authToken");
                String username = resultSet.getString("username");

                return new AuthData(auth, username);
            }
            return null;

        } catch(SQLException ex){
            throw new DataAccessException("Getting auth failed");
        }

    }

    public void deleteAuth(String authToken) throws DataAccessException{

        String command = "DELETE FROM authTokens WHERE authToken = '" + authToken + "'";

        try(Connection connection = DatabaseManager.getConnection();
        Statement statement = connection.createStatement()){
            if(statement.executeUpdate(command) <= 0){
                throw new DataAccessException("authToken doesn't exist");
            }
        } catch(SQLException ex){
            throw new DataAccessException("Deleting auth failed");
        }

    }

    public void clear() throws DataAccessException{

        String command = "DELETE FROM authTokens";

        try(Connection connection = DatabaseManager.getConnection();
        Statement statement = connection.createStatement()){
            statement.executeUpdate(command);
        } catch(SQLException ex){
            throw new DataAccessException("Deleting auth table failed");
        }
    }


}
