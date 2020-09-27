package com.project.p2p.service;

import com.project.p2p.utils.Page;

public interface IncomeServiceCustomer {
    /**
     * 根据用户id分页查询收入信息
     * @param uid 用户id
     * @param page 分页信息
     * @return 分页结果
     */
    Page queryListByUid(Integer uid, Page page);
}
