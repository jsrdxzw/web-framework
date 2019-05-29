package com.kyoshii.helper;

import com.kyoshii.annotation.Aspect;
import com.kyoshii.proxy.AspectProxy;
import com.kyoshii.proxy.Proxy;
import com.kyoshii.proxy.ProxyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * AOP 方法拦截助手类
 */
public class AopHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(AopHelper.class);


    static {
        Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
        try {
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
            for (Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()) {
                Class<?> targetClass = targetEntry.getKey();
                List<Proxy> proxyList = targetEntry.getValue();
                Object proxy = ProxyManager.createProxy(targetClass, proxyList);
                BeanHelper.setBean(targetClass, proxy);
            }
        } catch (IllegalAccessException | InstantiationException e) {
            LOGGER.error("aop init failure", e);
        }
    }

    /**
     * @param aspect
     * @return 要代理的目标类集合
     */
    private static Set<Class<?>> createTargetClassSet(Aspect aspect) {
        Set<Class<?>> targetClassSet = new HashSet<>();
        Class<? extends Annotation> annotation = aspect.value();
        // 获取在Aspect注解中的所有注解
        if (!annotation.equals(Aspect.class)) {
            targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
        }
        return targetClassSet;
    }

    /**
     * 获取代理类和目标类集合之间的映射关系
     * @return
     */
    public static Map<Class<?>, Set<Class<?>>> createProxyMap() {
        Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<>();
        // 获取所有的代理Class,要求既要继承自AspectProxy又要有@Aspect的注解
        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);
        for (Class<?> proxyClass : proxyClassSet) {
            if (proxyClass.isAnnotationPresent(Aspect.class)) {
                Aspect annotation = proxyClass.getAnnotation(Aspect.class);
                Set<Class<?>> targetClassSet = createTargetClassSet(annotation);
                proxyMap.put(proxyClass, targetClassSet);
            }
        }
        return proxyMap;
    }

    /**
     * 目标类与代理对象列表之间的映射关系
     *
     * @param proxyMap
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> proxyMap) throws IllegalAccessException, InstantiationException {
        Map<Class<?>, List<Proxy>> targetMap = new HashMap<>();
        for (Map.Entry<Class<?>, Set<Class<?>>> proxyEntry : proxyMap.entrySet()) {
            Class<?> proxyClass = proxyEntry.getKey(); // 代理类
            Set<Class<?>> targetClassSet = proxyEntry.getValue(); //目标类集合
            for (Class<?> targetClass : targetClassSet) { //遍历目标类
                Proxy proxy = (Proxy) proxyClass.newInstance();
                if (targetMap.containsKey(targetClass)) {
                    targetMap.get(targetClass).add(proxy);
                } else {
                    List<Proxy> proxyList = new ArrayList<>();
                    proxyList.add(proxy);
                    targetMap.put(targetClass, proxyList);
                }
            }
        }
        return targetMap;
    }

}
