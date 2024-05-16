package com.mini.context;

import com.mini.beans.BeansException;
import com.mini.beans.factory.ConfigurableBeanFactory;
import com.mini.beans.factory.ConfigurableListableBeanFactory;
import com.mini.beans.factory.ListableBeanFactory;
import com.mini.beans.factory.config.BeanFactoryPostProcessor;
import com.mini.core.env.Environment;
import com.mini.core.env.EnvironmentCapable;

public interface ApplicationContext
        extends EnvironmentCapable, ListableBeanFactory,
        ConfigurableBeanFactory, ApplicationEventPublisher{
    String getApplicationName();
    long getStartupDate();
    ConfigurableListableBeanFactory getBeanFactory() throws
            IllegalStateException;
    void setEnvironment(Environment environment);
    Environment getEnvironment();
    void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor);
    void refresh() throws BeansException, IllegalStateException;
    void close();
    boolean isActive();
}
