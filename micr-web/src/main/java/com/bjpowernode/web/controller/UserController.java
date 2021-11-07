package com.bjpowernode.web.controller;

import com.bjpowernode.consts.YLBConsts;
import com.bjpowernode.entity.AccountEntity;
import com.bjpowernode.entity.RechargeEntity;
import com.bjpowernode.entity.UserEntity;
import com.bjpowernode.enums.CodeEnum;
import com.bjpowernode.model.BidInfo;
import com.bjpowernode.util.YLBUtil;
import com.bjpowernode.vo.Result;
import com.bjpowernode.web.service.RealNameService;
import com.bjpowernode.web.service.SmsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Controller
public class UserController extends BaseController {

    @Resource
    private SmsService smsService;

    @Resource
    private RealNameService realNameService;

    /****************进入到注册页面****************/
    @GetMapping("/user/page/regist")
    public String pageRegister(){
        return "register";
    }

    /****************进入到实名认证页面****************/
    @GetMapping("/user/page/realname")
    public String pageRealName(){
        return "realName";
    }

    /****************进入到用户中心页面****************/
    @GetMapping("/user/page/mycenter")
    public String pageUserMyCenter(Model model,HttpSession session){
        UserEntity user = (UserEntity)session.getAttribute(YLBConsts.SESSION_USER);

        //账户金额
        AccountEntity account = accountService.queryByUid(user.getId());
        if( account != null){
            model.addAttribute("accountMoney",account.getAvailableMoney());
        }
        //查询投资记录 5 条
        List<BidInfo> bidInfoList = investService.queryBidListByUserId(user.getId(),1,5);
        model.addAttribute("bidInfoList",bidInfoList);

        //充值充值的5条记录
        List<RechargeEntity> rechargeList = rechargeService.queryRechageListByUserId(user.getId(),1,5);
        model.addAttribute("rechargeList",rechargeList);

        return "myCenter";
    }


    /****************进入登录页面****************/
    @GetMapping("/user/page/login")
    public String pageUserLogin(String returnUrl, HttpServletRequest request){
        if(StringUtils.isBlank(returnUrl)){
            //回到首页
            //http://localhost:8000/ylb/
            returnUrl = request.getScheme() + "://"+ request.getServerName() + ":"
                    + request.getServerPort() + request.getContextPath()+"/index";
        }

        //页面数据
        //调用远程服务，才能获取注册用户数量
        int registerUsers = userService.queryRegisterUsers();
        //累计成交金额
        BigDecimal sumBidMoney = investService.querySumInvestMoney();
        //收益率平均值
        BigDecimal avgRate = productService.queryAvgRate();


        request.setAttribute("registerUsers",registerUsers);
        request.setAttribute("sumBidMoney",sumBidMoney);
        request.setAttribute("avgRate",avgRate);
        request.setAttribute("returnUrl",returnUrl);
        return "login";
    }


    /****************查询手机号是否能注册************/
    @GetMapping("/phone/registed")
    @ResponseBody
    public Result phoneRegisted(String phone){
        //默认是错误的对象
        Result<String> result = Result.fail();
        if(StringUtils.isBlank(phone)){
            //给出错误提示
            result.setCodeEnum(CodeEnum.PHONE_BLANK_ERR);
        } else if(!YLBUtil.checkPhone(phone)) {
            //手机号格式判断
            result.setCodeEnum(CodeEnum.PHONE_FORMAT_ERR);
        } else {
            //处理手机查询
            UserEntity user = userService.queryUserByPhone(phone);
            if( user == null ){
                result = Result.ok();
            } else {
                result.setCodeEnum(CodeEnum.PHONE_HAS_REGISTED_ERR);
            }
        }
        return result;
    }


