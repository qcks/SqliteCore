package com.qcks.sqlitecore.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.qcks.sqlitecore.AppLog;


public class DBhelp extends SQLiteOpenHelper {
    private final String TAG = "DBhelp";
    private IUpDateDbCallBack upDateDBCallBack;

    /**
     * 更新数据库回调接口
     */
    public interface IUpDateDbCallBack {
        /**
         * 更新数据库
         * @param database db
         * @param oldVersion 旧版本号
         * @param newVersion 最新版本号
         */
        void onUpDateTo(SQLiteDatabase database, int oldVersion, int newVersion);
    }


    public DBhelp(Context context,String daName,IUpDateDbCallBack upDateDBCallBack) {
        /**
         * 第三个参数：CursorFactory指定在执行查询时获得一个游标实例的工厂类,设置为null,代表使用系统默认的工厂类
         * 第四个参数：是数据库的版本号 数据库只能升级,不能降级,版本号只能变大不能变小
         */
        super(context, daName, null, DataBaseConfig.getVersion(daName));
        this.upDateDBCallBack = upDateDBCallBack;
        AppLog.d(TAG, "数据库DBhelp");
    }

    /**
     * onCreate是在数据库创建的时候调用的，主要用来初始化数据表结构和插入数据初始化的记录
     * <p/>
     * 当数据库第一次被创建的时候调用的方法,适合在这个方法里面把数据库的表结构定义出来. 所以只有程序第一次运行的时候才会执行
     * 如果想再看到这个函数执行，必须卸载程序然后重新安装这个app
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        AppLog.d(TAG, "创建数据库");
    }

    /**
     * 当数据库更新的时候调用的方法 这个要版本号发生改变时才会执行
     * 注意，数据库的版本号只可以变大，不能变小，假设我们当前写的版本号是3，运行，然后又改成1，运行则报错。不能变小
     *
     * @param db
     * @param oldVersion 旧的版本号
     * @param newVersion 新
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        AppLog.d(TAG, "数据库更新");
        if(upDateDBCallBack != null){
            upDateDBCallBack.onUpDateTo(db,oldVersion,newVersion);
        }
    }

    /**
     * 当数据库打开时调用。实现 *在更新之前，
     *
     * @param db The database.
     */
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    /**
     * 在配置数据库连接时调用，以启用特性
     *
     * @param db The database.
     */
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
    }

    /**
     * 当数据库需要降级时调用
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    public void setUpDateDBCallBack(IUpDateDbCallBack upDateDBCallBack) {
        this.upDateDBCallBack = upDateDBCallBack;
    }
}
