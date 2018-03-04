package com.qcks.sqlitecore.exception.dbexception;


import com.qcks.sqlitecore.exception.BaseException;

/**
 * 自定义异常类(继承运行时异常)
 * Created by qckiss on 2018/1/19.
 */
public class DbException extends BaseException {

    private static final long serialVersionUID = 1L;
    /**
     * 字段注解 类属性的第一行注解应当是数据库字段注解
     * Can not find a class with the passing class name.
     */
    public static final String ATTRIBUTE_ANNOTATION_NOT_FIRST = " The first line of the class attribute should be a database field annotation.";

    /**
     * 没有主键
     */
    public static final String NO_PRIMARY_KEY = "The data table has no primary key or primary key is NULL,毫无主见 ";

    /**
     * Can not find a class with the passing class name.
     */
    public static final String CLASS_NOT_FOUND = "can not find a class named ";

    /**
     * An exception that indicates there was an error with SQL parsing or
     * execution.
     */
    public static final String SQL_ERROR = "An exception that indicates there was an error with SQL parsing or execution. ";

    /**
     * SQL syntax error when executing generation job.
     */
    public static final String SQL_SYNTAX_ERROR = "SQL syntax error happens while executing ";

    /**
     * Can not find a table with the passing table name when executing SQL.
     */
    public static final String TABLE_DOES_NOT_EXIST_WHEN_EXECUTING = "Table doesn't exist when executing ";

    /**
     * Can not find a table with the passing table name.
     */
    public static final String TABLE_DOES_NOT_EXIST = "Table doesn't exist with the name of ";

    /**
     * Don't have permission to create database on sdcard.
     * 不要在sdcard上创建数据库
     */
    public static final String EXTERNAL_STORAGE_PERMISSION_DENIED = "You don't have permission to access database at %1$s. Make sure you handled WRITE_EXTERNAL_STORAGE runtime permission correctly.";

    /**
     * 错误编码
     */
    private String errorCode;

    /**
     * 消息是否为属性文件中的Key
     */
    private boolean propertiesKey = true;

    /**
     * 构造一个基本异常.
     *
     * @param message 信息描述
     */
    public DbException(String message) {
        super(message);
    }

    /**
     * 构造一个基本异常.
     *
     * @param errorCode 错误编码
     * @param message   信息描述
     */
    public DbException(String errorCode, String message) {
        this(errorCode, message, true);
    }

    /**
     * 构造一个基本异常.
     *
     * @param errorCode 错误编码
     * @param message   信息描述
     */
    public DbException(String errorCode, String message, Throwable cause) {
        this(errorCode, message, cause, true);
    }

    /**
     * 构造一个基本异常.
     *
     * @param errorCode     错误编码
     * @param message       信息描述
     * @param propertiesKey 消息是否为属性文件中的Key
     */
    public DbException(String errorCode, String message, boolean propertiesKey) {
        super(message);
        this.setErrorCode(errorCode);
        this.setPropertiesKey(propertiesKey);
    }

    /**
     * 构造一个基本异常.
     *
     * @param errorCode 错误编码
     * @param message   信息描述
     */
    public DbException(String errorCode, String message, Throwable cause, boolean propertiesKey) {
        super(message, cause);
        this.setErrorCode(errorCode);
        this.setPropertiesKey(propertiesKey);
    }

    /**
     * 构造一个基本异常.
     *
     * @param message 信息描述
     * @param cause   根异常类（可以存入任何异常）
     */
    public DbException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isPropertiesKey() {
        return propertiesKey;
    }

    public void setPropertiesKey(boolean propertiesKey) {
        this.propertiesKey = propertiesKey;
    }

}