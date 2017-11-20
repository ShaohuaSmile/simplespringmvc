package shaohua.study.simplespringmvc.servlet;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能描述: 封装Controller和请求路径url的对应关系
 *
 * @author Liush
 * @date 2017/11/20 9:54
 */
public class AnnotationHandlerMapping implements HandlerMapping {

    /** key -> url, value -> controller instance */
    protected Map<String,Object> handlerMap = new LinkedHashMap<String,Object>();

    protected List<HandlerInterceptor> interceptors = new ArrayList<HandlerInterceptor>();

    public HandlerExecutionChain getHanlder(HttpServletRequest request) {
        String uri = request.getRequestURI();
        Object handler = handlerMap.get(uri);
        if(handler == null){
            return null;
        }
        HandlerExecutionChain executionChain = new HandlerExecutionChain();
        executionChain.setHandler(handler);
        //todo 添加拦截器
        return executionChain;
    }

    public void registerHanlder(String url, Object hanlder) {
        handlerMap.put(url,hanlder);
    }
}
