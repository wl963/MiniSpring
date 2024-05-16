package com.mini.context;

import com.mini.beans.*;
import com.mini.beans.factory.BeanFactory;
import com.mini.beans.factory.ConfigurableListableBeanFactory;
import com.mini.beans.factory.config.BeanFactoryPostProcessor;
import com.mini.beans.factory.support.*;
import com.mini.beans.factory.support.AutowireCapableBeanFactory;
import com.mini.beans.factory.xml.XmlBeanDefinitionReader;
import com.mini.core.ClassPathXmlResource;
import com.mini.core.Resource;

import java.util.ArrayList;
import java.util.List;

public class ClassPathXmlApplicationContext extends AbstractApplicationContext{

    DefaultListableBeanFactory beanFactory;

    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors =
            new ArrayList<BeanFactoryPostProcessor>();

    public ClassPathXmlApplicationContext(String fileName){
        this(fileName,true);
    }

    public ClassPathXmlApplicationContext(String fileName,boolean isRefresh) {
        Resource resource=new ClassPathXmlResource(fileName);
        DefaultListableBeanFactory defaultListableBeanFactory=new DefaultListableBeanFactory();
        XmlBeanDefinitionReader definitionReader = new XmlBeanDefinitionReader(defaultListableBeanFactory);
        definitionReader.loadBeanDefinitions(resource);
        this.beanFactory=defaultListableBeanFactory;
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


    void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory)
    {
        beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }

    void onRefresh(){
        this.beanFactory.refresh();
    }



    @Override
    void registerListeners() {
        ApplicationListener listener = new ApplicationListener();
        this.getApplicationEventPublisher().addApplicationListener(listener);
    }

    @Override
    void initApplicationEventPublisher() {
        ApplicationEventPublisher aep = new SimpleApplicationEventPublisher();
        this.setApplicationEventPublisher(aep);
    }

    @Override
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {

    }

    @Override
    public void addApplicationListener(ApplicationListener listener) {
        this.getApplicationEventPublisher().addApplicationListener(listener);
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        this.getApplicationEventPublisher().publishEvent(event);
    }

    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return this.beanFactory;
    }

    @Override
    void finishRefresh() {
        publishEvent(new ContextRefreshEvent("Context Refreshed..."));
    }
}
