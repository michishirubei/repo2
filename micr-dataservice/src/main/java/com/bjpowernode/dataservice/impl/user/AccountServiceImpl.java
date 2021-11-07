package com.bjpowernode.dataservice.impl.user;

import com.bjpowernode.dataservice.mapper.AccountEntityMapper;
import com.bjpowernode.entity.AccountEntity;
import com.bjpowernode.service.user.AccountService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

//账户资金的操作
@DubboService(interfaceClass = AccountService.class,version = "1.0")
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountEntityMapper accountMapper;

    /**
     * 查询资金账户，使用uid
     * @param uid uid
     * @return
     */
    @Override
    public AccountEntity queryByUid(Integer uid) {
        if( uid == null) {
            throw new RuntimeException("用户id是null");
        }
        //使用userId查询account账户
        return accountMapper.selectByUid(uid);
    }
}
