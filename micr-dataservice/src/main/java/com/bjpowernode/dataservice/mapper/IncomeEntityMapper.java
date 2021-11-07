package com.bjpowernode.dataservice.mapper;

import com.bjpowernode.entity.IncomeEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IncomeEntityMapper {

    /***********获取到期的收益记录*********************/
    List<IncomeEntity> selectExpireIncome();

    /***********更新状态*********************/
    int updateStatus(@Param("id") Integer id, @Param("sta") Integer incomeStatusBack);

    int deleteByPrimaryKey(Integer id);

    int insert(IncomeEntity record);

    int insertSelective(IncomeEntity record);

    IncomeEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IncomeEntity record);

    int updateByPrimaryKey(IncomeEntity record);



}