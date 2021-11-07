package com.bjpowernode.mapper;

import com.bjpowernode.entity.ProductEntity;

public interface ProductEntityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProductEntity record);

    int insertSelective(ProductEntity record);

    ProductEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProductEntity record);

    int updateByPrimaryKey(ProductEntity record);
}