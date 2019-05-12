package com.ruanmou.vip.myspringmvc;
import com.bk.util.StringUtils;
import com.ruanmou.vip.myspringmvc.annotation.*;
import com.ruanmou.vip.myspringmvc.util.ArrayUtil;
import com.ruanmou.vip.myspringmvc.util.BeanUtils;
import com.ruanmou.vip.myspringmvc.util.ClassUtils;
import com.ruanmou.vip.myspringmvc.util.ReflectUtil;
import com.ruanmou.vip.myspringmvc.util.javassits.Classes;
import com.ruanmou.vip.myspringmvc.xml.XMLConfigurationParser;
import com.ruanmou.vip.orm.core.handler.mysql.MySQLTemplateHandler;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * 软谋教育Java VIP课程
 * <pre>
 *    今日内容:IOC和DI
 * </pre>
 *
 * @author gerry
 * @date 2018-07-03
 */
public class DispatcherServlet extends HttpServlet {
    // 定义存储MySpringMVC配置文件的路径
    private String contextConfigLocation;
    // 定义一个线程安全的集合存储需要扫描管理类
    private List<Class<?>> classesList = Collections.synchronizedList(new ArrayList<Class<?>>());
    // 定义存储实例别名与对应实例关系的集合对象
    private Map<String, Object> contextContainer = Collections.synchronizedMap(new HashMap<String, Object>());
    // 定义存储访问路径与方法的映射关系的集合
    private Map<String, Object> urlMappingContext = Collections.synchronizedMap(new HashMap<String, Object>());

    public DispatcherServlet() {
        System.out.println("servlet实例化");
    }

    /**
     * 设置配置文件的路径的方法
     * @param contextConfigLocation
     */
    public void setContextConfigLocation(String contextConfigLocation) {
        this.contextConfigLocation = contextConfigLocation.replace("classpath:", "");
    }

