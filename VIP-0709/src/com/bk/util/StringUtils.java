package com.bk.util;

/**
 * 软谋教育Java VIP课程
 * <pre>
 *    今日内容:过滤器和监听器
 * </pre>
 *
 * @author gerry
 * @date 2018-07-02
 */
public class StringUtils {
    /**
     * 截取前缀的方法
     * @param strValue
     * @param prefix
     * @return
     */
    public static String trimByPrefix(String strValue ,String prefix) {
        return strValue.replace(prefix, "");
    }
}
