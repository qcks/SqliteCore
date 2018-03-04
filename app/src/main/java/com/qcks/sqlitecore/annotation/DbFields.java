package com.qcks.sqlitecore.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述字段
 * @author qckiss
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DbFields {
	/**
	 * 列名
	 * @return
	 */
	String columnName() default "";
	
	/**
	 * 是否是主键
	 * @return
	 */
	boolean isPrimaryKey() default false;
	/**
	 * 主键自动增量
	 * @return
	 */
	boolean pkAutoincrement() default false;

	/**
	 * 是否允许为空
	 * @return
	 */
	boolean isNull() default true;

	/**
	 * 是否唯一
	 * @return
	 */
	boolean isUnique() default false;

	/**
	 * 默认值
	 * @return
	 */
	String defaultValue() default "NULL";

	/**
	 * 是否关联外键
	 * @return
	 */
	boolean isRelevantFk() default false;
}
