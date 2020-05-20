package com.wuwind.undercover.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;


/**
 * {@inheritDoc}
 *
 * @see AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig wordDaoConfig;
    private final DaoConfig gameDaoConfig;
    private final DaoConfig userDaoConfig;

    private final WordDao wordDao;
    private final GameDao gameDao;
    private final UserDao userDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        wordDaoConfig = daoConfigMap.get(WordDao.class).clone();
        wordDaoConfig.initIdentityScope(type);

        gameDaoConfig = daoConfigMap.get(GameDao.class).clone();
        gameDaoConfig.initIdentityScope(type);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        wordDao = new WordDao(wordDaoConfig, this);
        gameDao = new GameDao(gameDaoConfig, this);
        userDao = new UserDao(gameDaoConfig, this);

        registerDao(Word.class, wordDao);
        registerDao(Game.class, gameDao);
        registerDao(User.class, userDao);
    }

    public void clear() {
        wordDaoConfig.clearIdentityScope();
        gameDaoConfig.clearIdentityScope();
        userDaoConfig.clearIdentityScope();
    }

    public WordDao getWordDao() {
        return wordDao;
    }

    public GameDao getGameDao() {
        return gameDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

}
