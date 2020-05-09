package com.wuwind.undercover.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.wuwind.undercover.db.DaoMaster;

import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.query.QueryBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;


/**
 * 数据库帮助类
 * DBHelper.getInstance().init(getContext(), "feng", DaoMaster.class.getName());
 * DBHelper.getInstance().setDebugMode(true);
 */
public class DBHelper {

    private static AbstractDaoMaster daoMaster;
    private static AbstractDaoSession daoSession;

    private DBHelper() {
    }

    private static class InstanceHolder {
        private static DBHelper instance = new DBHelper();
    }

    public static DBHelper getInstance() {
        return InstanceHolder.instance;
    }

    /**
     * 初始化  只要调用一次
     *
     * @param context
     * @param DB_NAME       数据库名称
     * @param daoMasterName DaoMaster的名称  包括包名
     */
    public void init(Context context, String DB_NAME, String daoMasterName) {
        try {
            Class<?> aClass = Class.forName(daoMasterName);

            Class<?> openHelperClass = Class.forName(daoMasterName + "$DevOpenHelper");

            Class[] argType = new Class[]{Context.class, String.class, SQLiteDatabase.CursorFactory.class};
            Object[] argParam = new Object[]{context, DB_NAME, null};
            Constructor constructor = openHelperClass.getConstructor(argType);

            Object oOpenHelper = constructor.newInstance(argParam);

            Method getWritableDatabase = openHelperClass.getMethod("getWritableDatabase");

            Object invoke = getWritableDatabase.invoke(oOpenHelper);


            Class[] argDMType = new Class[]{invoke.getClass()};
            Constructor constructor1 = aClass.getConstructor(argDMType);
            Object o = constructor1.newInstance(invoke);
            DaoMaster.createAllTables(new StandardDatabase((SQLiteDatabase) invoke), true);
            daoMaster = (AbstractDaoMaster) o;
            daoSession = daoMaster.newSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init(Context context, String DB_NAME) {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        try {
            DaoMaster.DevOpenHelper mHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
            SQLiteDatabase db = mHelper.getWritableDatabase();
            // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置是否为调试模式，调试模式会打印查询语句和结果日志
     *
     * @param debug
     */
    public void setDebugMode(boolean debug) {
        QueryBuilder.LOG_SQL = debug;
        QueryBuilder.LOG_VALUES = debug;
    }

    /**
     * 取得DaoMaster
     *
     * @return
     */
    public static AbstractDaoMaster getDaoMaster() {
        return daoMaster;
    }

    /**
     * 取得DaoSession
     *
     * @return
     */
    public static AbstractDaoSession getDaoSession() {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster();
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }
}
