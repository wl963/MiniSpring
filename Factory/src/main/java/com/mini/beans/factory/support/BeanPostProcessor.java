package com.mini.beans.factory.support;

import com.mini.beans.BeansException;
import com.mini.beans.factory.BeanFactory;

public interface BeanPostProcessor {
    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException, ClassNotFoundException;

    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;

    void setBeanFactory(BeanFactory abstractAutowireCapableBeanFactory);
}
