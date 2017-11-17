package shaohua.study.simplespringmvc.servlet;

import shaohua.study.simplespringframework.annotation.Controller;
import shaohua.study.simplespringframework.util.ClassUtil;
import shaohua.study.simplespringmvc.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述:
 *
 * @author Liush
 * @date 2017/11/16 20:01
 */
public class AnnotationHandlerAdapter implements HandlerAdapter {

    private Map<String,Method> urlMethodCache = new HashMap<String,Method>();

    public Object handle(HttpServletRequest request,Object handler) {
        Class controllerClass = handler.getClass();
        if(!ClassUtil.isAnnotated(controllerClass, Controller.class)){
            throw new RuntimeException("error controller "+controllerClass.getName());
        }
        String uri = request.getRequestURI();
        Method method = urlMethodCache.get(uri);
        if(method == null){
            RequestMapping requestMapping = (RequestMapping) controllerClass.getAnnotation(RequestMapping.class);
            String controllerUri = requestMapping.value();
            Method[] methods = controllerClass.getMethods();
            if(methods != null && methods.length > 0){
                for(Method m:methods){
                    if(!m.isAnnotationPresent(RequestMapping.class)){
                        continue;
                    }
                    RequestMapping methodRequestMapping =  m.getAnnotation(RequestMapping.class);
                    String methodUri = methodRequestMapping.value();
                    if(uri.equals(controllerUri+methodUri)){
                        method = m;
                        urlMethodCache.put(uri,method);
                        break;
                    }
                }
            }

        }
        try {
            return method.invoke(handler);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("error invoke "+uri, e);

        } catch (InvocationTargetException e) {
            throw new RuntimeException("error controller "+uri, e);
        }
    }
}
