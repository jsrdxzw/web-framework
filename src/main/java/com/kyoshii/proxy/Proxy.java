package com.kyoshii.proxy;

/**
 * @Author: xuzhiwei
 * @Date: 2019-05-26
 * @Description:
 */
public interface Proxy {

    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
