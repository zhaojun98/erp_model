package com.bdqn.sys.utils;

public class CommonUtils {

    /**
     * 字符串不为空
     * @param str
     * @return  空：false  不为空：true
     */
    public static boolean isNotBlank(String str){
        if(str!=null && str.trim().length()!=0){
            return true;
        }
        return false;
    }

    /**
     * 字符串为空
     * @param str
     * @return  空：true  不为空：false
     */
    public static boolean isBlank(String str){
        if(str!=null && str.trim().length()!=0){
            return false;
        }
        return true;
    }

    /**
     * 对象为空
     * @param object
     * @return  空：true  不为空：false
     */
    public static boolean isEmpty(Object object){
        if(object!=null && !object.equals("")){
            return false;
        }
        return true;
    }

    /**
     * 对象不为空
     * @param object
     * @return  空：false  不为空：true
     */
    public static boolean isNotEmpty(Object object){
        if(object!=null && !object.equals("")){
            return true;
        }
        return false;
    }

}
