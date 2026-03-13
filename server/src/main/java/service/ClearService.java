package service;

import dataaccess.*;
import service.records.ClearResult;

public class ClearService {
    private final DatabaseUserDao userDao;
    private final DatabaseAuthTokenDao authTokenDao;
    private final GameDao gameDao;

    public ClearService(DatabaseUserDao userDao, DatabaseAuthTokenDao authTokenDao, GameDao gameDao){
        this.userDao = userDao;
        this.authTokenDao = authTokenDao;
        this.gameDao = gameDao;
    }

    public ClearResult clear() throws DataAccessException{
        userDao.clear();
        authTokenDao.clear();
        gameDao.clear();

        return new ClearResult();
    }
}
