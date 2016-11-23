package org.smart4j.framework;

import org.smart4j.framework.helper.*;
import org.smart4j.framework.util.ClassUtil;

/**
 * 加载相应的Helper类
 * Created by zhaoshiqiang on 2016/11/6.
 */
public final class HelperLoader {
    //当我们第一次访问类时，就好加载其static块，这里只是为了让加载更集中
    public static void init(){
        Class<?>[] classes = {
                ClassHelper.class,
                BeanHelper.class,
                AopHelper.class,    //Aophelper要在IocHelper之前加载，因为首先需要通过AopHelper获取代理对象，然后才能通过IocHelper进行依赖注入
                IocHelper.class,
                ControllerHelper.class
        };

        for (Class<?> cls : classes){
            ClassUtil.loadClass(cls.getName());
        }
    }
}
