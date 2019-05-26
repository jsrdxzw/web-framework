package com.kyoshii.helper;

import com.kyoshii.annotation.Inject;
import com.kyoshii.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @Author: xuzhiwei
 * @Date: 2019-05-26
 * @Description: 依赖注入的助手类,获得的所有对象都是单例的
 */
public class IocHelper {
    static {
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (beanMap != null && !beanMap.isEmpty()) {
            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                Field[] beanFields = beanClass.getDeclaredFields();
                if (beanFields.length > 0) {
                    for (Field beanField : beanFields) {
                        // 开始搜索发现了就实例话
                        if (beanField.isAnnotationPresent(Inject.class)) {
                            Class<?> type = beanField.getType();
                            Object beanFieldInstance = beanMap.get(type);
                            if (beanFieldInstance != null) {
                                ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
                            }
                        }
                    }
                }
            }
        }
    }
}
