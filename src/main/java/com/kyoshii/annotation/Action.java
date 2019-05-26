package com.kyoshii.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ElementType.TYPE 注解作用于类型（类，接口，注解，枚举）
 * RetentionPolicy.RUNTIME 运行时保留，运行中可以处理
 * @Author: xuzhiwei
 * @Date: 2019-05-26
 * @Description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
    String value();
}
