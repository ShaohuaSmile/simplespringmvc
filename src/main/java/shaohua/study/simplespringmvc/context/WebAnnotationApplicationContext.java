package shaohua.study.simplespringmvc.context;

import shaohua.study.simplespringframework.annotation.Controller;
import shaohua.study.simplespringframework.beans.BeanDefinition;
import shaohua.study.simplespringframework.context.AbstractApplicationContext;
import shaohua.study.simplespringframework.context.AnnotationApplicationContext;
import shaohua.study.simplespringframework.factory.AutowireBeanFactory;
import shaohua.study.simplespringframework.factory.BeanFactory;
import shaohua.study.simplespringframework.reader.AnnotationBeanDefinitionReader;
import shaohua.study.simplespringframework.reader.BeanDefinitionReader;
import shaohua.study.simplespringframework.util.ClassUtil;

import javax.management.AttributeList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 功能描述:
 *
 * @author Liush
 * @date 2017/11/16 18:18
 */
public class WebAnnotationApplicationContext extends AbstractApplicationContext {
    protected final String[] scanPackages;
    protected final List<String> controllerBeanDefinitions;
    public WebAnnotationApplicationContext(String... scanPackages) throws IOException, ClassNotFoundException {
        this.scanPackages = scanPackages;
        controllerBeanDefinitions = new ArrayList<String>();
        this.beanFactory = new AutowireBeanFactory();
        this.refresh();
    }

    @Override
    public void refresh() throws IOException, ClassNotFoundException {
        BeanDefinitionReader reader = new AnnotationBeanDefinitionReader(scanPackages);
        reader.loadBeanDefinition();
        Map<String,BeanDefinition> registry =  reader.getRegistry();
        for(Map.Entry<String,BeanDefinition> entry : registry.entrySet()){
            BeanDefinition beanDefinition = entry.getValue();
            beanFactory.registerBeanDefinition(entry.getKey(),beanDefinition);
            if(ClassUtil.isAnnotated(beanDefinition.getBeanClass(), Controller.class)){
                controllerBeanDefinitions.add(entry.getKey());
            }
        }

    }

    public List<String> getControllerBeanDefinitions() {
        return controllerBeanDefinitions;
    }

}
