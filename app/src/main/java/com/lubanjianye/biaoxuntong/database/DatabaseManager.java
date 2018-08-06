package com.lubanjianye.biaoxuntong.database;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

/**
 * 项目名:   LuBanBiaoXunTong
 * 包名:     com.lubanjianye.biaoxuntong.pw.database
 * 文件名:   DatabaseManager
 * 创建者:   lunious
 * 创建时间: 2017/10/16  23:37
 * 描述:     TODO
 */

public class DatabaseManager {

    private DaoSession mDaoSession = null;
    private UserProfileDao mDao = null;

    private DatabaseManager() {
    }

    public DatabaseManager init(Context context) {
        initDao(context);
        return this;
    }

    private static final class Holder {
        private static final DatabaseManager INSTANCE = new DatabaseManager();
    }

    public static DatabaseManager getInstance() {
        return Holder.INSTANCE;
    }

    private void initDao(Context context) {
        final ReleaseOpenHelper helper = new ReleaseOpenHelper(context, "fast_ec.db");
        final Database db = helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
        mDao = mDaoSession.getUserProfileDao();
    }

    public final UserProfileDao getDao() {
        return mDao;
    }


}

