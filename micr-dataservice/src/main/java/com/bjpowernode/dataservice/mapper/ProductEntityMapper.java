package com.bjpowernode.dataservice.mapper;

import com.bjpowernode.entity.ProductEntity;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ProductEntityMapper {

    /*************查询满标的产品*******************/
    List<ProductEntity> selectManBiao(@Param("beginDate") Date beginDate,
                                      @Param("endDate") Date endDate);

    /*************按主键查询产品*******************/
    ProductEntity selectByPrimaryKey(@Param("id") Integer id);

    /*************计算利率的平均值*******************/
    BigDecimal selectAvgRate();


    /*************按类型分页查询产品*******************/
    List<ProductEntity> selectByProductTypePage(@Param("type") Integer productType,
                                                @Param("offSet") Integer offSet,
                                                @Param("rows") Integer pageSize);

    /**
     * @param productType 产品类型
     * @return 总记录数量
     */
    Integer selectCountByProductType(@Param("ptype") Integer productType);

    /*************产品剩余可投资金额**********************/
    int updateLeftMoneyByInvest(@Param("id") Integer productId, @Param("bidMoney") BigDecimal bidMoney);

    /*************更新产品为满标状态**********************/
    int updateProductStatus(@Param("id") Integer productId, @Param("pstatus") Integer productStatus);

    /*************更新产品的状态product_status**********************/
    int updateStatus(@Param("id") Integer productId, @Param("pstatus") Integer productStatus);


}