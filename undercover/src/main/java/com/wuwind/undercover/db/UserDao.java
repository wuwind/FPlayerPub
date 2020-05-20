package com.wuwind.undercover.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "GAME".
 */
public class UserDao extends AbstractDao<User, Long> {

    public static final String TABLENAME = "GAME";

    /**
     * Properties of entity User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "ID");
        public final static Property Word_id = new Property(1, String.class, "wxId", false, "WXID");
        public final static Property Count = new Property(2, String.class, "wxName", false, "WXNAME");
        public final static Property Normal = new Property(3, String.class, "wxPhoto", false, "WXPHOTO");
        public final static Property Undercover = new Property(4, String.class, "users", false, "USERS");
        public final static Property Blank = new Property(5, Long.class, "gameId", false, "GAMEID");
        public final static Property Audience = new Property(6, String.class, "words", false, "WORDS");
        public final static Property Sequence = new Property(7, String.class, "wordIS", false, "WORDIS");
        public final static Property Out = new Property(8, Integer.class, "ready", false, "READY");
        public final static Property Finish = new Property(9, Integer.class, "uOut", false, "UOUT");
        public final static Property UpdateTime = new Property(10, String.class, "updateTime", false, "UPDATETIME");
    }


    public UserDao(DaoConfig config) {
        super(config);
    }

    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /**
     * Creates the underlying database table.
     */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER\" (" + //
                "\"ID\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id  // id
                "\"WXID\" TEXT," + // 1: word_id  // wxId
                "\"WXNAME\" TEXT," + // 2: count  // wxName
                "\"WXPHOTO\" TEXT," + // 3: normal  // wxPhoto
                "\"USERS\" TEXT," + // 4: undercover  // users
                "\"GAMEID\" INTEGER," + // 5: blank  // gameId
                "\"WORDS\" TEXT," + // 6: audience  // words
                "\"WORDIS\" TEXT," + // 7: sequence  // wordIS
                "\"READY\" INTEGER," + // 7: sequence  // ready
                "\"UOUT\" INTEGER);" + // 8: finish  // uOut
                "\"UPDATETIME\" TEXT);"); // 8: finish  // updateTime
    }

    /**
     * Drops the underlying database table.
     */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"GAME\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, User entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }

        String word_id = entity.getWxId();
        if (word_id != null) {
            stmt.bindString(2, word_id);
        }

        String count = entity.getWxName();
        if (count != null) {
            stmt.bindString(3, count);
        }

        String normal = entity.getWxPhoto();
        if (normal != null) {
            stmt.bindString(4, normal);
        }

        String undercover = entity.getUsers();
        if (undercover != null) {
            stmt.bindString(5, undercover);
        }

        Long blank = entity.getGameId();
        if (blank != null) {
            stmt.bindLong(6, blank);
        }

        String audience = entity.getWords();
        if (audience != null) {
            stmt.bindString(7, audience);
        }

        String sequence = entity.getWordIS();
        if (sequence != null) {
            stmt.bindString(8, sequence);
        }

        Integer out = entity.getReady();
        if (out != null) {
            stmt.bindLong(9, out);
        }

        Integer finish = entity.getuOut();
        if (finish != null) {
            stmt.bindLong(10, finish);
        }

        String updateTime = entity.getUpdateTime();
        if (updateTime != null) {
            stmt.bindString(11, updateTime);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }

        String word_id = entity.getWxId();
        if (word_id != null) {
            stmt.bindString(2, word_id);
        }

        String count = entity.getWxName();
        if (count != null) {
            stmt.bindString(3, count);
        }

        String normal = entity.getWxPhoto();
        if (normal != null) {
            stmt.bindString(4, normal);
        }

        String undercover = entity.getUsers();
        if (undercover != null) {
            stmt.bindString(5, undercover);
        }

        Long blank = entity.getGameId();
        if (blank != null) {
            stmt.bindLong(6, blank);
        }

        String audience = entity.getWords();
        if (audience != null) {
            stmt.bindString(7, audience);
        }

        String sequence = entity.getWordIS();
        if (sequence != null) {
            stmt.bindString(8, sequence);
        }
        Integer out = entity.getReady();
        if (out != null) {
            stmt.bindLong(9, out);
        }

        Integer finish = entity.getuOut();
        if (finish != null) {
            stmt.bindLong(10, finish);
        }

        String updateTime = entity.getUpdateTime();
        if (updateTime != null) {
            stmt.bindString(11, updateTime);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    @Override
    public User readEntity(Cursor cursor, int offset) {
        User entity = new User(
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
                cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // wxId
                cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // wxName
                cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // wxPhoto
                cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // users
                cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5), // gameId
                cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // words
                cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // wordIS
                cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8), // ready
                cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9), // uOut
                cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10) // updateTime
        );
        return entity;
    }

    @Override
    public void readEntity(Cursor cursor, User entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setWxId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setWxName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setWxPhoto(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setUsers(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setGameId(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
        entity.setWords(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setWordIS(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setReady(cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8));
        entity.setuOut(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
        entity.setUpdateTime(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 10));
    }

    @Override
    protected final Long updateKeyAfterInsert(User entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }

    @Override
    public Long getKey(User entity) {
        if (entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(User entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }

}