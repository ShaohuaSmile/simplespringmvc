package shaohua.study.simplespringmvc.servlet;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述:
 *
 * @author Liush
 * @date 2017/11/13 9:57
 */
public class HandlerExecutionChain {
    private Object handler;

    private List<HandlerInterceptor> handlerInterceptorList;

    public Object getHandler() {
        return handler;
    }

    public void setHandler(Object handler) {
        this.handler = handler;
    }

    public List<HandlerInterceptor> getHandlerInterceptorList() {
        return handlerInterceptorList;
    }

    public void addHandlerInterceptorList(HandlerInterceptor handlerInterceptor) {
        if(handlerInterceptorList == null){
            handlerInterceptorList = new ArrayList<HandlerInterceptor>();
        }
        this.handlerInterceptorList.add(handlerInterceptor);
    }
}
