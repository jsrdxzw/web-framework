package com.kyoshii.helper;

import com.kyoshii.annotation.Action;
import com.kyoshii.bean.Handler;
import com.kyoshii.bean.Request;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: xuzhiwei
 * @Date: 2019-05-26
 * @Description: 控制器助手类
 */
public class ControllerHelper {
    private static final Map<Request, Handler> ACTION_MAP = new HashMap<>();

    // 初始化request和handler的映射关系
    static {
        System.out.println(123);
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if (controllerClassSet != null && !controllerClassSet.isEmpty()) {
            for (Class<?> controllerClass : controllerClassSet) {
                Method[] methods = controllerClass.getDeclaredMethods();
                if (methods.length > 0) {
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(Action.class)) {
                            // 获取方法上对应的annotation里面的参数
                            Action action = method.getAnnotation(Action.class);
                            // url地址等参数设置在该注解里面
                            String mapping = action.value();
                            // 形如post:/customers
                            if (mapping.matches("\\w+:/\\w*")) {
                                String[] split = mapping.split(":");
                                if (split.length == 2) {
                                    String requestMethod = split[0];
                                    String requestPath = split[1];
                                    Request request = new Request(requestMethod, requestPath);
                                    Handler handler = new Handler(controllerClass, method);
                                    ACTION_MAP.put(request, handler);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    public static Handler getHandler(String requestMethod, String requestPath) {
        Request request = new Request(requestMethod, requestPath);
        return ACTION_MAP.get(request);
    }
}
