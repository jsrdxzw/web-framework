package com.kyoshii.aspect;

import com.kyoshii.annotation.Aspect;
import com.kyoshii.annotation.Service;
import com.kyoshii.proxy.AspectProxy;

@Aspect(Service.class)
public class CustomerProxy extends AspectProxy {
}
