package com.bjpowernode.service.user;

import com.bjpowernode.entity.AccountEntity;

public interface AccountService {

    //查询资金账户，使用uid
    AccountEntity queryByUid(Integer uid);
}
