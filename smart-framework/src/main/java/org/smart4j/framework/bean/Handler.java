package org.smart4j.framework.bean;

import java.lang.reflect.Method;

/**
 * 封装action信息
 * Created by zhaoshiqiang on 2016/11/6.
 */
public class Handler {

    // Controller 类
    private Class<?> controllerClass;
    //action方法
    private Method actionMethod;

    public Handler(Class<?> controllerClass, Method actionMethod) {
        this.controllerClass = controllerClass;
        this.actionMethod = actionMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }
}
