package com.qcks.sqlitecore.bean;

/**
 * SqlDataType
 *
 * @author qckiss
 * @date 2018-1-1
 */

public class SqlDataType {
    /**
     *INT
     INTEGER
     TINYINT
     SMALLINT
     MEDIUMINT
     BIGINT
     UNSIGNED BIG INT
     INT2
     INT8
     */
    public static final String INTEGER = "INTEGER";
    /**
     *CHARACTER(20)
     VARCHAR(255)
     VARYING CHARACTER(255)
     NCHAR(55)
     NATIVE CHARACTER(70)
     NVARCHAR(100)
     TEXT
     CLOB
     */
    public static final String TEXT = "TEXT";
    /**
     *REAL
     DOUBLE
     DOUBLE PRECISION
     FLOAT
     */
    public static final String REAL = "REAL";
    /**
     *NUMERIC
     DECIMAL(10,5)
     BOOLEAN
     DATE
     DATETIME
     */
    public static final String NUMERIC = "NUMERIC";
    /**
     *BLOB
     no datatype specified
     */
    public static final String NONE = "NONE";
}
