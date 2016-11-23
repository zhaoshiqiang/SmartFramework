package org.smart4j.framework.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.Proxy.AspectProxy;
import org.smart4j.framework.Proxy.Proxy;
import org.smart4j.framework.Proxy.ProxyManager;
import org.smart4j.framework.annotation.Aspect;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Created by zhaoshiqiang on 2016/11/23.
 */
public final class AopHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(AopHelper.class);

    static {
        try {
            Map<Class<?>,Set<Class<?>>> proxyMap = createProxyMap();
            Map<Class<?>,List<Proxy>> targetMap = createTargetMap(proxyMap);
            for (Map.Entry<Class<?>,List<Proxy>> targetEntry : targetMap.entrySet()){
                Class<?> targetClass = targetEntry.getKey();
                List<Proxy> proxyList = targetEntry.getValue();
                //产生class对应的代理对象
                Object proxy = ProxyManager.createProxy(targetClass,proxyList);
                //将该代理对象重新放入BeanMap中
                BeanHelper.setBean(targetClass,proxy);
            }
        }catch (Exception e){
            LOGGER.error("aop failure", e);
        }
    }

    //获取由aspect指定的目标类
    private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Exception{
        Set<Class<?>> targetClassSet = new HashSet<Class<?>>();
        Class<? extends Annotation> annotation = aspect.value();
        //由Aspect通过注解来指定目标类，但是该目标类上的注解不能是Aspect注解
        if (annotation != null && !annotation.equals(aspect)){
            targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
        }
        return targetClassSet;
    }

    /**
     * 代理类需要扩展AspectProxy抽象类，还需要带有Aspect注解，只有满足这两个条件，才能依据Aspect注解中所定义
     * 的注解属性去获取该注解所对应的目标类集合，然后才能建立代理类与目标类集合之间的映射关系，最终返回这个映射关系
     */
    private static Map<Class<?>,Set<Class<?>>> createProxyMap() throws Exception{
        Map<Class<?>,Set<Class<?>>> proxyMap = new HashMap<Class<?>, Set<Class<?>>>();
        //获取抽象类AspectProxy的所有之类
        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);
        for (Class<?> proxyClass : proxyClassSet){
            //判断是否带有Aspect注解
            if (proxyClass.isAnnotationPresent(Aspect.class)){
                Aspect aspect = proxyClass.getAnnotation(Aspect.class);
                Set<Class<?>> targetClassSet = createTargetClassSet(aspect);    //获取目标类的集合
                proxyMap.put(proxyClass,targetClassSet);
            }
        }
        return proxyMap;
    }
    //根据代理类与目标类集合之间的映射关系，得出目标类与代理对象列表之间的映射关系
    private static Map<Class<?>,List<Proxy>> createTargetMap(Map<Class<?>,Set<Class<?>>> proxyMap) throws Exception{
        Map<Class<?>,List<Proxy>> targetMap = new HashMap<Class<?>, List<Proxy>>();
        for (Map.Entry<Class<?>,Set<Class<?>>> proxyEntry : proxyMap.entrySet()){
            Class<?> proxyClass = proxyEntry.getKey();
            Set<Class<?>> targetClassSet = proxyEntry.getValue();
            for (Class<?> targetClass : targetClassSet){
                Proxy proxy = (Proxy) proxyClass.newInstance();
                if (targetMap.containsKey(targetClass)){
                    targetMap.get(targetClass).add(proxy);
                }else {
                    List<Proxy> proxyList = new ArrayList<Proxy>();
                    proxyList.add(proxy);
                    targetMap.put(targetClass,proxyList);
                }
            }
        }
        return targetMap;
    }
}
