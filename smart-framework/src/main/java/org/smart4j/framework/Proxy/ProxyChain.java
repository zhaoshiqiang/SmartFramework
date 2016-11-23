package org.smart4j.framework.Proxy;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 代理链
 * Created by zhaoshiqiang on 2016/11/23.
 */
public class ProxyChain {
    private final Class<?> targetClass; //目标类
    private final Object targetObject;  //目标对象
    private final Method targetMethod;  //目标方法
    private final MethodProxy methodProxy;  //方法代理，这是CGLib开源项目提供的一个方法代理对象
    private final Object[] methodParams;    //方法参数


    private List<Proxy> proxyList = new ArrayList<Proxy>();
    private int proxyIndex = 0;

    public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod, MethodProxy methodProxy, Object[] methodParams, List<Proxy> proxyList) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodProxy = methodProxy;
        this.methodParams = methodParams;
        this.proxyList = proxyList;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }
    /*
        * 通过proxyIndex来充当代理对象的计数器，若尚未达到proxyList的上限，则从proxyList中取出相应的Proxy对象，
        * 并调用doProxy方法。在Proxy接口的实现中会提供相应的横切逻辑，并调用doProxyChain方法，
        * 随后将再次调用当前ProxyChain对象的doProxyChain方法，直到proxyIndex达到proxyList的上限位置，最后调用methodPrxoy的invokeSuper方法，执行目标对象的业务逻辑
        * */
    public Object doProxyChain() throws Throwable{
        Object methodResult;
        if (proxyIndex < proxyList.size()){
            methodResult = proxyList.get(proxyIndex++).doProxy(this);
        }else {
            methodResult = methodProxy.invokeSuper(targetObject,methodParams);
        }
        return methodResult;
    }
}
