package com.bk.util;

import jdk.nashorn.internal.ir.ReturnNode;
import sun.applet.Main;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

/**
 * 软谋教育Java VIP课程
 * <pre>
 *    今日内容:过滤器和监听器
 * </pre>
 *
 * @author gerry
 * @date 2018-06-29
 */
public class BeanUtils {

    /**
     * 把非自定义类型转换为Object对象
     * @param request
     * @param clazz
     * @return
     * @throws Exception
     */
    public static Object convertHttpServletRequestToSystemClassBean(HttpServletRequest request, Class<?> clazz) throws Exception {
        // 获取request对象中所有表单元素名称
        Enumeration<String> parameterNames = request.getParameterNames();
        // 定义存储字段需要的值
        Object fieldValue = null;

        while (parameterNames.hasMoreElements()) {
            String fieldName = parameterNames.nextElement();
            if ("param".equals(fieldName)) {
                continue;
            }
            // 定义获取到表单元素名称对应值
            String value = request.getParameter(fieldName);

            if (clazz == Integer.class) {
                fieldValue = Integer.valueOf(value);
            } else if (clazz == int.class) {
                fieldValue = Integer.parseInt(value);
            } else if (clazz == Double.class) {
                fieldValue = Double.valueOf(value);
            } else if (clazz == double.class) {
                fieldValue = Double.parseDouble(value);
            } else if (clazz == BigDecimal.class) {
                fieldValue = new BigDecimal(value);
            } else if (clazz == Date.class) {
                fieldValue = new SimpleDateFormat("yyyy-MM-dd").parse(value);
            } else if (clazz == String.class) {
                fieldValue = value;
            }
        }
        return fieldValue;
    }

    /**
     * 把request对象中提交的表单数据封装为一个对应JavaBean对象
     */
    public static <T> T convertHttpServletRequestToJavaBean(HttpServletRequest request, Class<T> clazz) throws Exception {
        // 创建一个对象的实例
        T instance = clazz.newInstance();

        // 获取request对象中所有表单元素名称
        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String fieldName = parameterNames.nextElement();
            if ("param".equals(fieldName)) {
                continue;
            }

            try {
                // 根据参数名称获取字段对象
                Field field = clazz.getDeclaredField(fieldName);
                // 获取到字段类型
                Class<?> fieldType = field.getType();

                // 定义获取到表单元素名称对应值
                String value = request.getParameter(fieldName);

                // 定义存储字段需要的值
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
                }

                // 设置访问权限
                field.setAccessible(true);
                // 设置字段的值
                field.set(instance,fieldValue);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        return instance;
    }
}
