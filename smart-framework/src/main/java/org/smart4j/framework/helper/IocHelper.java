package org.smart4j.framework.helper;

import org.smart4j.framework.annotation.Inject;
import org.smart4j.framework.util.ArrayUtil;
import org.smart4j.framework.util.CollectionUtil;
import org.smart4j.framework.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 这个IOC框架所管理的对象都是单例的。
 * 由于IOC框架底层还是从BeanHelper中获取bean map，
 * 而bean map中的对象都是事先创建好并放入这个bean容器的，
 * 所有的对象都是单例的
 * Created by zhaoshiqiang on 2016/11/6.
 */
public final class IocHelper {

    static {
        //获取所有的bean类与bean实例之间的映射关系(简称bean map)
        Map<Class<?>,Object> beanMap = BeanHelper.getBeanMap();
        if (CollectionUtil.isNotEmpty(beanMap)){
            //遍历bean map
            for (Map.Entry<Class<?>,Object> beanEntry : beanMap.entrySet()){
                //从bean map中获取bean类与bean实例
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                //获取bean类定义的所有成员变量
                Field[] beanFields = beanClass.getDeclaredFields();
                if (ArrayUtil.isNotEmpty(beanFields)){
                    //遍历bean field
                    for (Field beanField : beanFields){
                        //判断当前bean field是否带有Inject注解
                        if (beanField.isAnnotationPresent(Inject.class)){
                            //在bean map中获取bean field对应的实例
                            Class<?> beanFieldClass = beanField.getType();
                            Object beanFieldInstance = beanMap.get(beanFieldClass);
                            if (beanFieldInstance != null){
                                //通过反射初始化bean field的值
                                ReflectionUtil.setField(beanInstance,beanField,beanFieldInstance);
                            }
                        }
                    }
                }
            }
        }
    }

}
