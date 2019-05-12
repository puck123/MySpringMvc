package com.ruanmou.vip.myspringmvc.util;

import com.ruanmou.vip.myspringmvc.annotation.MyController;
import com.ruanmou.vip.myspringmvc.annotation.MyRepository;
import com.ruanmou.vip.myspringmvc.annotation.MyService;

/**
 * 软谋教育Java VIP课程
 * <pre>
 *    今日内容:IOC和DI
 * </pre>
 *
 * @author gerry
 * @date 2018-07-03
 */
public class ReflectUtil {

    /**
     * 根据类对象获取首字母小写别名
     */
    private static String getLowerAlias(Class<?> cl) {
        String simpleName = cl.getSimpleName();
        String start =simpleName.substring(0,1).toLowerCase();
        String end = simpleName.substring(1);

        return  start + end;
    }

    /**
     * 获取到注解对应别名称
     * @param cl
     * @return
     */
    public static String getAnnotationAlias(Class<?> cl) {
        // 默认别名
        String aliasName = getLowerAlias(cl);

        if (cl.isAnnotationPresent(MyRepository.class)) {
            MyRepository myRepository = cl.getAnnotation(MyRepository.class);

            if (!"".equals(myRepository.value())) {
                aliasName = myRepository.value();
            }
         } else if (cl.isAnnotationPresent(MyService.class)) {
            MyService myService = cl.getAnnotation(MyService.class);

            if (!"".equals(myService.value())) {
                aliasName = myService.value();
            }
        } else if (cl.isAnnotationPresent(MyController.class)) {
            MyController myController = cl.getAnnotation(MyController.class);

            if (!"".equals(myController.value())) {
                aliasName = myController.value();
            }
        }

        return aliasName;
    }

    /**
     * 定义处理url路径的方法
     * @param url
     * @return
     */
    public static String handlerUrl(String url) {
        if (url.startsWith("/")) {
            return url;
        } else {
            return  "/" + url;
        }
    }

}
