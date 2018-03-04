package com.qcks.sqlitecore.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Xml;

import com.qcks.sqlitecore.AppLog;
import com.qcks.sqlitecore.annotation.DbFields;
import com.qcks.sqlitecore.annotation.DbTable;
import com.qcks.sqlitecore.bean.SqlDataType;
import com.qcks.sqlitecore.db.DBhelp;
import com.qcks.sqlitecore.exception.dbexception.DbException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.content.res.AssetManager.ACCESS_STREAMING;
import static com.qcks.sqlitecore.exception.dbexception.DbException.ATTRIBUTE_ANNOTATION_NOT_FIRST;
import static com.qcks.sqlitecore.exception.dbexception.DbException.NO_PRIMARY_KEY;

/**
 * BaseDao
 * 自动建表，表升级
 *
  * @date 2017-12-31
 */

public class BaseDao<T> implements IBaseDao<T> {
    private final String TAG = "BaseDao";
    private SQLiteDatabase db;
    private DBhelp dBhelp;
    private Class<T> entityClass;
    private String tableName;
    /**
     * 字段名--字段 关系缓存
     */
    private HashMap<String, Field> cacheMap;
    private boolean isInit;
    private boolean havePrimaryKey;
    private String pkFieldName;
    private String pkName;
    private String relevant_fk;

    public BaseDao(final Context context, final Class<T> entityClass) {
        this.entityClass = entityClass;
        AppLog.d(TAG, "获取帮助类来管理数据库创建和版本管理");
        String dbName = entityClass.getAnnotation(DbTable.class).dbName().toLowerCase();
        dBhelp = new DBhelp(context, dbName, new DBhelp.IUpDateDbCallBack() {
            @Override
            public void onUpDateTo(SQLiteDatabase database, int oldVersion, int newVersion) {
                upDateTable(context, database, oldVersion, newVersion);
                AppLog.d(TAG, "数据库更新完成");
            }
        });
        AppLog.d(TAG, "获取数据库连接");
        db = dBhelp.getWritableDatabase();
        AppLog.d(TAG, "数据库name=" + dBhelp.getDatabaseName(), "数据库path=" + db.getPath());
        init(entityClass);
    }

    /**
     * 初始化
     *
     * @param entityClass
     */
    private void init(Class<T> entityClass) {
        if (isInit || !db.isOpen()) {
            return;
        }
        tableName = entityClass.getAnnotation(DbTable.class).tableName().toLowerCase();
        if (tableIsExist(tableName)) {
            AppLog.d(TAG, "数据表已存在");
            isInit = true;
            //缓存 表字段——属性 关系
            initCacheMap();
        } else {
            //数据表不存在，创建
            if (createTable(entityClass)) {
                isInit = true;
                initCacheMap();
            }
        }
    }

