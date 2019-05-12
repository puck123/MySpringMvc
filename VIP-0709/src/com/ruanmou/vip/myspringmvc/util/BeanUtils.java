package com.ruanmou.vip.myspringmvc.util;

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
    public static Object convertHttpServletRequestToSystemClassBean(HttpServletRequest request, Class<?> clazz, String paramName) throws Exception {
        // 获取request对象中所有表单元素名称
        Enumeration<String> parameterNames = request.getParameterNames();
        // 定义存储字段需要的值
        Object fieldValue = null;

        while (parameterNames.hasMoreElements()) {
            String fieldName = parameterNames.nextElement();

            if (!fieldName.equals(paramName)) {
                continue;
            }

            // 定义获取到表单元素名称对应值
            String value = request.getParameter(fieldName);

            fieldValue = ClassUtils.converterType(clazz, value);
            break;
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

            // 判断是否为当前类的字段名称
            if (!ClassUtils.isFieldNameInClass(clazz,fieldName)) {
                continue;
            }

            try {
                // 根据参数名称获取字段对象
                Field field = clazz.getDeclaredField(fieldName);
                // 获取到字段类型
                Class<?> fieldType = field.getType();

                // 定义获取到表单元素名称对应值
                String value = request.getParameter(fieldName);

                // 根据字段类型获取对应的值
                Object fieldValue = ClassUtils.converterType(field.getType(), value);

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
