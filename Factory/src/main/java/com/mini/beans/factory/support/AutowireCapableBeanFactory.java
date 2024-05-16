package com.mini.beans.factory.support;

import com.mini.beans.BeansException;
import com.mini.beans.factory.BeanFactory;

import java.util.ArrayList;
import java.util.List;

public interface AutowireCapableBeanFactory  extends BeanFactory {
    int AUTOWIRE_NO = 0;
    int AUTOWIRE_BY_NAME = 1;
    int AUTOWIRE_BY_TYPE = 2;
    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean,
                                                       String beanName) throws BeansException, ClassNotFoundException;
    Object applyBeanPostProcessorsAfterInitialization(Object existingBean,
                                                      String beanName) throws BeansException;
}
