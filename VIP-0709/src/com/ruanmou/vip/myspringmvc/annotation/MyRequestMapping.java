package com.ruanmou.vip.myspringmvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 软谋教育Java VIP课程
 * <pre>
 *    用于标注处理请求路径
 * </pre>
 *
 * @author gerry
 * @date 2018-07-04
 */
@Target({ElementType.TYPE, ElementType.METHOD}) // 可以标注类和方法上面
@Retention(RetentionPolicy.RUNTIME)
public @interface MyRequestMapping {
    String value() default ""; // 配置方法的访问路径
    MethodType method() default  MethodType.NONE; // 配置请求方式

    /**
     * 定义方法类型枚举
     */
    public enum MethodType {
        PUT,
        DELETE,
        GET,
        POST,
        UPDATE,
        NONE
    }
}
