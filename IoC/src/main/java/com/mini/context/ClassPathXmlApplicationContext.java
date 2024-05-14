package com.mini.context;

import com.mini.beans.*;
import com.mini.core.ClassPathXmlResource;
import com.mini.core.Resource;

public class ClassPathXmlApplicationContext implements BeanFactory {

    private BeanFactory beanFactory;

    public ClassPathXmlApplicationContext(String fileName) {
        Resource resource=new ClassPathXmlResource(fileName);
        BeanFactory beanFactory=new SimpleBeanFactory();
        XmlBeanDefinitionReader definitionReader = new XmlBeanDefinitionReader(beanFactory);
        definitionReader.loadBeanDefinitions(resource);
        this.beanFactory=beanFactory;
    }

    @Override
    public Object getBean(String beanName) throws BeansException, ClassNotFoundException {
        return beanFactory.getBean(beanName);
    }

    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        this.beanFactory.registerBeanDefinition(beanDefinition);
    }
}
