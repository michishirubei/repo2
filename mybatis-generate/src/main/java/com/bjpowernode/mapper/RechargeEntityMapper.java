package com.bjpowernode.mapper;

import com.bjpowernode.entity.RechargeEntity;

public interface RechargeEntityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RechargeEntity record);

    int insertSelective(RechargeEntity record);

    RechargeEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RechargeEntity record);

    int updateByPrimaryKey(RechargeEntity record);
}