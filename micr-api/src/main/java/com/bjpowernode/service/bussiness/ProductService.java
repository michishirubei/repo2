package com.bjpowernode.service.bussiness;

import com.bjpowernode.entity.ProductEntity;
import com.bjpowernode.model.IndexData;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {


    /*************首页数据*******************/
    IndexData queryIndexData();



    /*************计算利率的平均值*******************/
    BigDecimal queryAvgRate();


    /**
     * 分类型查询产品（分页）
     * @param productType 产品类型
     * @param pageNo      页号（1）
     * @param pageSize    每页大小（9）
     * @return
     */
    List<ProductEntity> queryProductListPage(Integer productType,Integer pageNo,Integer pageSize);

    /**
     * @param productType 产品类型
     * @return 此产品类型的总记录数量
     */
    Integer queryTotalRecordByProuductType(Integer productType);

    /**
     * @param productId 产品主键id
     * @return 产品对象或者null
     */
    ProductEntity queryProductById(Integer productId);
}
