package com.ruanmou.vip.myspringmvc.util;

import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 软谋教育Java VIP课程
 * <pre>
 *    Class常用操作的工具类型
 * </pre>
 *
 * @author gerry
 * @date 2018-07-02
 */
public class ClassUtils {

    private static final String SERIALIZE_UID =  "serialVersionUID";
    /**
     * 判断一个类是否是系统类
     * @param cl
     * @return
     */
    public static boolean isSystemClass(Class<?> cl) {
        System.out.println(cl.getClassLoader());
        return cl != null && cl.getClassLoader() == null;
    }

    /**
     * 判断某个字段名称是否属于某个类
     */
    public static boolean isFieldNameInClass(Class<?> cl ,String fieldName) {

        // 获取当前类中所有定义字段
        Field[] fields = cl.getDeclaredFields();
        if (ArrayUtil.isNotEmpty(fields)) {
            for (Field field : fields) {
                if (SERIALIZE_UID.equals(field.getName())) {
                    continue;
                }

                if(fieldName.equals(field.getName())) {
                    return true;
                }
            }
        }

        return  false;
    }

    /**
     * 把一个字符串转换为字段对应类型
     */
    public static Object converterType(Class<?> fieldType, String value) throws Exception {
        Object fieldValue = null;

        if (fieldType == Integer.class) {
            fieldValue = Integer.valueOf(value);
        } else if (fieldType == int.class){
            fieldValue = Integer.parseInt(value);
        } else if (fieldType == Double.class){
            fieldValue = Double.valueOf(value);
        } else if (fieldType == double.class){
            fieldValue = Double.parseDouble(value);
        } else if (fieldType == BigDecimal.class){
            fieldValue = new BigDecimal(value);
        } else if (fieldType == Date.class){
            fieldValue = new SimpleDateFormat("yyyy-MM-dd").parse(value);
        } else if (fieldType == String.class) {
            fieldValue = value;
        } else if (fieldType == Boolean.class) {
            fieldValue = Boolean.valueOf(value);
        } else if (fieldType == boolean.class) {
            fieldValue = Boolean.parseBoolean(value);
        }

        return fieldValue;
    }
}
