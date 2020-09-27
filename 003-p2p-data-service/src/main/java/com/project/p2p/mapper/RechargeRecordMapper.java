package com.project.p2p.mapper;

import com.project.p2p.pojo.RechargeRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RechargeRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RechargeRecord record);

    int insertSelective(RechargeRecord record);

    RechargeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RechargeRecord record);

    int updateByPrimaryKey(RechargeRecord record);

    /**
     * 根据用户id分页查询充值记录
     * @param uid 用户id
     * @param start 开始下标
     * @param count 每页个数
     * @return 分页结果
     */
    List<RechargeRecord> selectListByUid(Integer uid, Integer start, Integer count);

    /**
     * 根据用户id查询充值数量
     * @param uid
     * @return
     */
    Integer selectCountAllByUid(Integer uid);

    /**
     * 根据订单号获取订单信息
     * @param rechargeNo 订单号
     * @return 订单信息
     */
    RechargeRecord queryByRechargeNo(String rechargeNo);

    /**
     * 根据支付状态查询订单集合
     * @param status 订单状态
     */
    List<RechargeRecord> selectListByStatus(String status);
}