package com.kyoshii;

import com.kyoshii.bean.Handler;
import com.kyoshii.helper.BeanHelper;
import com.kyoshii.helper.ConfigHelper;
import com.kyoshii.helper.ControllerHelper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * @Author: xuzhiwei
 * @Date: 2019-05-26
 * @Description: 整个 Web 应用中，只有一个 Servlet
 * loadOnStartup 当值为0或者大于0时，表示容器在应用启动时就加载这个servlet；
 * 非负数的值越小，启动该servlet的优先级越高。0表示最高优先级
 */
@WebServlet(name = "DispatcherServlet", urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        HelperLoader.init();
        // ServletContext用来存放全局变量
        ServletContext servletContext = config.getServletContext();
        // 添加JSP路径映射，意思就是说/WEB-INF/view/下面的都交给jspServlet去处理
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath()+"*");

        // 注册处理静态资源的默认Servlet,路径是Js,css,图片等
        // 忽略所有的静态请求的方法
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath()+"*");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestMethod = request.getMethod().toLowerCase();
        String requestPath = request.getPathInfo();

        //获取Action处理器
        Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
        if (handler!=null){
            Class<?> controllerClass = handler.getControllerClass();
            Object bean = BeanHelper.getBean(controllerClass);
            HashMap<String, Object> paramMap = new HashMap<>(4);
            Enumeration<String> parameterNames = request.getParameterNames();
            // 获取url的参数
            while (parameterNames.hasMoreElements()){
                String paramName = parameterNames.nextElement();
                String paramValue = request.getParameter(paramName);
                paramMap.put(paramName,paramValue);
            }

        }

    }
}
