package com.minlia.module.common.util;

import org.apache.commons.lang.StringUtils;

/**
 * Created by garen on 2018/6/14.
 */
public class PrivacyShieldUtils {

    public static String idNo(String idNo) {
        if (StringUtils.isNotEmpty(idNo)) {
            return idNo.replaceAll("(\\d{4})\\d{10}(\\d{4})","$1****$2");
        } else {
            return null;
        }
    }

    public static String cellphone(String cellphone) {
        if (StringUtils.isNotEmpty(cellphone)) {
            return cellphone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
        } else {
            return null;
        }
    }

    public static String name(String name) {
        if (StringUtils.isNotEmpty(name)) {
            return name.replaceAll("([\\u4e00-\\u9fa5]{1})(.*)", "$1" + createAsterisk(name.length() - 1));
        } else {
            return null;
        }
    }

    /**
     * 生成很多个*号
     */
    public static String createAsterisk(int length) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            stringBuffer.append("*");
        }
        return stringBuffer.toString();
    }

//    public static void main(String[] args) {
//        String phone = "18771632488";
//        System.out.println(phone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
//
//        String idCard = "421302199208165464";
//        System.out.println(idCard.replaceAll("(\\d{4})\\d{10}(\\d{4})","$1****$2"));
//
//        String name = "后市大范德萨发";
//        System.out.println(name(name));
//    }

}