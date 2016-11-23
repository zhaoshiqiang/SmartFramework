package org.smart4j.framework.Proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.annotation.Transaction;
import org.smart4j.framework.helper.DatabaseHelper;

import java.lang.reflect.Method;

/**
 * 事务代理
 * Created by zhaoshiqiang on 2016/11/23.
 */
public class TransactionProxy implements Proxy {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProxy.class);
    //本地线程变量，它是一个标志，可以保证同意线程中食物控制相关逻辑只会执行一次
    private static final ThreadLocal<Boolean> FLAG_HOLDER = new ThreadLocal<Boolean>(){
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };


    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;
        boolean flag = FLAG_HOLDER.get();
        Method method = proxyChain.getTargetMethod();
        if (!flag && method.isAnnotationPresent(Transaction.class)){
            FLAG_HOLDER.set(true);
            try {
                DatabaseHelper.beginTransaction();
                LOGGER.debug("begin transaction");
                result = proxyChain.doProxyChain();
                LOGGER.debug("commit transaction");
                DatabaseHelper.commitTransaction();
            }catch (Exception e){
                LOGGER.debug("rollback transaction");
                DatabaseHelper.rollbackTransaction();
            }finally {
                FLAG_HOLDER.remove();
            }
        }else {
            result = proxyChain.doProxyChain();
        }
        return result;
    }
}
