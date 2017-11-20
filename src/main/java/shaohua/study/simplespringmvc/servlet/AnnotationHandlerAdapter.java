package shaohua.study.simplespringmvc.servlet;

import shaohua.study.simplespringframework.annotation.Controller;
import shaohua.study.simplespringframework.util.ClassUtil;
import shaohua.study.simplespringmvc.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述: 
 *
 * @author Liush
 * @date 2017/11/16 20:01
 */
public class AnnotationHandlerAdapter implements HandlerAdapter {

    /** key -> url, value -> method */
    private Map<String,Method> urlMethodCache = new HashMap<String,Method>();

    /**
     * 功能描述: 调用controller方法
     *
     * @author Liush
     * @date 2017/11/20
     */
    public Object handle(HttpServletRequest request,Object handlerExcutionChain) {
        HandlerExecutionChain chain = (HandlerExecutionChain) handlerExcutionChain;
        Object handler = chain.getHandler();
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
            //todo 参数处理
            Object[] args = getInvokerArgs(method,request);

            return method.invoke(handler,args);
        } catch (Exception e) {
            throw new RuntimeException("error invoke "+uri, e);
        }
    }

    private Object[] getInvokerArgs(Method method, HttpServletRequest request) throws IllegalAccessException, InstantiationException {
        //参数类型
        Class[] paramTypes = method.getParameterTypes();
        //Parameter[] parameters = method.getParameters();
        //TypeVariable<Method>[] typeParameters = method.getTypeParameters();
        //参数值
        Object[] args = new Object[paramTypes.length];

        //逐个参数处理
        for(int i = 0; i < args.length; i++){
            //String name = typeParameters[i].getName();
            //todo basic type
            //todo annotation @PathVariable @RequestParam

            //Common Object 参数为引用类型
            Class paramClass = paramTypes[i];
            args[i] = paramClass.newInstance();
            Field[] fields = paramClass.getDeclaredFields();

            //为引用类型属性赋值
            for(Field field:fields){
                Class fieldType = field.getType();
                //暂时只处理基本类型
                if(fieldType.isPrimitive() || String.class.isAssignableFrom(fieldType)){
                    String fieldName = field.getName();
                    String fieldValue = request.getParameter(fieldName);
                    //暂时只处理String
                    Object value = null;
                    if(String.class.isAssignableFrom(fieldType)){
                        value = fieldValue;
                    }else if(int.class.isAssignableFrom(fieldType)){
                        fieldValue = fieldValue == null ? "0":fieldValue;
                        value = Integer.valueOf(fieldValue);
                    }

                    field.setAccessible(true);
                    field.set(args[i],value);
                }
            }
        }
        return args;
    }
}
