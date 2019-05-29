package com.kyoshii;

import com.kyoshii.helper.*;
import com.kyoshii.util.ClassUtil;

/**
 * @Author: xuzhiwei
 * @Date: 2019-05-26
 * @Description: 统一加载helper类
 */
public class HelperLoader {
    public static void init() {
        // 加载类的时候要实例化一下,运行static的代码
        ClassUtil.loadClass(ClassHelper.class.getName(),true);
        ClassUtil.loadClass(BeanHelper.class.getName(),true);
        ClassUtil.loadClass(AopHelper.class.getName(),true);
        ClassUtil.loadClass(IocHelper.class.getName(),true);
        ClassUtil.loadClass(ControllerHelper.class.getName(),true);
    }
}
