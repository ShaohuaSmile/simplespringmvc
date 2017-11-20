package shaohua.study.simplespringmvc;

import shaohua.study.simplespringframework.annotation.Autowired;
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
    @Autowired
    private SimpleService simpleService;
    @RequestMapping("/hello")
    public String hello(Person person){
        return person.getUserName() +" say: " + simpleService.sayHello();
    }
}
