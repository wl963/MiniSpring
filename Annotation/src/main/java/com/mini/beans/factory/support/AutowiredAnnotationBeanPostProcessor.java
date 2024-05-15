package com.mini.beans.factory.support;

import com.mini.beans.BeansException;
import com.mini.beans.factory.BeanFactory;
import com.mini.beans.factory.annptation.Autowired;

import java.lang.reflect.Field;

public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor{
    private AutowireCapableBeanFactory beanFactory;


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Object result=bean;
        Class<?> clz=bean.getClass();
        Field[] fields = clz.getDeclaredFields();
        for (Field field:fields)
        {
            boolean isAutowired=field.isAnnotationPresent(Autowired.class);
            if(isAutowired)
            {
                String fieldName= field.getName();
                Object autowiredObj=this.getBeanFactory().getBean(fieldName);
                try {
                    field.setAccessible(true);
                    field.set(bean,autowiredObj);
                    System.out.println("autowie"+fieldName+"for bean"+beanName);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return result;
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    public AutowireCapableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory=beanFactory;
    }
}
