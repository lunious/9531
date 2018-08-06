package com.lubanjianye.biaoxuntong.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;

/**
 * 项目名:   LuBanBiaoXunTong
 * 包名:     com.lubanjianye.biaoxuntong.pw.database
 * 文件名:   ReleaseOpenHelper
 * 创建者:   lunious
 * 创建时间: 2017/10/16  23:37
 * 描述:     TODO
 */

public class ReleaseOpenHelper extends DaoMaster.OpenHelper {
    public ReleaseOpenHelper(Context context, String name) {
        super(context, name);
    }

    public ReleaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onCreate(Database db) {
        super.onCreate(db);
    }
}
