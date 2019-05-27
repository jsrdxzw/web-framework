package com.kyoshii.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @Author: xuzhiwei
 * @Date: 2019-05-27
 * @Description:
 */
public abstract class AspectProxy implements Proxy {
    private final static Logger LOGGER = LoggerFactory.getLogger(AspectProxy.class);

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;
        Class<?> targetClass = proxyChain.getTargetClass();
        Method targetMethod = proxyChain.getTargetMethod();
        Object[] methodParams = proxyChain.getMethodParams();
        begin();
        try {
            if (intercept(targetClass, targetMethod, methodParams)) {
                before(targetClass, targetMethod, methodParams);
                result = proxyChain.doProxyChain();
                after(targetClass, targetMethod, result);
            } else {
                result = proxyChain.doProxyChain();
            }
        } catch (Exception e) {
            LOGGER.error("proxy error", e);
            error(targetClass, targetMethod, methodParams, e);
        }
        end();
        return result;
    }

    public void begin() {

    }

    public void end() {

    }

    public boolean intercept(Class<?> cls, Method method, Object[] params) {
        return true;
    }

    public void before(Class<?> cls, Method method, Object[] params) {

    }

    public void after(Class<?> cls, Method method, Object result) {

    }

    public void error(Class<?> cls, Method method, Object[] params, Exception e) {

    }
}
