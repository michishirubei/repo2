package com.bjpowernode.dataservice.impl.bussiness;

import com.bjpowernode.consts.YLBConsts;
import com.bjpowernode.dataservice.mapper.AccountEntityMapper;
import com.bjpowernode.dataservice.mapper.RechargeEntityMapper;
import com.bjpowernode.entity.RechargeEntity;
import com.bjpowernode.model.RechargeResult;
import com.bjpowernode.service.bussiness.RechargeService;
import com.bjpowernode.util.YLBUtil;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@DubboService(interfaceClass = RechargeService.class,version = "1.0")
public class RechargeServiceImpl implements RechargeService {

    @Resource
    private RechargeEntityMapper rechargMapper;

    @Resource
    private AccountEntityMapper accountMapper;

    @Override
    public List<RechargeEntity> queryRechageListByUserId(Integer userId,
                                                         Integer pageNo,
                                                         Integer pageSize) {
        List<RechargeEntity> rechargeList  = new ArrayList<>();
        if( userId != null && userId > 0 ){
            pageNo = YLBUtil.pageNo(pageNo);
            pageSize = YLBUtil.pageSize(pageSize);
            rechargeList = rechargMapper.selectRechargeListByUserId(userId, YLBUtil.offSet(pageNo,pageSize),pageSize);
        }
        return rechargeList;
    }

    @Override
    public Integer queryRechargeRecordNumByUserId(Integer userId) {
        int totalRecordNums  = 0;
        if( userId != null && userId > 0 ){
            totalRecordNums = rechargMapper.selectRechargeRecordNum(userId);
        }
        return totalRecordNums;
    }

    /***********创建充值记录***************/
    @Override
    public boolean addRecharge(RechargeEntity entity) {
        boolean add = false;
        if( entity != null){
            add = rechargMapper.insert(entity) > 0 ;
        }
        return add;
    }


    /**
     *  完成快钱异步通知业务
     * @param orderId     商家订单号
     * @param payResult   充值结果 10：成功， 11失败
     * @param payAmount   充值金额(分)
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized RechargeResult doWithKqNotifyService(String orderId, String payResult, String payAmount) {
        RechargeResult result  = new RechargeResult();
        result.setResult(false);

        //1.查询订单是否为商家的
        int rows  = 0;
        RechargeEntity recharge = rechargMapper.selectByRechargeNo(orderId);
        if( recharge != null ){
            //有充值记录, 判断充值的状态
            if( recharge.getRechargeStatus()  == YLBConsts.RECHAGE_STATUS_PROCESSING){
                //可以继续处理， 判断充值金额和快钱的金额是否一致
                String fen  = recharge.getRechargeMoney().multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString();
                if( fen.equalsIgnoreCase(payAmount)){
                    //金额正确，判断支付结果

                    if("10".equals(payResult)){

                        //充值成功，可以给用户增加充值金额
                        rows  = accountMapper.updateAvailableMoneyByRecharge(recharge.getUid(),recharge.getRechargeMoney());
                        if(rows < 1 ){
                            throw new RuntimeException( "userId="+recharge.getUid()+",快钱异步通知，更新账户资金失败");
                        }

                        //更新充值记录的状态为 1
                        rows  = rechargMapper.updateStatusByPrimaryKey(recharge.getId(),YLBConsts.RECHAGE_STATUS_SUCCESS);
                        if( rows <1){
                            throw new RuntimeException( "userId="+recharge.getUid()+",快钱异步通知，更新充值记录状态为1失败");
                        }

                        result.setResult(true);
                        result.setCode(30000);
                        result.setReason("充值结果成功");

                    } else {
                        //充值失败
                        rows  = rechargMapper.updateStatusByPrimaryKey(recharge.getId(),YLBConsts.RECHAGE_STATUS_FAILURE);
                        if( rows <1){
                            throw new RuntimeException( "userId="+recharge.getUid()+",快钱异步通知，更新充值记录状态为2失败");
                        }
                        result.setResult(true);
                        result.setCode(30001);
                        result.setReason("充值结果失败");
                    }

                } else {
                    result.setCode(30004);
                    result.setReason(orderId+"，订单金额和实际支付金额不一致");
                }
            } else {
                //记录已经处理过了，不能在处理
                result.setCode(30003);
                result.setReason(orderId+"，记录已经处理过了");
            }
        } else {
            //记录日志，或者数据库
            result.setCode(30002);
            result.setReason(orderId+"，记录在商家不存在");
        }

        return result;
    }

    /**
     * 根据充值订单号，查询充值记录
     * @param orderId 充值订单号
     * @return
     */
    @Override
    public RechargeEntity queryRechargeByRechargeNo(String orderId) {
        RechargeEntity recharge  = null;
        if( orderId != null){
            recharge  = rechargMapper.selectByRechargeNo(orderId);
        }
        return recharge;
    }

    /***********状态是充值中的记录***************/
    @Override
    public List<RechargeEntity> queryProcessingStatusList() {
        List<RechargeEntity> rechargeList  = rechargMapper.selectInProcessing();
        return rechargeList;
    }
}
