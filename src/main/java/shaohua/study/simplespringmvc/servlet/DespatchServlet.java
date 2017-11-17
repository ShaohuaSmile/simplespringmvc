package shaohua.study.simplespringmvc.servlet;

import shaohua.study.simplespringframework.context.AbstractApplicationContext;
import shaohua.study.simplespringframework.context.AnnotationApplicationContext;
import shaohua.study.simplespringframework.context.ApplicationContext;
import shaohua.study.simplespringframework.factory.BeanFactory;
import shaohua.study.simplespringmvc.annotation.RequestMapping;
import shaohua.study.simplespringmvc.context.WebAnnotationApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能描述:
 *
 * @author Liush
 * @date 2017/11/16 17:48
 */
public class DespatchServlet extends HttpServlet {

    protected ApplicationContext applicationContext;
    protected Map<String,Object> handlerMappings = new LinkedHashMap<String,Object>();

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
        if(applicationContext == null){
            throw new RuntimeException("spring init error, ApplicationContext not found!");
        }
        if(applicationContext instanceof WebAnnotationApplicationContext){
            WebAnnotationApplicationContext webAnnotationApplicationContext = (WebAnnotationApplicationContext) applicationContext;
            List<String> controllerBeanNames =  webAnnotationApplicationContext.getControllerBeanDefinitions();
            for(String beanName : controllerBeanNames){
                Object obj = applicationContext.getBean(beanName);
                Class controllerClass = obj.getClass();
                if(!controllerClass.isAnnotationPresent(RequestMapping.class)){
                    continue;
                }
                RequestMapping requestMapping = (RequestMapping) controllerClass.getAnnotation(RequestMapping.class);
                String value = requestMapping.value();
                Method[] methods = controllerClass.getMethods();
                if(methods == null || methods.length < 1){
                    continue;
                }
                for(Method method:methods){
                    if(!method.isAnnotationPresent(RequestMapping.class)){
                       continue;
                    }
                    RequestMapping methodRequestMapping =  method.getAnnotation(RequestMapping.class);
                    String url = value + methodRequestMapping.value();
                    handlerMappings.put(url,obj);
                }
            }
        }
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        String uri = request.getRequestURI();
        Object handler = handlerMappings.get(uri);
        Object result = handlerAdapter.handle(request,handler);
        Writer writer = response.getWriter();
        writer.write(result.toString());
        return;
    }

    public void destroy(){

    }

}