    /**
     * 数据库更新
     *
     * @param context
     * @param database
     * @param oldVersion
     * @param newVersion
     */
    private void upDateTable(Context context, SQLiteDatabase database, int oldVersion, int newVersion) {
        for (int version = oldVersion + 1; version <= newVersion; version++) {
            AppLog.d(TAG, "数据库更新到版本" + version);
            try {
                InputStream inputStream = context.getAssets().open("database_update.xml", ACCESS_STREAMING);
                XmlPullParser pullParser = Xml.newPullParser();
                pullParser.setInput(inputStream, "UTF-8");
                // 取得事件
                int event = pullParser.getEventType();
                int upVersion = 0;
                String upDbName = "";
                String upTabName = "";
                // 若为解析到末尾
                while (event != XmlPullParser.END_DOCUMENT) {
                    String nodeName = pullParser.getName();
                    switch (event) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        // 标签开始
                        case XmlPullParser.START_TAG:
                            AppLog.d(TAG, "nodeName=" + nodeName);
                            if ("update".equals(nodeName)) {
                                upVersion = Integer.valueOf(pullParser.getAttributeValue(0));
                                AppLog.d(TAG, "upVersion=" + upVersion);
                            }
                            if ("dbNames".equals(nodeName)) {
                                upDbName = pullParser.getAttributeValue(0);
                                AppLog.d(TAG, "upDbName=" + upDbName);
                            }
                            if ("updateTable".equals(nodeName)) {
                                upTabName = pullParser.getAttributeValue(0);
                                AppLog.d(TAG, "upTabName=" + upTabName);
                            }
                            if ("sql".equals(nodeName)) {
                                String sql = pullParser.nextText();
                                AppLog.d(TAG, "sql=" + sql);
                                if (upVersion == version && upDbName.equals(dBhelp.getDatabaseName())) {
                                    AppLog.d(TAG, "数据库更新操作sql");
                                    database.execSQL(sql);
                                }
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if ("update_db".equals(nodeName)) {
                            }
                            break;
                        default:
                    }
                    event = pullParser.next();
                }
            } catch (XmlPullParserException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断数据表是否存在
     *
     * @param tableName
     * @return
     */
    private boolean tableIsExist(String tableName) {
        final String sql = "select * from sqlite_master where type='table';";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            if (cursor.getString(1).equals(tableName)) {
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }

    /**
     * 缓存关系 表字段——属性
     */
    private void initCacheMap() {
        AppLog.d(TAG, "缓存 表字段与bean属性 关系 ");
//        final String sql = "PRAGMA table_info("+tableName+ ")";
        final String sql = "select * from " + tableName + " limit 1,0;";
        Cursor cursor = db.rawQuery(sql, null);
        //字段名数组
        String[] columnNames = cursor.getColumnNames();
        Field[] columnFields = entityClass.getDeclaredFields();
        cacheMap = new HashMap<>((int)(columnNames.length/0.75));
        for (String columnName : columnNames) {
            for (Field columnField : columnFields) {
                // 默认使用属性名时
                String columnFieldName;
                DbFields dbField = columnField.getAnnotation(DbFields.class);
                if (dbField.columnName() == null
                        || dbField.columnName().length() < 1) {
                    columnFieldName = columnField.getName();
                } else {
                    columnFieldName = dbField.columnName();
                }
                if (columnName.equalsIgnoreCase(columnFieldName)) {
                    cacheMap.put(columnName, columnField);
                    AppLog.d(TAG, "表字段——属性", columnName, "--", columnField.toString());
                    break;
                }
                if(dbField.isPrimaryKey()){
                    pkFieldName = columnField.getName();
                    AppLog.d(TAG, "主键属性名=", pkFieldName);
                }
            }
        }
        for (String columnName :cacheMap.keySet()){
            if(cacheMap.get(columnName).getName().equals(pkFieldName)){
                pkName = columnName.toLowerCase();
                AppLog.d(TAG, "主键名=", pkName);
                break;
            }
        }
        cursor.close();
    }

    /**
     * 自动建表
     *
     * @param entity
     */
    private boolean createTable(Class<T> entity) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("create table if not exists ");
        sqlBuffer.append(tableName + " (");
        /**
         * 添加字段
         *
         * 反射获取字段 getDeclaredFields获得某个类的所有声明的字段，
         * 即包括public、private和proteced，但是不包括父类的申明字段
         */
        Field[] fields = entity.getDeclaredFields();
        for (Field field : fields) {
            //数据库中的字段列名
            String columnName = null;
            if (field.getAnnotations().length < 1) {
                continue;
            } else {
                // 类属性的第一行注解是数据库字段注解
                if (field.getAnnotations()[0] instanceof DbFields) {
                    DbFields dbField = (DbFields) field.getAnnotations()[0];
                    // 默认使用属性名
                    if (dbField.columnName() == null
                            || dbField.columnName().length() < 1) {
                        columnName = field.getName().toLowerCase();
                    } else {
                        columnName = dbField.columnName().toLowerCase();
                    }
                    // 添加字段
                    sqlBuffer.append("\n").append(columnName).append(" ");
                    // 字段数据类型
                    Class type = field.getType();
                    if (type == int.class || type == Integer.class || type == long.class || type == Long.class || type == short.class || type == Short.class) {
                        sqlBuffer.append(SqlDataType.INTEGER);
                        // 是否是主键 sqlite的主键 primary key的列值允许你存储64位的整数。
                        // 必须是INTEGER PRIMARY KEY  AUTOINCREMENT
                        if (dbField.isPrimaryKey() && !dbField.isNull()) {
                            sqlBuffer.append(" primary key ").append(dbField.pkAutoincrement() ? " autoincrement " : "");
                            havePrimaryKey = true;
                        }
                    } else if (type == String.class) {
                        sqlBuffer.append(SqlDataType.TEXT);
                    } else if (type == char.class || type == Character.class) {
                        AppLog.e(TAG, "Don't use 'Chat' type");
                    } else if (type == double.class || type == Double.class || type == float.class || type == Float.class) {
                        sqlBuffer.append(SqlDataType.REAL);
                    } else if (type == boolean.class || type == Boolean.class) {
                        sqlBuffer.append(SqlDataType.NUMERIC);
                    } else {
                        sqlBuffer.append(SqlDataType.NONE);
                    }
                    // 是否允许为空
                    if (dbField.isNull()) {
                        sqlBuffer.append(" default (").append(dbField.defaultValue()).append(") ");
                    } else {
                        sqlBuffer.append(" not null").append(" ");
                    }
                    //是否唯一
                    if (dbField.isUnique()) {
                        sqlBuffer.append(" unique");
                    }
                    //该字段是否关联外键
                    if (dbField.isRelevantFk()) {
                        relevant_fk = columnName;
                    }
                    sqlBuffer.append(",");
                } else {
                    int len = field.getAnnotations().length;
                    for (int i = 1; i < len; i++) {
                        if (field.getAnnotations()[i] instanceof DbFields) {
                            throw new DbException(ATTRIBUTE_ANNOTATION_NOT_FIRST);
                        }
                    }
                    AppLog.d(TAG, "不存入数据库的字段");
                }
            }
        }
        sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
        //拥有外键
        String parentTableName = entityClass.getAnnotation(DbTable.class).parentTableName().toLowerCase();

        if (parentTableName.length() > 0) {
            String fkName = entityClass.getAnnotation(DbTable.class).foreignKey().toLowerCase();

            sqlBuffer.append(" ,constraint ").append("fk_").append(tableName).append("_").append(relevant_fk)
                    .append(" foreign key (").append(relevant_fk).append(") references ").append(parentTableName).append("(").append(fkName).append(")");
        }
        sqlBuffer.append(" )");
        String createTableSql = sqlBuffer.toString().toLowerCase();
        AppLog.d(TAG, entity.getSimpleName() + "_createTableSql=" + createTableSql);
        AppLog.d(TAG, "创建表" + tableName);
        if (!havePrimaryKey) {
            AppLog.e(TAG, "创建表失败,数据表无主键或主键为NULL");
            throw new DbException(NO_PRIMARY_KEY);
        }
        try {
            db.execSQL(createTableSql);
        } catch (Exception e) {
            e.printStackTrace();
            AppLog.e(TAG, "创建表" + tableName + "失败");
            return false;
        }
        return true;
    }

    @Override
    public long insert(T t) {
        ContentValues contentValues = getValues(t);
        AppLog.d(TAG,"=="+contentValues.toString());
        //重复插入SQLiteConstraintException: UNIQUE constraint failed (code 1555)
        return db.insert(tableName, null, contentValues);
    }


    @Override
    public long insert(List<T> tList) {
        //手动设置开始事务
        int count = 0;
        db.beginTransaction();
        try {
            for (T t : tList) {
                insert(t);
            }
            //设置事务处理成功，不设置会自动回滚不提交
            db.setTransactionSuccessful();
            count = tList.size();
        } catch (Exception e) {
            AppLog.e(TAG, e);
            count = -1;
        } finally {
            //处理完成
            db.endTransaction();
            return count;
        }
    }

    /**
     * 删
     *
     * @param pk
     * @return 更新了多少行记录
     */
    @Override
    public long delete(int pk) {
        int rowcount = db.delete(tableName, pkName + "=?", new String[]{String.valueOf(pk)});
        AppLog.d(TAG, pk + "删除数据rowcount=" + rowcount);
        return rowcount;
    }

    @Override
    public long delete(Collection<Integer> pkList) {
        int rowcount = 0;
        for (Integer pk : pkList) {
            rowcount = db.delete(tableName, pkName + "=?", new String[]{String.valueOf(pk)});
            AppLog.d(TAG, pk + "删除数据rowcount=" + rowcount);
        }
        return rowcount;
    }

    @Override
    public long deleteAllData() {
        //删除整表数据
        return db.delete(tableName, null, null);
    }

    /**
     * 改
     *
     * @param t
     * @return 更新了多少行记录
     */
    @Override
    public long update(T t) {
        ContentValues values = getValues(t);
        int rowcount = db.update(tableName, values, null, null);
        AppLog.d(TAG, "update rowcount=" + rowcount);
        return rowcount;
    }

    @Override
    public long updatePK(T t, int newPk) {
        ContentValues values = getValues(t);
        int oldPk = values.getAsInteger(pkName);
        values.put(pkName, newPk);
        int rowcount = db.update(tableName, values, pkName + "=?", new String[]{String.valueOf(oldPk)});
        AppLog.d(TAG, "update rowcount=" + rowcount);
        return rowcount;
    }

    /**
     * 查
     *
     * @return
     */
    @Override
    public List<T> select() {
        List<T> list = new ArrayList<>();
        //第二个参数表示查询的列名，第三个参数表示where条件，第四个参数表示注入的where条件占位符的值，第五个参数表示gourpby列，第六个参数表示having条件，第七个参数表示orderby的列
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            try {
                T obj = entityClass.newInstance();
                String[] ColumnNames = cursor.getColumnNames();
                for (String columnName : ColumnNames) {
                    for (String fieldNameKey : cacheMap.keySet()) {
                        if (columnName.equalsIgnoreCase(fieldNameKey)) {
                            //AppLog.d(TAG,"属性名="+cacheMap.get(fieldNameKey).getName());
                            Field field = cacheMap.get(fieldNameKey);
                            field.setAccessible(true);
                            int columnIndex = cursor.getColumnIndex(columnName);
                            Class type = field.getType();
                            if (type == int.class || type == Integer.class) {
                                field.set(obj, cursor.getInt(columnIndex));
                            } else if (type == short.class || type == Short.class) {
                                field.set(obj, cursor.getShort(columnIndex));
                            } else if (type == long.class || type == Long.class) {
                                field.set(obj, cursor.getLong(columnIndex));
                            } else if (type == String.class) {
                                field.set(obj, cursor.getString(columnIndex));
                            } else if (type == char.class || type == Character.class) {
                                //AppLog.w(TAG, "chat 是一个鬼");
                            } else if (type == float.class || type == Float.class) {
                                field.set(obj, cursor.getFloat(columnIndex));
                            } else if (type == double.class || type == Double.class) {
                                field.set(obj, cursor.getDouble(columnIndex));
                            } else if (type == byte.class || type == Byte.class) {
                                field.set(obj, (Byte.valueOf(cursor.getString(columnIndex))));
                            } else if (type == byte[].class || type == Byte[].class) {
                                field.set(obj, cursor.getBlob(columnIndex));
                            } else if (type == boolean.class || type == Boolean.class) {
                                field.set(obj, (Boolean.valueOf(cursor.getString(columnIndex))));
                            }
                            break;
                        }
                    }
                }
                list.add(obj);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        return list;
    }

    /**
     * @param t
     * @return
     */
    private ContentValues getValues(T t) {
        ContentValues contentValues = new ContentValues();
        Iterator<Map.Entry<String, Field>> iterator = cacheMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Field> fieldEntry = iterator.next();
            Field field = fieldEntry.getValue();
            String key = fieldEntry.getKey();
            field.setAccessible(true);
            try {
                Object obj = field.get(t);
                Class type = field.getType();
                String defaultValue = ((DbFields) field.getAnnotations()[0]).defaultValue();
//                AppLog.d(TAG,"key——"+key,"属性——"+obj,"type="+type,"defaultValue="+defaultValue);
                if (type == int.class || type == Integer.class) {
                    contentValues.put(key, (Integer) obj);
                } else if (type == short.class || type == Short.class) {
                    contentValues.put(key, (Short) obj);
                } else if (type == long.class || type == Long.class) {
                    contentValues.put(key, (Long) obj);
                } else if (type == String.class) {
                    contentValues.put(key, (String) obj);
                } else if (type == char.class || type == Character.class) {
                    contentValues.put(key, String.valueOf(obj));
                } else if (type == float.class || type == Float.class) {
                    contentValues.put(key, (Float) obj);
                } else if (type == double.class || type == Double.class) {
                    contentValues.put(key, (Double) obj);
                } else if (type == byte.class || type == Byte.class) {
                    contentValues.put(key, (Byte) obj);
                } else if (type == byte[].class || type == Byte[].class) {
                    contentValues.put(key, (byte[]) obj);
                } else if (type == boolean.class || type == Boolean.class) {
                    contentValues.put(key, (Boolean) obj);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return contentValues;
    }

}
