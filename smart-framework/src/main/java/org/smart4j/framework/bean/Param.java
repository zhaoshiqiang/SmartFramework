package org.smart4j.framework.bean;

import org.smart4j.framework.util.CastUtil;
import org.smart4j.framework.util.CollectionUtil;

import java.util.Map;

/**
 * 请求参数对象
 * Created by zhaoshiqiang on 2016/11/6.
 */
public class Param {

    private Map<String,Object> paramMap;

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }
    //获取所有字段信息
    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public long getLong(String name){
        return CastUtil.castLong(paramMap.get(name));
    }

    public boolean isEmpty(){
        return CollectionUtil.isEmpty(paramMap);
    }
}