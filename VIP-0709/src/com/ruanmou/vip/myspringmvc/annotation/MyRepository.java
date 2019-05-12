package com.ruanmou.vip.myspringmvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 软谋教育Java VIP课程
 * <pre>
 *    今日内容:IOC和DI
 * </pre>
 * 标注DAO层类
 * @author gerry
 * @date 2018-07-03
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MyRepository {
    String value() default "";
}
