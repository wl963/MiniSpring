package com.mini.context;

import com.mini.beans.*;
import com.mini.beans.factory.BeanFactory;
import com.mini.beans.factory.support.AutowireCapableBeanFactory;
import com.mini.beans.factory.support.AutowireCapableBeanFactory;
import com.mini.beans.factory.support.AutowiredAnnotationBeanPostProcessor;
import com.mini.beans.factory.xml.XmlBeanDefinitionReader;
import com.mini.core.ClassPathXmlResource;
import com.mini.core.Resource;

import java.util.List;

public class ClassPathXmlApplicationContext implements BeanFactory, ApplicationEventPublisher{

    private AutowireCapableBeanFactory beanFactory;

    public ClassPathXmlApplicationContext(String fileName){
        this(fileName,true);
    }

    public ClassPathXmlApplicationContext(String fileName,boolean isRefresh) {
        Resource resource=new ClassPathXmlResource(fileName);
        AutowireCapableBeanFactory autoBeanFactory=new AutowireCapableBeanFactory();
        XmlBeanDefinitionReader definitionReader = new XmlBeanDefinitionReader(autoBeanFactory);
        definitionReader.loadBeanDefinitions(resource);
        this.beanFactory=autoBeanFactory;
        if(isRefresh)
        {
            try {
                refresh();
            } catch (BeansException e) {
                throw new RuntimeException(e);
            }
        }
    }

//    public List getBeanFactoryPostProcessors() { return this.beanFactoryPostProcessors; }
//    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor) { this.beanFactoryPostProcessors.add(postProcessor); }

    public void refresh() throws BeansException,IllegalStateException{
        registerBeanPostProcessors(this.beanFactory);
        onRefresh();
    }

    private void registerBeanPostProcessors(AutowireCapableBeanFactory beanFactory)
    {
        beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }

    private void onRefresh(){
        this.beanFactory.refresh();
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
