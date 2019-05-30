package com.kyoshii.proxy;

import com.kyoshii.annotation.Transaction;
import com.kyoshii.helper.DatabaseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class TransactionProxy implements Proxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProxy.class);

    @Override
    synchronized public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;
        Method targetMethod = proxyChain.getTargetMethod();
        if (targetMethod.isAnnotationPresent(Transaction.class)) {
            try {
                DatabaseHelper.beginTransaction();
                result = proxyChain.doProxyChain();
                DatabaseHelper.commitTransaction();
            } catch (Exception e) {
                DatabaseHelper.rollbackTransaction();
                LOGGER.error("rollback transaction", e);
            }
        } else {
            result = proxyChain.doProxyChain();
        }
        return result;
    }
}
