package com.kyoshii;

import com.kyoshii.helper.BeanHelper;
import com.kyoshii.helper.ClassHelper;
import com.kyoshii.helper.ControllerHelper;
import com.kyoshii.helper.IocHelper;
import com.kyoshii.util.ClassUtil;

/**
 * @Author: xuzhiwei
 * @Date: 2019-05-26
 * @Description: 统一加载helper类
 */
public class HelperLoader {
    public static void init() {
        ClassUtil.loadClass(ClassHelper.class.getName());
        ClassUtil.loadClass(BeanHelper.class.getName());
        ClassUtil.loadClass(IocHelper.class.getName());
        ClassUtil.loadClass(ControllerHelper.class.getName());
    }
}
