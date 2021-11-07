package com.bjpowernode.mapper;

import com.bjpowernode.entity.AccountEntity;

public interface AccountEntityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AccountEntity record);

    int insertSelective(AccountEntity record);

    AccountEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AccountEntity record);

    int updateByPrimaryKey(AccountEntity record);
}