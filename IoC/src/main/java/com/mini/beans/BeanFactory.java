package com.mini.beans;

public interface BeanFactory {
    Object getBean(String beanName) throws BeansException, ClassNotFoundException;
    void registerBeanDefinition(BeanDefinition beanDefinition);
}
