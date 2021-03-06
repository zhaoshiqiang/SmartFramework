package org.smart4j.framework;

import org.smart4j.framework.bean.Data;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.bean.View;
import org.smart4j.framework.helper.*;
import org.smart4j.framework.util.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by zhaoshiqiang on 2016/11/6.
 */

/**
 * loadOnStartup标记容器是否在启动的时候就加载这个servlet。
 * 当值为0或者大于0时，表示容器在应用启动时就加载这个servlet；
 * 当是一个负数时或者没有指定时，则指示容器在该servlet被选择时才加载。
 * 正数的值越小，启动该servlet的优先级越高。
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        //初始化相关Helper类
        HelperLoader.init();
        //获取ServletContext对象（用于注册servlet）
        ServletContext servletContext = servletConfig.getServletContext();
        //注册处理jsp的servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");
        //注册处理静态资源的默认servlet
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");

        UploadHelper.init(servletContext);
    }

    /**
     *Servlet程序是由WEB服务器调用，web服务器收到客户端的Servlet访问请求后：
     　　①Web服务器首先检查是否已经装载并创建了该Servlet的实例对象。如果是，则直接执行第④步，否则，执行第②步。
     　　②装载并创建该Servlet的一个实例对象。
     　　③调用Servlet实例对象的init()方法。
     　　④创建一个用于封装HTTP请求消息的HttpServletRequest对象和一个代表HTTP响应消息的HttpServletResponse对象，
     然后调用Servlet的service()方法并将请求和响应对象作为参数传递进去。（默认情况下，无论你是get还是post 提交过来 都会经过service（）方法来处理，
     然后service来控制是转向doGet或是doPost方法）
     　　⑤WEB应用程序被停止或重新启动之前，Servlet引擎将卸载Servlet，并在卸载之前调用Servlet的destroy()方法。
     */
    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletHelper.init(req,resp);
        try {
            //获取请求方法与请求路径
            String requestMethod = req.getMethod().toLowerCase();
            String requestPath = req.getPathInfo();
            //获取Action处理器
            Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
            if (handler != null){
                //获取Controller类及其bean实例
                Class<?> controllerClass = handler.getControllerClass();
                Object controllerBean = BeanHelper.getBean(controllerClass);

                //创建请求参数对象
                Param param = null;
                if (UploadHelper.isMultipart(req)){
                    param = UploadHelper.createParam(req);
                }else {
                    param = RequestHelper.createParam(req);
                }

                //调用action方法
                Method actionMethod = handler.getActionMethod();
                Object result;
                if (param.isEmpty()){
                    result = ReflectionUtil.invokeMethod(controllerBean,actionMethod);
                }else {
                    result= ReflectionUtil.invokeMethod(controllerBean,actionMethod,param);
                }

                //处理action方法返回值
                if (result instanceof View){
                    //返回jsp页面
                    handleViewResult((View) result,req,resp);
                }else if (result instanceof Data){
                    //返回json数据
                    handleDataResult((Data)result,resp);
                }
            }
        }finally {
            ServletHelper.destory();
        }
    }



    private void handleViewResult(View view,HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
        String path = view.getPath();
        if(StringUtil.isNotEmpty(path)){
            if (path.startsWith("/")){
                response.sendRedirect(request.getContextPath() + path);
            }else {
                Map<String,Object> model = view.getModel();
                for (Map.Entry<String,Object> entry : model.entrySet()){
                    request.setAttribute(entry.getKey(),entry.getValue());
                }
                request.getRequestDispatcher(ConfigHelper.getAppJspPath()+path).forward(request,response);
            }
        }
    }

    private void handleDataResult(Data data,HttpServletResponse response) throws IOException {
        Object model = data.getModel();
        if (model != null){
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF8");
            PrintWriter writer = response.getWriter();
            String json = JsonUtil.toJson(model);
            writer.write(json);
            writer.flush();
            writer.close();
        }
    }

}