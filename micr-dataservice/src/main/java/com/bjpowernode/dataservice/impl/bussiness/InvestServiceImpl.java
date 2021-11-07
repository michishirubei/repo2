package com.bjpowernode.dataservice.impl.bussiness;

import com.bjpowernode.consts.YLBConsts;
import com.bjpowernode.dataservice.mapper.AccountEntityMapper;
import com.bjpowernode.dataservice.mapper.BidEntityMapper;
import com.bjpowernode.dataservice.mapper.ProductEntityMapper;
import com.bjpowernode.entity.AccountEntity;
import com.bjpowernode.entity.BidEntity;
import com.bjpowernode.entity.ProductEntity;
import com.bjpowernode.model.BidInfo;
import com.bjpowernode.model.InvestResult;
import com.bjpowernode.service.bussiness.InvestService;
import com.bjpowernode.util.DecimalUtil;
import com.bjpowernode.util.YLBUtil;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@DubboService(interfaceClass = InvestService.class,version = "1.0")
public class InvestServiceImpl implements InvestService {

    @Resource
    private BidEntityMapper bidMapper;

    @Resource
    private AccountEntityMapper accountMapper;

    @Resource
    private ProductEntityMapper productMapper;

    /***********累计的投资金额和*****************/
    @Override
    public BigDecimal querySumInvestMoney() {
        BigDecimal sumBidMoney = bidMapper.selectSumBidMoney();
        return sumBidMoney;
    }

    /***********某个产品的最近3条投资记录*****************/
    @Override
    public List<BidInfo> queryBidListByProudctId(Integer productId) {
        List<BidInfo> bidInfoList = bidMapper.selectBidsByProductId(productId);
        return bidInfoList;
    }

    /**
     * 查询某个用户的投资记录列表
     * @param userId 用户id
     * @return  投资列表
     */
    @Override
    public List<BidInfo> queryBidListByUserId(Integer userId,Integer pageNo,Integer pageSize) {
        List<BidInfo> bidList  = new ArrayList<>();
        if( userId != null && userId > 0 ){

            pageNo = YLBUtil.pageNo(pageNo);
            pageSize = YLBUtil.pageSize(pageSize);
            bidList = bidMapper.selectBidsByUserId(userId,YLBUtil.offSet(pageNo,pageSize),pageSize);
        }
        return bidList;
    }

    /**
     * 查询某个用户的投资记录总数
     * @param userId 用户id
     * @return 此用户投资记录总数
     */
    @Override
    public Integer staticsBidRecordNumsByUserId(Integer userId) {
        int totalRecords = 0;
        if( userId != null && userId > 0 ){
            totalRecords  = bidMapper.selectBidsRecordNums(userId);
        }
        return totalRecords;
    }

    /**
     * @param userId        用户id
     * @param productId     产品id
     * @param bidMoney      投资金额
     * @return InvestResult
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public InvestResult invest(Integer userId, Integer productId, BigDecimal bidMoney) {
        int rows = 0;
        InvestResult result  = new InvestResult();
        //1.查询资金
        AccountEntity account = accountMapper.selectForUpdate(userId);
        if( account != null){
            if(DecimalUtil.ge( account.getAvailableMoney(),bidMoney) ){

                ProductEntity product = productMapper.selectByPrimaryKey(productId);
                if( product != null){
                    //产品的 金额处理 min， max ，产品leftMoney ， 产品的status
                    if( product.getProductStatus() == YLBConsts.PRODUCT_STATUS_NONE_MANBIAO ){
                        //产品可以投资
                        if( DecimalUtil.ge(bidMoney,product.getBidMinLimit())
                          && DecimalUtil.ge(product.getBidMaxLimit(),bidMoney)
                          && DecimalUtil.ge(product.getLeftProductMoney(),bidMoney) ) {
                            //可以投资 1. 扣除account资金    update account 资金
                            rows =  accountMapper.updateAvailableMoneyByInvest(userId,bidMoney);
                            if( rows < 1 ){
                                throw new RuntimeException("用户投资，更新资金余额失败");
                            }

                            //产品剩余可投资金额  update leftMoney
                            rows = productMapper.updateLeftMoneyByInvest(productId,bidMoney);
                            if(rows < 1){
                                throw new RuntimeException("用户投资，更新产品的剩余可投资金额失败");
                            }

                            //生成投资记录 insert bid_info
                            BidEntity bid  = new BidEntity();
                            bid.setBidMoney(bidMoney);
                            bid.setBidStatus(YLBConsts.BID_STATUS_SUCCESS);
                            bid.setBidTime(new Date());
                            bid.setProdId(productId);
                            bid.setUid(userId);
                            bidMapper.insert(bid);

                            //判断产品状态， 更新满标
                            ProductEntity queryProduct = productMapper.selectByPrimaryKey(productId);
                            if( queryProduct.getLeftProductMoney().compareTo( new BigDecimal("0")) == 0){
                                //更新产品是满标
                               rows = productMapper.updateProductStatus(productId,YLBConsts.PRODUCT_STATUS_MANBIAO);
                               if( rows < 1 ){
                                   throw new RuntimeException("用户投资，更新产品满标状态失败");
                               }
                            }
                            //投资成功
                            result.setCode(20000);// 投资成
                            result.setReason( "投资成功");
                            result.setResult(true);

                        } else {
                            result.setCode(20005);//
                            result.setReason( "投资金额不符合要求，可投资范围("+
                                     product.getBidMinLimit()+"-"+product.getBidMaxLimit()+")");
                            result.setResult(false);
                        }
                    } else {
                        result.setCode(20004);// 产品不存在
                        result.setReason( productId+",不可投资");
                        result.setResult(false);
                    }

                } else {
                    result.setCode(20003);// 产品不存在
                    result.setReason( productId+",产品不存在");
                    result.setResult(false);
                }
            } else {
                // 没有资金账户
                result.setCode(20002);//资金不足
                result.setReason(userId+",资金不足");
                result.setResult(false);
            }
        } else {
            // 没有资金账户
            result.setCode(20001);//没有资金账户
            result.setReason(userId+",没有资金账户");
            result.setResult(false);
        }
        return result;
    }
}
