package com.bjpowernode.dataservice.impl.bussiness;

import com.bjpowernode.consts.YLBConsts;
import com.bjpowernode.dataservice.mapper.BidEntityMapper;
import com.bjpowernode.dataservice.mapper.ProductEntityMapper;
import com.bjpowernode.dataservice.mapper.UserEntityMapper;
import com.bjpowernode.entity.ProductEntity;
import com.bjpowernode.model.IndexData;
import com.bjpowernode.service.bussiness.ProductService;
import com.bjpowernode.util.YLBUtil;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@DubboService(interfaceClass = ProductService.class,version = "1.0")
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductEntityMapper productEntityMapper;

    @Resource
    private BidEntityMapper bidEntityMapper;

    @Resource
    private UserEntityMapper userMapper;



    /*************首页数据*******************/
    @Override
    public IndexData queryIndexData() {

        int registerUsers = userMapper.selectRegisterUsers();
        BigDecimal sumBidMoney = bidEntityMapper.selectSumBidMoney();
        BigDecimal avgRate = productEntityMapper.selectAvgRate();

        List<ProductEntity> xinList = productEntityMapper
                           .selectByProductTypePage(YLBConsts.PRODUCT_TYPE_XINSHOUBAO,0,1);

        List<ProductEntity> youList = productEntityMapper
                .selectByProductTypePage(YLBConsts.PRODUCT_TYPE_YOUXUAN,0,4);

        List<ProductEntity> sanList = productEntityMapper
                .selectByProductTypePage(YLBConsts.PRODUCT_TYPE_SANBIAO,0,8);

        IndexData indexData = new IndexData(registerUsers,sumBidMoney,avgRate,xinList,youList,sanList);
        return indexData;

    }

    /*************计算利率的平均值*******************/
    @Override
    public BigDecimal queryAvgRate() {

        BigDecimal avgRate  = productEntityMapper.selectAvgRate();
        return avgRate;
    }

    /**
     * 分类型查询产品（分页）
     * @param productType 产品类型
     * @param pageNo      页号（1）
     * @param pageSize    每页大小（9）
     * @return
     */
    @Override
    public List<ProductEntity> queryProductListPage(Integer productType,
                                                    Integer pageNo,
                                                    Integer pageSize) {
        List<ProductEntity> productEntityList = new ArrayList<>();
        //调用数据库的mapper，获取数据
        if(YLBUtil.typeInRange(productType)){
            //类型符合要求
            pageNo = YLBUtil.pageNo(pageNo);
            pageSize = YLBUtil.pageSize(pageSize);

            //按类型查询产品
            productEntityList= productEntityMapper.selectByProductTypePage(
                                productType,YLBUtil.offSet(pageNo,pageSize), pageSize);

        }
        return productEntityList;
    }

    /**
     * @param productType 产品类型
     * @return 此产品类型的总记录数量
     */
    @Override
    public Integer queryTotalRecordByProuductType(Integer productType) {
        Integer totalRecord = productEntityMapper.selectCountByProductType(productType);
        return totalRecord;
    }

    /**
     * @param productId 产品主键id
     * @return 产品对象或者null
     */
    @Override
    public ProductEntity queryProductById(Integer productId) {
        ProductEntity pe = null;
        if( productId != null && productId > 0 ){
            //查询数据库
            pe = productEntityMapper.selectByPrimaryKey(productId);
        }
        return pe;
    }
}
