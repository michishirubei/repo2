package com.bjpowernode.micrtask;

import com.bjpowernode.service.bussiness.IncomeService;
import com.bjpowernode.service.pay.ExportKuaiQianService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component("taskManager")
public class TaskManager {

    @DubboReference(interfaceClass = IncomeService.class,version = "1.0")
    private IncomeService incomeService;


    @DubboReference(interfaceClass = ExportKuaiQianService.class,version = "1.0")
    private ExportKuaiQianService kuaiQianService;

    /**
     * 定义定时任务的方法
     * 1.public方法
     * 2.方法没有参数
     * 3.方法没有返回值
     */

    //@Scheduled(cron = "*/20 * * * * ?")
    public void testTask(){
        System.out.println("定时任务时间："+new Date());
    }

    //每天上午10点53分01秒
    //@Scheduled(cron = "1 53 10 * * ?")
    public void testTask2(){
        System.out.println("53 定时任务时间："+new Date());
    }



    //调用投资服务，生成收益计划
    @Scheduled(cron = "0 0 1 * * ?")
    public void invokeGenerateIncomePlan(){
        System.out.println("====开始执行了生成收益计划的方法====");
        incomeService.generateIncomePlan();
        System.out.println("====完成执行了生成收益计划的方法====");
    }

    //调用投资服务，收益返还
    @Scheduled(cron = "0 0 2 * * ?")
    public void invokeGeneraterIncomeBack(){
        System.out.println("=====开始执行收益返还============");
        incomeService.generateIncomeBack();
        System.out.println("=====完成执行收益返还============");
    }


    //调用快钱查询接口
    @Scheduled(cron = "0 */20 * * * ?")
    public void invokeMicrPayKqQueryInterface(){
        System.out.println("=====快钱查询接口，订单补偿============");
        kuaiQianService.handlerKQDiaoDan();
        System.out.println("=====快钱查询接口，订单补偿============");
    }

}
