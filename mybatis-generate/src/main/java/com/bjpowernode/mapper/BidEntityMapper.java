package com.bjpowernode.mapper;

import com.bjpowernode.entity.BidEntity;

public interface BidEntityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BidEntity record);

    int insertSelective(BidEntity record);

    BidEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidEntity record);

    int updateByPrimaryKey(BidEntity record);
}