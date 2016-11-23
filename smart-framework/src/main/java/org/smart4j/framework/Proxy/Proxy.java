package org.smart4j.framework.Proxy;

/**
 * 代理接口
 * Created by zhaoshiqiang on 2016/11/23.
 */
public interface Proxy {
    //执行链式代理
    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
