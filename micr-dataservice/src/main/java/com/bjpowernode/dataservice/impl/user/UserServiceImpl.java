package com.bjpowernode.dataservice.impl.user;

import com.bjpowernode.consts.YLBKey;
import com.bjpowernode.dataservice.mapper.AccountEntityMapper;
import com.bjpowernode.dataservice.mapper.UserEntityMapper;
import com.bjpowernode.entity.AccountEntity;
import com.bjpowernode.entity.UserEntity;
import com.bjpowernode.service.user.UserService;
import com.bjpowernode.util.YLBUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

//暴露服务
@DubboService(interfaceClass = UserService.class,version = "1.0")
public class UserServiceImpl implements UserService {

    @Resource
    private UserEntityMapper userMapper;

    @Resource
    private AccountEntityMapper accountMapper;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * @return 注册用户数量
     * redis存储数据
     * 使用redis数据的步骤
     * 1.先从redis获取数据，
     * 2.如果redis中有数据，直接返回给请求方
     * 3.如果redis中没有数据，调用持久才层，访问数据库
     * 4.查询到的数据，保存到redis，设置30分钟过期。
     * 5.返回给请求方
     *
     */
    @Override
    public  Integer queryRegisterUsers() {
        //1.从redis获取数据
        ValueOperations operations = redisTemplate.opsForValue();
        Integer registerUsers = (Integer) operations.get(YLBKey.USER_REGIST_COUNTS);

        if( registerUsers == null ){
            synchronized (this){
                registerUsers = (Integer) operations.get(YLBKey.USER_REGIST_COUNTS);
                if( registerUsers == null){
                    //3. redis中没有数据，查询数据库
                    registerUsers = userMapper.selectRegisterUsers();
                    //4. 查询到的数据，保存到redis，设置30分钟过期。
                    operations.set(YLBKey.USER_REGIST_COUNTS,registerUsers,30, TimeUnit.MINUTES);
                }

            }
        }
        //2 如果redis中有数据，直接返回给请求方
        return registerUsers;
    }

    /**
     * 通过手机号查询用户
     * @param phone 手机号
     * @return
     */
    @Override
    public UserEntity queryUserByPhone(String phone) {

        UserEntity userEntity = null;
        if( YLBUtil.checkPhone(phone) ){
            //查询数据库
            userEntity = userMapper.selectByPhone(phone);
        }
        return userEntity;
    }

    /**
     * 通过手机号注册用户
     * @param phone     手机号
     * @param password  密码（md5）
     * @return 注册的用户
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserEntity userPhoneRegist(String phone, String password) {

        UserEntity user  = null;
        if( YLBUtil.checkPhone(phone)){
            //1.查询手机号是否注册过
            user = userMapper.selectByPhone(phone);
            if( user == null){ //手机号没有注册过
                //添加数据到u_user
                UserEntity inputUser = new UserEntity();
                inputUser.setPhone(phone);
                //密码需要二次加密(加盐)
                password = DigestUtils.md5Hex(password+phone);
                inputUser.setLoginPassword(password);
                inputUser.setAddTime(new Date());
                userMapper.insertReturnId(inputUser);

                //添加到account表
                AccountEntity account = new AccountEntity();
                account.setUid(inputUser.getId());  //u_user主键值
                account.setAvailableMoney(new BigDecimal("888"));
                accountMapper.insertSelective(account);
                return inputUser;
            }
        }
        return null;
    }

    /**
     * 更新实名认证信息
     * @param phone 手机号
     * @param name  姓名
     * @param card  身份证号
     * @return  更新后的User
     */
    @Override
    public UserEntity modifyUser(String phone, String name, String card) {

        UserEntity user = null;
        if(!StringUtils.isAnyBlank(phone,name,card)){
            int rows  = userMapper.updateByPhone(phone,name,card);
            if( rows > 0 ){
                user  = userMapper.selectByPhone(phone);
            }
        }

        return user;
    }

    /**
     * 用户登录
     * @param phone     手机号
     * @param password  密码（md5）
     * @return
     */
    @Override
    public UserEntity userLogin(String phone, String password) {
        UserEntity userEntity = null;
        if( StringUtils.isAnyBlank(phone,password) || !YLBUtil.checkPhone(phone) || password.length() !=32){
            return userEntity;
        }
        //数据正确 , 密码是加盐(phone)，再做md5
        password = DigestUtils.md5Hex(password + phone);
        /*UserEntity dbUser = userMapper.selectByPhone(phone);
        if( dbUser.getLoginPassword().equalsIgnoreCase(password)){
            //登录成
        }*/
        userEntity = userMapper.selectLogin(phone,password);
        //登录成功，更新登录的时间
        if( userEntity != null){
            UserEntity updateUser = new UserEntity();
            updateUser.setId(userEntity.getId());
            updateUser.setLastLoginTime(new Date());
            //update set last_login_time = xxx where id=yyyy
            userMapper.updateByPrimaryKeySelective(updateUser);
        }
        return userEntity;
    }
}
