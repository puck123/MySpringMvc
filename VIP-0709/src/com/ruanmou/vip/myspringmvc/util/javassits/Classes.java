package com.ruanmou.vip.myspringmvc.util.javassits;

import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 软谋教育Java VIP课程
 * <pre>
 *    基于javassist的工具类
 * </pre>
 *
 * @author gerry
 * @date 2018-07-08
 */
public class Classes {
    private Classes() {

    }

    /**
     * <p>
     * 获取方法参数名称
     * </p>
     *
     * @param cm
     * @return
     */
    protected static String[] getMethodParamNames(CtMethod cm) {
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
                .getAttribute(LocalVariableAttribute.tag);

        String[] paramNames = null;
        try {
            paramNames = new String[cm.getParameterTypes().length];
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int i = 0; i < paramNames.length; i++) {
            paramNames[i] = attr.variableName(i + pos);
        }
        return paramNames;
    }

    /**
     * 获取方法参数名称，按给定的参数类型匹配方法
     *
     * @param clazz
     * @param method
     * @param paramTypes
     * @return
     */
    public static String[] getMethodParamNames(Class<?> clazz, String method,
                                               Class<?>... paramTypes) {

        ClassPool pool = ClassPool.getDefault();
        CtClass cc = null;
        CtMethod cm = null;
        try {
            cc = pool.get(clazz.getName());

            String[] paramTypeNames = new String[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++)
                paramTypeNames[i] = paramTypes[i].getName();

            cm = cc.getDeclaredMethod(method, pool.get(paramTypeNames));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return getMethodParamNames(cm);
    }

    /**
     * 获取方法参数名称组成的数组
     *
     * @param method
     * @return
     * @throws NotFoundException 如果类或者方法不存在
     */
    public static String[] getMethodParamNames(Method method) {

        ClassPool pool = ClassPool.getDefault();
        CtClass cc;
        CtMethod cm = null;
        try {
            cc = pool.get(method.getDeclaringClass().getName());
            cm = cc.getDeclaredMethod(method.getName());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return getMethodParamNames(cm);
    }

    public void test(String addd, int bww, String aaa123) {

    }

    public static void main(String[] args) throws NoSuchMethodException {
        System.out.println(Arrays.toString(getMethodParamNames(Classes.class.getDeclaredMethod("test", String.class, int.class, String.class))));
    }
}
