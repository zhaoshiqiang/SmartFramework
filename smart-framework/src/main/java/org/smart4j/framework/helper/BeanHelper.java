package org.smart4j.framework.helper;

import org.smart4j.framework.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * bean 助手类，它相当于一个bean容器，用于存放bean类与bean实例映射关系，可通过bean类，获取bean实例
 * Created by zhaoshiqiang on 2016/11/6.
 */
public final class BeanHelper {

    /**
     * 定义bean映射（用于存放bean类与bean实例映射关系）
     */
    private static final Map<Class<?>,Object> BEAN_MAP = new HashMap<Class<?>, Object>();

    static {
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
        for (Class<?> beanClass : beanClassSet){
            BEAN_MAP.put(beanClass, ReflectionUtil.newInstance(beanClass));
        }
    }

    /**
     * 获取 Bean 映射
     */
    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> cls){
        if (!BEAN_MAP.containsKey(cls)){
            throw new RuntimeException("can not get bean by class: " + cls);
        }
        return (T) BEAN_MAP.get(cls);
    }

    public static void setBean(Class<?> cls,Object obj){
        BEAN_MAP.put(cls,obj);
    }
}
