package shaohua.study.simplespringmvc;

import shaohua.study.simplespringframework.annotation.Service;

import java.util.Date;

/**
 * 功能描述:
 *
 * @author Liush
 * @date 2017/11/20 11:10
 */
@Service
public class SimpleService {
    public String sayHello(){
        return "Hello CRM boys and girls, welcome! " + new Date();
    }
}
