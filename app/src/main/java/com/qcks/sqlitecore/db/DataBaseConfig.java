package com.qcks.sqlitecore.db;


import com.qcks.sqlitecore.exception.dbexception.DbException;

/**
 * 数据库配置
 * Created by qckiss on 2018/1/7.
 */

public class DataBaseConfig {
    /**
     * 数据库名称
     */
    public static final String MAIN_DB = "main.db";

    public static final String OTHER_DB = "other.db";

    public DataBaseConfig() {

    }

    public static int getVersion(String dbName) {
        if(dbName.equals(MAIN_DB)){
            return 2;
        }else if(dbName.equals(OTHER_DB)){
            return 1;
        }else {
            throw new DbException("Unable to find the database,找不到数据库="+dbName);
        }
    }
}
