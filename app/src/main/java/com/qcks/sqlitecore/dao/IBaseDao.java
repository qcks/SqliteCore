package com.qcks.sqlitecore.dao;

import java.util.Collection;
import java.util.List;

/**
 * IBaseDao
 *
 * @author qckiss
 * @date 2017-12-31
 */

interface IBaseDao<T> {
    /**
     * 增
     * @param t 添加对象
     * @return 是否成功
     */
    long insert(T t);
    long insert(List<T> tList);

    /**
     * 删
     * @param
     * @return
     */
    long delete(int pk);
    long delete(Collection<Integer> pkList);
    long deleteAllData();

    /**
     * 改
     * @param t
     * @return
     */
    long update(T t);
    long updatePK(T t, int newPk);

    /**
     * 查
     * @return 结果集合
     */
    List<T> select();
}
