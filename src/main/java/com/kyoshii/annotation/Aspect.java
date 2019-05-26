package com.kyoshii.annotation;

import java.lang.annotation.*;

/**
 * @Author: xuzhiwei
 * @Date: 2019-05-26
 * @Description: 这是一个切面编程的注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    Class<? extends Annotation> value();
}
