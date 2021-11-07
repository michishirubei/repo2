package com.bjpowernode.service.user;

import com.bjpowernode.entity.UserEntity;

public interface UserService {

    /**************注册用户数量************/
    Integer queryRegisterUsers();

    /**************通过手机号查询用户************/
    UserEntity queryUserByPhone(String phone);

    /**************通过手机号注册用户************/
    UserEntity userPhoneRegist(String phone, String password);

    /**************更新实名认证信息************/
    UserEntity modifyUser(String phone, String name, String card);

    /**************用户登录************/
    UserEntity userLogin(String phone, String password);
}
