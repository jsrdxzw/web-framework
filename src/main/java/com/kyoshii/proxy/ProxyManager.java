package com.kyoshii.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.util.List;

/**
 * @Author: xuzhiwei
 * @Date: 2019-05-26
 * @Description: 代理管理器
 */
public class ProxyManager {

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(Class<?> targetClass, List<Proxy> proxyList) {
        return (T) Enhancer.create(
                targetClass,
                (MethodInterceptor) (targetObject, targetMethod, methodParams, methodProxy) ->
                        new ProxyChain(targetClass, targetObject, targetMethod, methodProxy, methodParams, proxyList)
        );
    }
}
