package com.kyoshii.bean;

import java.util.Map;

/**
 * @Author: xuzhiwei
 * @Date: 2019-05-26
 * @Description: 请求参数对象
 */
public class Param {
    private Map<String,Object> paramMap;

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }


}
