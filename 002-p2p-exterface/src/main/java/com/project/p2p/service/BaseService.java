package com.project.p2p.service;

import java.util.List;

public interface BaseService<T> {
    /**
     * 增加
     * @param t 对象
     * @return 添加的对象
     */
    T add(T t);

    /**
     * 根据id删除
     * @param id 对象id
     * @return 删除的对象
     */
    T remove(Integer id);

    /**
     * 修改
     * @param t 对象
     * @return 修改过的对象
     */
    T modify(T t);

    /**
     * 查询一个
     * @param id 对象id
     * @return 对象
     */
    T queryOne(Integer id);

    /**
     * 查询全部
     * @return 对象列表
     */
    List<T> quertAll();
}
