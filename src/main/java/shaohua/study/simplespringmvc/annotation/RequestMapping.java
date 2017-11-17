package shaohua.study.simplespringmvc.annotation;

import java.lang.annotation.*;

/**
 * 功能描述:
 *
 * @author Liush
 * @date 2017/11/16 18:50
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    String value() default "";
}