    /****************用户注册************/
    @PostMapping("/user/regist")
    @ResponseBody
    public Result userRegist(@RequestParam("phone") String phone,
                             @RequestParam("passwd") String password,
                             @RequestParam("code") String code,
                             HttpSession session){
        Result<String> result = Result.fail();
        //检查参数
        if( StringUtils.isAnyBlank(phone,password,code)){
            result.setCodeEnum(CodeEnum.PARAMETER_BLANK_ERROR);
        } else if( !YLBUtil.checkPhone(phone)){
            result.setCodeEnum(CodeEnum.PHONE_FORMAT_ERR);
        } else if( password.length() !=  32){
            result.setCodeEnum(CodeEnum.PARAMETER_PASSWORD_ERROR);
        } else if( !smsService.checkAuthCode(phone,code) ) {  //短信验证码
            result.setCodeEnum(CodeEnum.PARAMETER_SMSCODE_ERROR);
        } else {
            //判断次数
            int numTimes = realNameService.checkRealNameTimes(phone);
            if( numTimes > 10){
                result.setCodeEnum(CodeEnum.REALNAME_TIMES_ERROR);
                return result;
            }
            //可以注册
            UserEntity user  = userService.userPhoneRegist(phone,password);
            if( user != null ){
                //注册成功
                result = Result.ok();
                //把user放到session
                user.setLoginPassword("");
                session.setAttribute(YLBConsts.SESSION_USER,user);
            }
        }
        return result;
    }


    /****************用户实名认证*********************/
    @PostMapping("/user/realname")
    @ResponseBody
    public Result userRealName(@RequestParam("phone") String phone,
                               @RequestParam("name") String name,
                               @RequestParam("card") String card,
                               HttpSession session){
        Result<String> result = Result.fail();

        //获取session中存储的手机号
        UserEntity user  = (UserEntity) session.getAttribute(YLBConsts.SESSION_USER);
        //检查数据
        if( StringUtils.isNotBlank(user.getName())){
           // 已经认证了，无需再次认证
           result.setCodeEnum(CodeEnum.REALNAME_CHECKED_ERROR);
        } else if(StringUtils.isAnyBlank(phone,name,card)){
            result.setCodeEnum(CodeEnum.PARAMETER_BLANK_ERROR);
        } else if( !user.getPhone().equals(phone)){
            result.setCodeEnum(CodeEnum.REALNAME_PHONE_ERROR);
        } else if( !YLBUtil.checkRealName(name)){
            result.setCodeEnum(CodeEnum.REALNAME_NAME_ERROR);
        } else if( !YLBUtil.checkIdCard(card)){
            result.setCodeEnum(CodeEnum.REALNAME_IDCARD_ERROR);
        } else {
            //可以做实名认证
            UserEntity realNameUser= realNameService.handlerRealName(phone,name,card);
            if(realNameUser != null){
                session.setAttribute(YLBConsts.SESSION_USER, realNameUser);
                result = Result.ok();
            }
        }
        return result;
    }


    /****************用户登录*********************/
    @PostMapping("/user/login")
    @ResponseBody
    public Result userLogin(@RequestParam("phone") String phone,
                            @RequestParam("passwd") String password,
                            HttpSession session){
        Result<String> result = Result.fail();
        //检查用户提交的数据
        if( StringUtils.isAnyBlank(password,phone)){
            result.setCodeEnum(CodeEnum.PARAMETER_BLANK_ERROR);
        } else if( !YLBUtil.checkPhone(phone)){
            result.setCodeEnum(CodeEnum.PHONE_FORMAT_ERR);
        } else if( password.length() !=32 ){
            result.setCodeEnum(CodeEnum.PARAMETER_PASSWORD_ERROR);
        } else {
            //登录，操作数据服务接口
            UserEntity user  = userService.userLogin(phone,password);
            //登录成功，保存user到session
            if( user != null){
                user.setLoginPassword("");
                session.setAttribute(YLBConsts.SESSION_USER,user);
                result = Result.ok();
            } else {
                //没有成功，给用户提示
                result.setCodeEnum(CodeEnum.PARAMETER_PASSWORD_ERROR);
            }
        }
        return result;
    }


    /****************用户退出系统*********************/
    @GetMapping("/user/logout")
    public String userLogout(HttpSession session){
        //让session无效
        session.invalidate();
        //重定向到首页
        return "redirect:/index";

    }

    /****************查询用户资金*********************/
    @GetMapping("/user/account/money")
    @ResponseBody
    public Result queryAccountMoney(HttpSession session){
        Result<String> result = Result.fail();
        UserEntity user = (UserEntity) session.getAttribute(YLBConsts.SESSION_USER);
        AccountEntity account = accountService.queryByUid(user.getId());
        if( account != null){
            result = Result.ok();
            result.setData(account.getAvailableMoney().toPlainString());
        }
        return result;
    }

}
