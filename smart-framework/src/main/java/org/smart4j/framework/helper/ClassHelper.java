package org.smart4j.framework.helper;

import org.smart4j.framework.annotation.Controller;
import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.util.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * 类操作助手类，获取基础包名所对应的所有类，service类，controller类，框架所管理的bean
 * Created by zhaoshiqiang on 2016/11/5.
 */
public final class ClassHelper {

    /**
     * 定义类集合（用于存放所加载的类）
     */
    private static final Set<Class<?>> CLASS_SET;

    static {
        String basePackage = ConfigHelper.getAppBasePackage();
        CLASS_SET = ClassUtil.getClassSet(basePackage);
    }

    public static Set<Class<?>> getClassSet(){
        return CLASS_SET;
    }

    /**
     * 获取应用包名下所有 Service 类
     */
    public static Set<Class<?>> getServiceClassSet(){
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> cls : CLASS_SET){
            if (cls.isAnnotationPresent(Service.class)){
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /**
     * 获取应用包名下所有 Controller 类
     */
    public static Set<Class<?>> getControllerClassSet(){
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> cls : CLASS_SET){
            if (cls.isAnnotationPresent(Controller.class)){
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /**
     * 获取应用包名下所有 Bean 类（我们将带有Controller注解与Service注解的类所产生的对象，理解为由框架所管理的bean）
     */
    public static Set<Class<?>> getBeanClassSet(){
        Set<Class<?>> beanClassSet = new HashSet<Class<?>>();
        beanClassSet.addAll(getServiceClassSet());
        beanClassSet.addAll(getControllerClassSet());
        return beanClassSet;
    }

    /**
     *获取应用包名下某父类（或接口）的所有之类（或实现类）
     */
    public static Set<Class<?>> getClassSetBySuper(Class<?> superClass){
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> cls : CLASS_SET){
            //自身类.class.isAssignableFrom(自身类或子类.class)  返回true
            if (superClass.isAssignableFrom(cls) && !superClass.equals(cls)){
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /**
     *获取应用包名下带有某注解的所有类
     */
    public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotationClass){
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> cls : CLASS_SET){
            if (cls.isAnnotationPresent(annotationClass)){
                classSet.add(cls);
            }
        }
        return classSet;
    }

}
