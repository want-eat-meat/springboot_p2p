package com.project.p2p.mapper;

import com.project.p2p.pojo.IncomeRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IncomeRecord record);

    int insertSelective(IncomeRecord record);

    IncomeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IncomeRecord record);

    int updateByPrimaryKey(IncomeRecord record);

    /**
     * 根据用户id分页查询收入信息
     * @param uid 用户id
     * @param start 开始下标
     * @param count 分页结果
     * @return 分页结果
     */
    List<IncomeRecord> selectListByUid(Integer uid, Integer start, Integer count);

    /**
     * 根据用户id查询收入数
     * @param uid
     * @return
     */
    Integer selectCountByUid(Integer uid);

    /**
     * 根据收入状态的查询收入记录集合
     * @param status 状态
     * @return 收入记录集合
     */
    List<IncomeRecord> selectListByStatus(Integer status);
}