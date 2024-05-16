package com.mini.beans.factory;

import com.mini.beans.BeansException;

import java.util.Map;

public interface ListableBeanFactory extends BeanFactory{
    boolean containBeanDefinition(String beanName);
    int getBeanDefinitionCount();

    String[] getBeanDefinitionNames();

    String[] getBeanNamesForType(Class<?> type);

    <T> Map<String,T> getBeansOfType(Class<T> type) throws BeansException, ClassNotFoundException;
}
