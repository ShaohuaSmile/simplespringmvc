package shaohua.study.simplespringmvc.servlet;

import javax.servlet.http.HttpServletRequest;

/**
 * 功能描述: 封装Controller和请求路径url的对应关系
 *
 * @author Liush
 * @date 2017/11/16 20:51
 */
public interface HandlerMapping {
    /**
     * 功能描述: 获取Controller实例、以及拦截器列表
     *
     * @author Liush
     * @date 2017/11/16
     */
    HandlerExecutionChain getHanlder(HttpServletRequest request);

    /**
     * 功能描述: 注册controller
     *
     * @author Liush
     * @date 2017/11/16
     */
    void registerHanlder(String url,Object hanlder);
}
