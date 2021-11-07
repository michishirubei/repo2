package com.bjpowernode.web.settings;

import com.bjpowernode.web.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SystemApplicationConfig implements WebMvcConfigurer {

    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        String addPath [] = { "/invest/**" , "/recharge/**","/user/**" };
        String exludePath []=
                {
                    "/product/**","/sms/**","/phone/registed",
                    "/user/login","/user/logout","/user/page/login",
                    "/user/page/regist","/user/regist"
                };
        registry.addInterceptor( new LoginInterceptor() )
                .addPathPatterns(addPath)
                .excludePathPatterns(exludePath);

    }
}
