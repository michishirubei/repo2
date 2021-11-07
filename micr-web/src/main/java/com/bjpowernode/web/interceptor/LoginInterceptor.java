package com.bjpowernode.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.consts.YLBConsts;
import com.bjpowernode.entity.UserEntity;
import com.bjpowernode.enums.CodeEnum;
import com.bjpowernode.vo.Result;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

//登录拦截器
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        System.out.println("==============拦截器代码的执行了============");
        //从Header中获取 X-Requested-With
        String xwith = request.getHeader("X-Requested-With");
        System.out.println("xwith="+xwith);

        //判断是否登录
        HttpSession session = request.getSession();

        UserEntity user  = (UserEntity) session.getAttribute(YLBConsts.SESSION_USER);
        if( user == null ){
            if("XMLHttpRequest".equalsIgnoreCase(xwith)){
                //ajax请求。返还数据
                Result<String> result = Result.fail();
                result.setCodeEnum(CodeEnum.LOGIN_REQUIED_ERROR);

                response.setContentType("application/json;charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println(JSONObject.toJSONString(result));
                out.flush();
                out.close();
            } else {
                response.sendRedirect( request.getContextPath() + "/user/page/login");
            }

            return false;
        }

        return true;
    }
}
