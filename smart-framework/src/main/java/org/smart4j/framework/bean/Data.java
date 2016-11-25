package org.smart4j.framework.bean;

/**
 * 返回数据对象，框架会将该对象写入HttpServletResponse对象中，从而直接输出至浏览器
 * Created by zhaoshiqiang on 2016/11/6.
 */
public class Data {
    //数据模型
    private Object model;

    public Data(Object model) {
        this.model = model;
    }

    public Object getModel() {
        return model;
    }
}