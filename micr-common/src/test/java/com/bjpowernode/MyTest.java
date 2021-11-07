package com.bjpowernode;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class MyTest {

    @Test
    public void test01(){
        String s="【动力金融】你的验证码是：%s，3分钟内有效！请不要泄露";

        String code="123456";

        String res = String.format(s, code);
        System.out.println("res = " + res);

    }

    @Test
    public void test02(){
        //commons-lang3
        String r1 = RandomStringUtils.randomAlphabetic(6);
        System.out.println("r1 = " + r1);

        String r2 = RandomStringUtils.randomAlphanumeric(6);
        System.out.println("r2 = " + r2);

        String r3 = RandomStringUtils.randomNumeric(6);
        System.out.println("r3 = " + r3);




    }

    @Test
    public void test03(){
        String reg="^[\\u4e00-\\u9fa5]{0,}$";

        boolean b = Pattern.matches(reg, "张升");
        System.out.println("b = " + b);
    }

    @Test
    public void test04(){
        Date bdate = null; //'2021-10-18 00:00:00'
        Date edate = null; //'2021-10-19 00:00:00'

        Date cur = new Date(); //2021-10-19
        //18日时间
        bdate = DateUtils.addDays(cur,-1);
        System.out.println("bdate="+DateUtils.truncate(DateUtils.addDays(cur,-1), Calendar.DATE));

        edate  = DateUtils.truncate(cur,Calendar.DATE);
        System.out.println("edate = " + edate);

    }

    @Test
    public void test5(){

        BigDecimal rate = new BigDecimal("0.12");

        BigDecimal r = rate.divide(new BigDecimal("360"), 10, RoundingMode.HALF_UP);
        System.out.println("r = " + r);

    }

    @Test
    public void test06(){
        String yuan="0.10";
        String str =  new BigDecimal(yuan).multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString();
        System.out.println(str);
    }
}
