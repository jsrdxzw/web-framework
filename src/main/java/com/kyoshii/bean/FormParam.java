package com.kyoshii.bean;

/**
 * @Author: xuzhiwei
 * @Date: 2019-05-26
 * @Description: 封装表单参数
 */
public class FormParam {
    private String fieldName;
    private Object fieldValue;

    public  FormParam(String fieldName, Object fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public  String getFieldName() {
        return fieldName;
    }

    public  Object getFieldValue() {
        return fieldValue;
    }
}
