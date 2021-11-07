package com.bjpowernode.dataservice.impl.bussiness;

import com.bjpowernode.consts.YLBConsts;
import com.bjpowernode.dataservice.mapper.AccountEntityMapper;
import com.bjpowernode.dataservice.mapper.BidEntityMapper;
import com.bjpowernode.dataservice.mapper.IncomeEntityMapper;
import com.bjpowernode.dataservice.mapper.ProductEntityMapper;
import com.bjpowernode.entity.BidEntity;
import com.bjpowernode.entity.IncomeEntity;
import com.bjpowernode.entity.ProductEntity;
import com.bjpowernode.service.bussiness.IncomeService;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@DubboService(interfaceClass = IncomeService.class,version = "1.0")
public class IncomeServiceImpl implements IncomeService {

    @Resource
    private IncomeEntityMapper incomeMapper;

    @Resource
    private ProductEntityMapper productMapper;

    @Resource
    private BidEntityMapper bidMapper;


    @Resource
    private AccountEntityMapper accountMapper;


    //生成收益计划
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void generateIncomePlan() {
        Date cur = new Date();
        Date beginDate  = DateUtils.truncate(DateUtils.addDays(cur,-1), Calendar.DATE);
        Date endDate = DateUtils.truncate(cur,Calendar.DATE);

        //1.获取满标的产品
        List<ProductEntity> productList = productMapper.selectManBiao(beginDate, endDate);

        //2. 查询每个产品的投资记录
        Date incomeDate = null;
        BigDecimal incomeMoney = new BigDecimal("0");

        Date fullDate = null;
        Integer productType = null;//产品类型
        Integer cycle = null;//产品周期
        BigDecimal dateRate = null;//日利率


        for(ProductEntity product : productList){

           cycle = product.getCycle();
           fullDate = product.getProductFullTime();
           productType = product.getProductType();
           dateRate = product.getRate()
                         .divide( new BigDecimal("100"), 2, RoundingMode.HALF_UP )
                         .divide( new BigDecimal("360"),10,RoundingMode.HALF_UP);


            //使用产品id，查询投资记录
           List<BidEntity>  bidList = bidMapper.selectByProductId(product.getId());

           for(BidEntity bid:bidList){
               //计算每个投资记录的，利息和到期时间

               //到期时间 = 满标时间 + 周期（天或者月）
               if(YLBConsts.PRODUCT_TYPE_XINSHOUBAO == productType ){
                   //周期是天
                   incomeDate = DateUtils.addDays(fullDate,(1+ cycle));
                   //利息 = 投资金额 * 周期* 利率
                   incomeMoney = bid.getBidMoney().multiply(dateRate).multiply(new BigDecimal(cycle));
               } else {
                   incomeDate= DateUtils.addDays(fullDate,(1 + (cycle * 30 ) ));
                   incomeMoney = bid.getBidMoney().multiply(dateRate).multiply(new BigDecimal(cycle*30));
               }

               //每个投资记录，都需要生成收益记录
               IncomeEntity incomeRecord = new IncomeEntity();
               incomeRecord.setBidId(bid.getId());
               incomeRecord.setBidMoney(bid.getBidMoney());
               incomeRecord.setIncomeDate(incomeDate);
               incomeRecord.setIncomeMoney(incomeMoney);
               incomeRecord.setIncomeStatus(YLBConsts.INCOME_STATUS_PLAN);
               incomeRecord.setProdId(product.getId());
               incomeRecord.setUid(bid.getUid());
               incomeMapper.insert(incomeRecord);

           }
           //更新产品的满标状态值
           int rows  = productMapper.updateStatus(product.getId(),YLBConsts.PRODUCT_STATUS_PLAN);
           if(rows < 1){
               throw new RuntimeException("生成收益计划，更新产品为生成收益计划失败");
           }
        }
    }

    //收益返还
    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized void generateIncomeBack() {

        //1.获取到期的收益记录
        List<IncomeEntity> incomeList  = incomeMapper.selectExpireIncome();

        int rows  = 0;
        //2.遍历记录
        for(IncomeEntity income:incomeList ){
            //更新资金
            rows = accountMapper.updateAvailableMoneyByIncomeBack(income.getUid(),income.getBidMoney(),income.getIncomeMoney());
            if( rows < 1 ){
                throw new RuntimeException("收益返还，更新用户资金失败");
            }

            //更新收益计划的记录 ， 状态从 0 变为 1
            rows  =  incomeMapper.updateStatus(income.getId(), YLBConsts.INCOME_STATUS_BACK);
            if( rows <1 ){
                throw new RuntimeException("收益返还，更新收益记录状态为1失败");
            }
        }

    }
}
