package com.mini.context;

import com.mini.beans.*;
import com.mini.core.ClassPathXmlResource;
import com.mini.core.Resource;

public class ClassPathXmlApplicationContext implements BeanFactory , ApplicationEventPublisher{

    private BeanFactory beanFactory;

    public ClassPathXmlApplicationContext(String fileName) {
        Resource resource=new ClassPathXmlResource(fileName);
        SimpleBeanFactory simpleBeanFactory=new SimpleBeanFactory();
        XmlBeanDefinitionReader definitionReader = new XmlBeanDefinitionReader(simpleBeanFactory);
        definitionReader.loadBeanDefinitions(resource);
        this.beanFactory=simpleBeanFactory;
    }

    @Override
    public Object getBean(String beanName) throws BeansException, ClassNotFoundException {
        return beanFactory.getBean(beanName);
    }

    @Override
    public Boolean containsBean(String name) {
        return null;
    }

    @Override
    public boolean isSingleton(String name) {
        return false;
    }

    @Override
    public boolean isPrototype(String name) {
        return false;
    }

    @Override
    public Class<?> getType(String name) {
        return null;
    }


    @Override
    public void publishEvent(ApplicationEvent event) {

    }
}
