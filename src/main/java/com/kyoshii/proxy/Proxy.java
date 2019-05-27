package com.kyoshii.proxy;

/**
 * @Author: xuzhiwei
 * @Date: 2019-05-26
 * @Description:
 */
public interface Proxy {

    /**
     * 代理对应的类
     * @param proxyChain
     * @return
     * @throws Throwable
     */
    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
