package com.kyoshii;

import com.kyoshii.bean.*;
import com.kyoshii.helper.BeanHelper;
import com.kyoshii.helper.ConfigHelper;
import com.kyoshii.helper.ControllerHelper;
import com.kyoshii.util.JsonUtil;
import com.kyoshii.util.ReflectionUtil;
import com.kyoshii.util.StringUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: xuzhiwei
 * @Date: 2019-05-26
 * @Description: 整个 Web 应用中，只有一个 Servlet
 * loadOnStartup 当值为0或者大于0时，表示容器在应用启动时就加载这个servlet；
 * 非负数的值越小，启动该servlet的优先级越高。0表示最高优先级
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    private static final String IGNORE_URL = "favicon.ico";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        HelperLoader.init();
        // ServletContext用来存放全局变量
        ServletContext servletContext = config.getServletContext();
        // 添加JSP路径映射，意思就是说/WEB-INF/view/下面的都交给jspServlet去处理
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");

        // 注册处理静态资源的默认Servlet,路径是Js,css,图片等
        // 忽略所有的静态请求的方法
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");

    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.service(request, response);
        String requestMethod = request.getMethod().toLowerCase();
        String requestPath = request.getPathInfo();
        if (IGNORE_URL.equals(requestPath)) {
            return;
        }
        //获取Action处理器
        Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
        if (handler != null) {
            Class<?> controllerClass = handler.getControllerClass();
            Object bean = BeanHelper.getBean(controllerClass);
            HashMap<String, Object> paramMap = new HashMap<>(4);
            Enumeration<String> parameterNames = request.getParameterNames();
            // 获取url的参数
            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                String paramValue = request.getParameter(paramName);
                paramMap.put(paramName, paramValue);
            }
//            String body = URLDecoder.decode(StreamUtil.getString(request.getInputStream()), "utf-8");
//            if (StringUtil.isNotEmpty(body)){
//                String[] params = StringUtil.splitString(body, "&");
//                if (params.length > 0){
//                    for (String param : params) {
//                        String[] array = StringUtil.splitString(param, "=");
//                        if (array.length==2){
//                            String paramName = array[0];
//                            String paramValue = array[1];
//                            paramMap.put(paramName,paramValue);
//                        }
//                    }
//                }
//            }
            Param param = new Param(paramMap);
            // 调用Action的方法
            Method actionMethod = handler.getActionMethod();
            Object result = ReflectionUtil.invokeMethod(bean, actionMethod, param);
            if (result instanceof View) {
                View view = (View) result;
                String path = view.getPath();
                if (StringUtil.isNotEmpty(path)) {
                    if (path.startsWith("/")) {
                        response.sendRedirect(request.getContextPath() + path);
                    } else {
                        Map<String, Object> model = view.getModel();
                        for (Map.Entry<String, Object> entry : model.entrySet()) {
                            request.setAttribute(entry.getKey(), entry.getValue());
                        }
                        request.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(request, response);
                    }
                }
            } else if (result instanceof Data) {
                Data data = (Data) result;
                Object model = data.getModel();
                if (model != null) {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    String json = JsonUtil.toJson(model);
                    PrintWriter writer = response.getWriter();
                    writer.write(json);
                    writer.flush();
                    writer.close();
                }
            }
        }
    }

    public static void main(String[] args) {
        String s = JsonUtil.toJson(new Request("user", "xuzhiwei"));
        System.out.println(s);
    }
}
