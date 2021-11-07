package com.bjpowernode.mapper;

import com.bjpowernode.entity.IncomeEntity;

public interface IncomeEntityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IncomeEntity record);

    int insertSelective(IncomeEntity record);

    IncomeEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IncomeEntity record);

    int updateByPrimaryKey(IncomeEntity record);
}