    /**
     * 初始化MySpringMVC容器
     * @throws ServletException
     */
    public void init() throws ServletException {
        // 设置配置文件的路径
        setContextConfigLocation(getInitParameter("contextConfigLocation"));

        try {
            // 扫描配置文件指定的根包路径下面所有需要交给MySpring框架管理类
            String basePackage = XMLConfigurationParser.readXMLBasePackage(contextConfigLocation);
            scanBasePackageAnnotationClass(basePackage);
            // 把所有扫描到的类(用于完成实例类别名映射关系)【别名做为key，实例作为value】
            doIoc();
            // 完成运行时所有依赖对象的装配
            doDI();
            // 完成访问路径与对应方法映射装配
            urlMappingToMethod();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理客户请求的业务逻辑
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 定义保存参数类型对应值的集合
        List<Object> parameterList = null;
        // 定义一个存储形参名称与形参类型对应的关系
        Map<String, Class<?>> paramMappingToClass = new LinkedHashMap<String, Class<?>>();
        // 调用方法后返回的路径
        Object url = null;
        // 获取当前访问的路径
        String uri = request.getRequestURI();
        // 获取当前项目访问context路径
        String contextPath = request.getContextPath();
        // 获取当前访问的方法路径
        String urlMethodMapping = uri.replace(contextPath, "");
        // 通过访问映射路径找到对应方法
        Method method = (Method) urlMappingContext.get(urlMethodMapping);

        try {
            if (method != null) {
                // 获取形参列表
                String[] methodParamNames = Classes.getMethodParamNames(method);
                // 获取到方法的参数类型列表
                Class<?>[] parameterTypes = method.getParameterTypes();
                // 完成参数列表与形参名称的对应关系
                for (int i = 0; i < methodParamNames.length; i++) {
                    paramMappingToClass.put(methodParamNames[i], parameterTypes[i]);
                }

                if (!paramMappingToClass.isEmpty()) {
                    // 定义存储方法调用参数值列表
                    parameterList = new ArrayList<Object>();

                    for (Map.Entry<String, Class<?>> map : paramMappingToClass.entrySet()) {
                        Class<?> cl = map.getValue();
                        if (!cl.isInterface()) {
                            if (ClassUtils.isSystemClass(cl)) {
                                // 定义处理系统类的方法
                                parameterList.add(BeanUtils.convertHttpServletRequestToSystemClassBean(request, cl, map.getKey()));
                            } else {
                                // 处理自定义类型方法
                                parameterList.add(BeanUtils.convertHttpServletRequestToJavaBean(request, cl));
                            }
                        } else {
                            // 添加到javaweb中常用HttpServlet中API对象
                            if (cl == HttpServletRequest.class) {
                                // 注入request对象
                                parameterList.add(request);
                            } else if (cl == HttpServletResponse.class) {
                                // 注入reqpsone对象
                                parameterList.add(response);
                            } else if (cl == HttpSession.class) {
                                // 注入session对象
                                parameterList.add(request.getSession());
                            } else if (cl == ServletContext.class) {
                                // 注入applicaiton对象
                                parameterList.add(this.getServletContext());
                            }
                        }
                    }
                }

                // 确定方法对应实例对象Class对象
                Class<?> declaringClass = method.getDeclaringClass();
                // 通过class对象获取容器中实例化好的对象
                Object instance = getContextBean(ReflectUtil.getAnnotationAlias(declaringClass));

                if (null != parameterList && parameterList.size() > 0) {
                    // 反射调用方法
                    url = method.invoke(instance, parameterList.toArray());
                } else {
                    // 反射调用方法
                    url = method.invoke(instance);
                }

                if (null != url) {
                    String urlString = url.toString();
                    if (urlString.startsWith("redirect:")) {
                        // 重定向
                        response.sendRedirect(request.getContextPath() + StringUtils.trimByPrefix(urlString, "redirect:"));
                    } else if (urlString.startsWith("forward:")) {
                        // 转发
                        request.getRequestDispatcher(StringUtils.trimByPrefix(urlString, "forward:")).forward(request, response);
                    } else {
                        // 默认为转发
                        request.getRequestDispatcher(urlString).forward(request, response);
                    }
                } else {
                    System.out.println("访问的" + url + "路径不存在，请检查。");
                }
            } else {
                response.setStatus(404);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 完成访问路径与对应方法的映射装配
     */
    private void urlMappingToMethod() {
        if (classesList.size() == 0) {
            return;
        }

        for (int i = 0; i < classesList.size(); i++) {
            Class<?> cl = classesList.get(i);

            // 判断是否有MyController注解
            if (cl.isAnnotationPresent(MyController.class)) {
                // 判断是否存在MyRequestMapping注解
                if (cl.isAnnotationPresent(MyRequestMapping.class)) {
                    // 获取注解配置的value值
                    MyRequestMapping requestMapping = cl.getAnnotation(MyRequestMapping.class);
                    // 定义存储根路径的句柄
                    String baseUrlMapping = null;
                    if (!"".equals(requestMapping.value())) {
                        baseUrlMapping = ReflectUtil.handlerUrl(requestMapping.value());
                    }

                    // 要求获取方法必须是public修饰的方法，并且是本类中定义的方法
                    Method[] methods = cl.getDeclaredMethods();

                    if (ArrayUtil.isNotEmpty(methods)) {
                        for (Method method : methods) {
                            if (method.getModifiers() == Modifier.PUBLIC &&
                                    method.isAnnotationPresent(MyRequestMapping.class)) {
                                // 获取方法上面的MyRequestMapping对象
                                MyRequestMapping myRequestMapping = method.getAnnotation(MyRequestMapping.class);
                                // 定义存储方法完成映射路径的字符串
                                String methodUrlMapping = baseUrlMapping;

                                if (!"".equals(myRequestMapping.value())) {
                                    String url = ReflectUtil.handlerUrl(myRequestMapping.value());
                                    methodUrlMapping = baseUrlMapping+url;
                                }
                                // 完成方法的访问路径与对应的方法的映射
                                urlMappingContext.put(methodUrlMapping, method);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 执行DI操作
     */
    private void doDI() throws IllegalAccessException {
        // 没有需要容器管理的类
        if (classesList.size() == 0) {
            return;
        }

        // 遍历每一个类
        for (int i = 0; i < classesList.size(); i++) {
            Class<?> cl = classesList.get(i);
            // 获取到别名
            String annotationAlias = ReflectUtil.getAnnotationAlias(cl);
            // 获取需要注入依赖对象实例
            Object annotationInstance = contextContainer.get(annotationAlias);
            // 获取当前类下面所有定义属性
            Field[] fields = cl.getDeclaredFields();

            if (ArrayUtil.isNotEmpty(fields)) {
                // 循环判断是否添加@MyAutowried注解
                for (Field field : fields) {
                    if (field.isAnnotationPresent(MyAutowired.class)) {
                        // 获取注解对象
                        MyAutowired myAutowired = field.getAnnotation(MyAutowired.class);

                        // 定义变量获取需要装配的依赖对象
                        Object injectionObj = null;

                        if (!"".equals(myAutowired.value())) {
                            // 在contextContainer容器中存储的key值吗？
                            String instanceName = myAutowired.value();
                            // 按照名称注入依赖对象
                            injectionObj = contextContainer.get(instanceName);
                        } else {
                            // 默认按照类型注入对应的依赖对象
                            // 1、获取字段的类型
                            Class<?> fieldType = field.getType();
                            // 2、获取到容器中所有装配的实例集合
                            Collection values = contextContainer.values();
                            Iterator iterator = values.iterator();
                            // 3、遍历找到与当前字段类型匹配对象
                            while (iterator.hasNext()) {
                                // 获取ioc容器中对应实例
                                Object obj = iterator.next();

                                // 判断当前对象的类型是否为字段类型同类型或者子类型
                                if (fieldType.isAssignableFrom(obj.getClass())) {
                                    // 确定对应类型的注入对象
                                    injectionObj = obj;
                                    break;
                                }
                            }

                        }

                        // 把需要依赖对象注入到对应实例中
                        field.setAccessible(true);
                        field.set(annotationInstance, injectionObj);
                    }
                }

            }
        }


    }

    /**
     * 执行IOC操作
     */
    private void doIoc() throws Exception {
        // 没有需要容器管理的类
        if (classesList.size() == 0) {
            return;
        }

        for (int i = 0; i < classesList.size(); i++) {
            Class<?> cl = classesList.get(i);
            // 获取到别名
            String annotationAlias = ReflectUtil.getAnnotationAlias(cl);
            // 把别名与实例映射起来
            contextContainer.put(annotationAlias, cl.newInstance());
        }
        // 加入模板
        String templateName = ReflectUtil.getAnnotationAlias(MySQLTemplateHandler.class);
        contextContainer.put(templateName, new MySQLTemplateHandler());
    }


    /**
     * 扫描当前根包下面需要交给容器管理类
     * @param basePackage
     */
    private void scanBasePackageAnnotationClass(String basePackage) throws URISyntaxException {
        // 获取到本磁盘上面根包路径
        URL url = this.getClass().getClassLoader().getResource(basePackage.replace(".","/"));
        // 创建当前这个路径对应文件对象
        File file = new File(url.toURI());
        // 获取当前路径下面所有的子文件
        file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File childFile) {
                if (childFile.isDirectory()) {
                    try {
                        scanBasePackageAnnotationClass(basePackage+"."+childFile.getName());
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (childFile.getName().endsWith(".class")) {
                        String className = childFile.getName().replace(".class","");
                        try {
                            Class<?> aClass = this.getClass().getClassLoader().loadClass((basePackage + "." + className));
                                // 只需要使用@MyRepository,@MyService, @MyController这些类才交给容器管理
                                if (aClass.isAnnotationPresent(MyRepository.class)
                                        || aClass.isAnnotationPresent(MyService.class)
                                        || aClass.isAnnotationPresent(MyController.class)) {
                                    // 把需要管理的类的Class对象全部存储起来
                                    classesList.add(aClass);
                                }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return false;
            }
        });
    }

    /**
     * 定义根据容器key获取对应实例
     */
    public Object getContextBean(String beanName) {
        return contextContainer.get(beanName);
    }

    public static void main(String[] args) throws ServletException {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
    }
}
