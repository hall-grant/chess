package dataaccess;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static String databaseName;
    private static String dbUsername;
    private static String dbPassword;
    private static String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        loadPropertiesFromResources();
    }

    /**
     * Creates the database if it does not already exist.
     */
    static public void createDatabase() throws DataAccessException {
        var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
             var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create database", ex);
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DatabaseManager.getConnection()) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataAccessException {
        try {
            //do not wrap the following line with a try-with-resources
            var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException ex) {
            throw new DataAccessException("failed to get connection", ex);
        }
    }

    private static void loadPropertiesFromResources() {
        try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
            if (propStream == null) {
                throw new Exception("Unable to load db.properties");
            }
            Properties props = new Properties();
            props.load(propStream);
            loadProperties(props);
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties", ex);
        }
    }

    private static void loadProperties(Properties props) {
        databaseName = props.getProperty("db.name");
        dbUsername = props.getProperty("db.user");
        dbPassword = props.getProperty("db.password");

        var host = props.getProperty("db.host");
        var port = Integer.parseInt(props.getProperty("db.port"));
        connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
    }



    public static void createUserTable() throws DataAccessException{
        String command = """
                CREATE TABLE IF NOT EXISTS users (
                id INT AUTO_INCREMENT PRIMARY KEY,
                username VARCHAR(128) NOT NULL UNIQUE,
                password VARCHAR(128) NOT NULL,
                email VARCHAR(128) NOT NULL)
                """;

        try (Connection connection = getConnection();
        Statement statement = connection.createStatement()){
            statement.executeUpdate(command);
        } catch(SQLException ex){
            throw new DataAccessException("Creation of users table failed");
        }
    }

    public static void createAuthTable() throws DataAccessException{
        String command = """
                CREATE TABLE IF NOT EXISTS authTokens (
                authToken VARCHAR(128) PRIMARY KEY,
                username VARCHAR(128) NOT NULL)
                """;

        try (Connection connection = getConnection();
        Statement statement = connection.createStatement()){
            statement.executeUpdate(command);
        } catch(SQLException ex){
            throw new DataAccessException("Creation of auth table failed");
        }
    }

    public static void createGamesTable() throws DataAccessException{
        // don't auto increment gameID for now. maybe change later if bugs
        String command = """
                CREATE TABLE IF NOT EXISTS games (
                gameID INT AUTO_INCREMENT PRIMARY KEY,
                whiteUsername VARCHAR(255),
                blackUsername VARCHAR(255),
                gameName VARCHAR(255) NOT NULL,
                game TEXT NOT NULL)
                """; // game should probably be a TEXT
        try (Connection connection = getConnection();
        Statement statement = connection.createStatement()){
            statement.executeUpdate(command);
        }catch(SQLException ex){
            throw new DataAccessException("Creation of games table failed", ex);
        }
    }

}
