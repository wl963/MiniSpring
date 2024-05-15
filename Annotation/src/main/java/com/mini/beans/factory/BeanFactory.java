package com.mini.beans.factory;

import com.mini.beans.BeansException;

public interface BeanFactory {
    Object getBean(String beanName) throws BeansException, ClassNotFoundException;
    Boolean containsBean(String name);

    boolean isSingleton(String name);

    boolean isPrototype(String name);
    Class<?> getType(String name);
}
