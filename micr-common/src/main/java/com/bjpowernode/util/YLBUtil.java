package com.bjpowernode.util;


import java.util.regex.Pattern;

//工具类
public class YLBUtil {
    /********产品类型时0 ，1 ，2 ****************/
    public static  boolean typeInRange(Integer productType){
        boolean flag  = false; //没有通过
        if( productType != null){
            if( productType >= 0 && productType <= 2){
                flag = true;
            }
        }
        return flag;
    }

    /******** pageNo的默认值处理  1 ****************/
    public static  Integer pageNo(Integer pNo){
        Integer pageNo = pNo;
        if( pNo == null || pNo < 1){
            pageNo = 1;
        }
        return pageNo;
    }

    /******** pageSize 的默认值处理 5 ****************/
    public static  Integer pageSize(Integer pSize){
        Integer pageSize= pSize;
        if( pSize == null || pSize  > 50 || pSize < 1){
            pageSize = 5;
        }
        return pageSize;
    }
    /******** 计算limit 中的offSet ****************/
    public static  Integer offSet(Integer pageNo,Integer pageSize){
        return (pageNo-1)*pageSize;
    }

    /******** 检查手机号格式是否正确， true正确，false不正确 ****************/
    public static boolean checkPhone(String phone){
        boolean flag = false;
        if( phone != null ){
            flag = Pattern.matches("^1[1-9]\\d{9}$",phone);
        }
        return flag;
    }


    /******** 检查姓名必须是中文， true正确，false不正确 ****************/
    public static boolean checkRealName(String name){
        boolean flag = false;
        if(name != null){
            String reg="^[\\u4e00-\\u9fa5]{0,}$";
            flag = Pattern.matches(reg, name);
        }
        return flag;
    }

    /******** 检查姓身份证号， true正确，false不正确 ****************/
    public static boolean checkIdCard(String card){
        boolean flag = false;
        if( card != null){
            flag = Pattern.matches("(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)",card);
        }
        return flag;
    }
    public static void main(String[] args) {
        String phone="1360000000";
        boolean b = checkPhone(phone);
        System.out.println("b = " + b);
    }

}
