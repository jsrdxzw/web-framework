package com.kyoshii.bean;

import java.lang.reflect.Method;

/**
 * @Author: xuzhiwei
 * @Date: 2019-05-26
 * @Description: 封装Action的信息
 */
public class Handler {
    private Class<?> controllerClass;
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
