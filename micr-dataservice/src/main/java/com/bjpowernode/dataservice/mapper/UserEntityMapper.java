package com.bjpowernode.dataservice.mapper;

import com.bjpowernode.entity.UserEntity;
import org.apache.ibatis.annotations.Param;

public interface UserEntityMapper {

    /***********统计注册用户总数*********************/
    int selectRegisterUsers();

    /***********通过手机号查询记录*********************/
    UserEntity selectByPhone(@Param("phone") String phone);

    /***********添加用户记录后，获取主键id值*********************/
    int insertReturnId(UserEntity user);

    /***********实名认证，更新用户信息********************/
    int updateByPhone(@Param("phone") String phone,
                      @Param("name") String name,
                      @Param("card") String card);


    /***********登录操作，手机号和密码作为条件********************/
    UserEntity selectLogin(@Param("phone") String phone, @Param("mima") String password);

    int deleteByPrimaryKey(Integer id);

    int insert(UserEntity record);

    int insertSelective(UserEntity record);

    UserEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserEntity record);

    int updateByPrimaryKey(UserEntity record);



}