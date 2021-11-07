package com.bjpowernode.util;

import java.math.BigDecimal;

//操作BigDecimal类型
public class DecimalUtil {

    /*********n1 >= n2 true: false ************/
    public static boolean ge(BigDecimal n1,BigDecimal n2){
        if( n1 == null || n2 == null){
            throw new RuntimeException("比较的参数异常");
        }
        return n1.compareTo(n2) >=0;
    }

    public static void main(String[] args) {
        BigDecimal n1 = new BigDecimal("100");
        BigDecimal n2 = new BigDecimal("200");

        boolean ge = ge(n1, n2);
        System.out.println("ge = " + ge);
    }

}
