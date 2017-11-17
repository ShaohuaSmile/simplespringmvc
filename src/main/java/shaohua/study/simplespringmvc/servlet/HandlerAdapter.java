package shaohua.study.simplespringmvc.servlet;

import javax.servlet.http.HttpServletRequest;

/**
 * 功能描述:
 *
 * @author Liush
 * @date 2017/11/16 17:55
 */
public interface HandlerAdapter {
    Object handle(HttpServletRequest request, Object handler);
}
