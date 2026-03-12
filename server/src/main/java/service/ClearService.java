package service;

import dataaccess.*;
import service.records.ClearResult;

public class ClearService {
    private final DatabaseUserDao userDao;
    private final AuthTokenDao authTokenDao;
    private final GameDao gameDao;

    public ClearService(DatabaseUserDao userDao, AuthTokenDao authTokenDao, GameDao gameDao){
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
