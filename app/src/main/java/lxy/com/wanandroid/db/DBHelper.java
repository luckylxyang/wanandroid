package lxy.com.wanandroid.db;

import lxy.com.wanandroid.WanApplication;
import lxy.com.wanandroid.greendao.DaoMaster;
import lxy.com.wanandroid.greendao.DaoSession;

/**
 * Creator : lxy
 * date: 2019/3/14
 */

public class DBHelper {
    private static DBHelper instance;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    private DBHelper(){
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(WanApplication.getContext(),
                "search", null);
        DaoMaster mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        daoSession = mDaoMaster.newSession();
    }

    public static DBHelper getInstance(){
        if (instance == null){
            instance = new DBHelper();
        }

        return instance;
    }

    public DaoMaster getDaoMaster() {
        return daoMaster;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

}
