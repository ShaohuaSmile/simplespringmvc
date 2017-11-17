package shaohua.study.simplespringmvc;

import shaohua.study.simplespringframework.annotation.Controller;
import shaohua.study.simplespringmvc.annotation.RequestMapping;

/**
 * 功能描述:
 *
 * @author Liush
 * @date 2017/11/16 20:50
 */
@Controller
@RequestMapping("/springmvc/simple")
public class SimpleController {
    @RequestMapping("/hello")
    public String hello(){
        return "HElllll0";
    }
}
