package org.smart4j.framework.Proxy;

import net.sf.cglib.proxy.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 切面代理
 * Created by zhaoshiqiang on 2016/11/23.
 */
public abstract class AspectProxy implements Proxy{
    private static final Logger logger = LoggerFactory.getLogger(AspectProxy.class);

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;
        Class<?> cls = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getMethodParams();

        begin();
        try {

            if (intercept(cls,method,params)){
                before(cls,method,params);
                result = proxyChain.doProxyChain();
                after(cls,method,params,result);
            }else {
                result = proxyChain.doProxyChain();
            }

        }catch (Exception e){
            logger.error("proxy failure", e);
            error(cls,method,params,e);
            throw e;
        }finally {
            end();
        }

        return result;
    }

    public boolean intercept(Class<?> cls, Method method, Object[] params) throws Throwable{
        return true;
    }

    public void begin(){}
    public void end(){}
    public void before(Class<?> cls,Method method,Object[] params)throws Throwable{}
    public void after(Class<?> cls,Method method,Object[] params,Object result)throws Throwable{}
    public void error(Class<?> cls,Method method,Object[] params,Throwable e){}
}
