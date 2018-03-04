package com.qcks.sqlitecore.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DbTable {

    /**
     * 所属库名(xxx.db)
     *
     * @return
     */
    String dbName() default "";

    /**
     * 数据库表名
     *
     * @return
     */
    String tableName() default "className";

    /**
     * 父表名（如果要设置外键）
     *
     * @return
     */
    String parentTableName() default "";

    /**
     * 外键
     *
     * @return
     */
    String foreignKey() default "";

}
