package com.project.p2p.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.project.p2p.mapper.*;
import com.project.p2p.pojo.BidInfo;
import com.project.p2p.pojo.IncomeRecord;
import com.project.p2p.pojo.LoanInfo;
import com.project.p2p.service.IncomeService;
import com.project.p2p.service.LoanInfoService;
import com.project.p2p.utils.DateTimeUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
@Service(interfaceClass = IncomeService.class, version="1.0.0", retries = 2)
@Component
@Transactional
public class IncomeServiceImpl implements IncomeService {

    @Autowired
    private IncomeRecordMapper incomeRecordMapper;
    @Autowired
    private LoanInfoMapper loanInfoMapper;
    @Autowired
    private BidInfoMapper bidInfoMapper;
    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    /**
     * 根据用户id分页查询收入信息
     */
    @Override
    public List<IncomeRecord> queryListByUid(Integer uid, Integer start, Integer count) {
        return incomeRecordMapper.selectListByUid(uid, start, count);
    }
    /**
     * 根据用户id查询收入数
     */
    @Override
    public Integer queryCountByUid(Integer uid) {
        return incomeRecordMapper.selectCountByUid(uid);
    }

    /**
     * 创建收益计划
     */
    @Override
    public void createIncome() {
        //获取全部状态为1（已满标）的产品
        List<LoanInfo> loanInfos = loanInfoMapper.selectListByStatus(1);
        //根据产品id读取全部投资记录
        List<Integer> loanIds = new ArrayList<>();
        for(LoanInfo loanInfo : loanInfos){
            loanIds.add(loanInfo.getId());
        }
        if(loanIds.size() == 0){
            return;
        }
        List<BidInfo> bidInfos = bidInfoMapper.selectListByLoanIds(loanIds);
        System.out.println(bidInfos);
        //根据投资记录生成收益记录，设置状态为0(未返)
        for(BidInfo bidInfo : bidInfos){
            IncomeRecord incomeRecord = uploadIncomeRecord(bidInfo);
            incomeRecordMapper.insertSelective(incomeRecord);
        }
        //更改产品状态为2(已满标并获取收益)
        loanInfoMapper.updateStatusByPrimaryKeys(loanIds, 2);
    }

    /**
     * 结算收益
     */
    @Override
    public void settleIncome() {
        //查询所有状态为0(未返)的收入记录
        List<IncomeRecord> incomeRecords = incomeRecordMapper.selectListByStatus(0);
        for(IncomeRecord incomeRecord : incomeRecords){
            if(DateUtils.isSameDay(new Date(), incomeRecord.getIncomeDate())) {
                //生成收益并修改对应用户余额
                financeAccountMapper.updateFinanceByUid(incomeRecord.getUid(), incomeRecord.getBidMoney() + incomeRecord.getIncomeMoney());
                //修改收入记录状态为1(已返)
                incomeRecord.setIncomeStatus(1);
                incomeRecordMapper.updateByPrimaryKeySelective(incomeRecord);
            }
        }


    }

    /**
     * 添加收入记录信息
     * @param bidInfo
     * @return
     */
    public IncomeRecord uploadIncomeRecord(BidInfo bidInfo) {
        IncomeRecord incomeRecord = new IncomeRecord();
        incomeRecord.setBidId(bidInfo.getId());
        incomeRecord.setBidMoney(bidInfo.getBidMoney());
        incomeRecord.setIncomeStatus(0);
        incomeRecord.setLoanId(bidInfo.getLoanId());
        incomeRecord.setUid(bidInfo.getUid());
        Integer cycle = bidInfo.getLoanInfo().getCycle();
        int days = bidInfo.getLoanInfo().getProductType() == 0 ? cycle : cycle * 30;
        Date nowDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        incomeRecord.setIncomeDate(calendar.getTime());
        Double incomeMoney = Math.round(bidInfo.getLoanInfo().getRate() * bidInfo.getBidMoney() * days / 365) / 100d;
        incomeRecord.setIncomeMoney(incomeMoney);
        return incomeRecord;
    }

    @Override
    public Object add(Object o) {
        return null;
    }

    @Override
    public Object remove(Integer id) {
        return null;
    }

    @Override
    public Object modify(Object o) {
        return null;
    }

    @Override
    public Object queryOne(Integer id) {
        return null;
    }

    @Override
    public List quertAll() {
        return null;
    }
}
