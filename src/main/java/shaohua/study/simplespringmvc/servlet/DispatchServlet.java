package shaohua.study.simplespringmvc.servlet;

import shaohua.study.simplespringframework.context.ApplicationContext;
import shaohua.study.simplespringmvc.annotation.RequestMapping;
import shaohua.study.simplespringmvc.context.WebAnnotationApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.List;


/**
 * 功能描述:
 *
 * @author Liush
 * @date 2017/11/16 17:48
 */
public class DispatchServlet extends HttpServlet {

    protected ApplicationContext applicationContext;

    protected HandlerMapping handlerMapping;

    protected HandlerAdapter handlerAdapter;

    public void init(){
        try {
            //todo 扫包应该走配置文件
           applicationContext = new WebAnnotationApplicationContext("shaohua.study");
           initHandlerMapping();
           initHandlerAdapter();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initHandlerAdapter() {
        this.handlerAdapter = new AnnotationHandlerAdapter();
    }

    private void initHandlerMapping() throws Exception {
        this.handlerMapping = new AnnotationHandlerMapping();

        if(applicationContext == null){
            throw new RuntimeException("spring init error, ApplicationContext not found!");
        }
        if(applicationContext instanceof WebAnnotationApplicationContext){
            WebAnnotationApplicationContext webAnnotationApplicationContext = (WebAnnotationApplicationContext) applicationContext;
            List<String> controllerBeanNames =  webAnnotationApplicationContext.getControllerBeanDefinitions();
            for(String beanName : controllerBeanNames){

                //向容器获取controller实例
                Object obj = applicationContext.getBean(beanName);
                Class controllerClass = obj.getClass();
                if(!controllerClass.isAnnotationPresent(RequestMapping.class)){
                    continue;
                }

                //获取controller上的url
                RequestMapping requestMapping = (RequestMapping) controllerClass.getAnnotation(RequestMapping.class);
                String value = requestMapping.value();
                Method[] methods = controllerClass.getMethods();
                if(methods == null || methods.length < 1){
                    continue;
                }

                //遍历controller里的所有方法并获取方法上的url
                for(Method method:methods){
                    if(!method.isAnnotationPresent(RequestMapping.class)){
                       continue;
                    }
                    RequestMapping methodRequestMapping =  method.getAnnotation(RequestMapping.class);
                    String url = value + methodRequestMapping.value();
                    handlerMapping.registerHanlder(url,obj);
                }
            }
        }
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        doDispatch(request,response);
    }

    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
       //String uri = request.getRequestURI();
        Object handler = handlerMapping.getHanlder(request);

        Object result = handlerAdapter.handle(request,handler);

        //todo ModelAndView 渲染
        Writer writer = response.getWriter();
        writer.write(result.toString());

        return;
    }

    public void destroy(){

    }

}
