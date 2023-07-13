package com.dvaren.bill.utils;

import java.util.Calendar;
import java.util.List;

/**
 * @description:
 * @author: iiu
 * @date: 2021/6/7 23:33
 */
public class TextUtil {
    public static boolean isEmpty(String s){
        return s == null || s.isEmpty();
    }
    public static boolean containEmptyValue(List<String> args){
        for (int i = 0; i < args.size(); i++) {
            if( args.get(i) == null || args.get(i).isEmpty()){
                return true;
            }
        }
        return false;
    }

    public static String generateNickName() {
        return "user_" + (int) (Math.random()*10000000);
    }

    public static String generateAvatar() {
        return "https://iiu.oss-cn-chengdu.aliyuncs.com/blog-v2/default-avatar/" + (int)(Math.random()*5+1) + ".png";
    }
    /**
     * 用户名验证
     *  @param name
     *  @return
     */
    public static boolean checkName(String name) {
        String regExp = "^[a-zA-Z0-9_-]{4,16}$";
        return name.matches(regExp);
    }
    /**
     * 密码验证
     *  @param pwd
     *  @return
     */
    public static boolean checkPwd(String pwd) {
        String regExp = "^[\\w_]{6,20}$";
        return pwd.matches(regExp);
    }
    /**
     * 判断当前时间距离第二天凌晨的秒数
     *
     * @return 返回值单位为[s:秒]
     */
    public static Long getSecondsNextEarlyMorning(Integer day) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }
}
