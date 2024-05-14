package com.mini.beans;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory,BeanDefinitionRegistry{

    private Map<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap<>();

    private List<String> beanDefinitionNames = new ArrayList<>();
    public SimpleBeanFactory() {
    }

    //getBean，容器的核心方法
    public Object getBean(String beanName) throws BeansException{
        //先尝试直接拿Bean实例
        Object singleton = this.getSingleton(beanName);
        //如果此时还没有这个Bean的实例，则获取它的定义来创建实例
        if (singleton == null) {
            BeanDefinition beanDefinition=this.beanDefinitions.get(beanName);
            if (beanDefinition == null) {
                throw new BeansException("No Bean");
            }
            try {
                singleton=Class.forName(beanDefinition.getClassName()).newInstance();
                this.registerSingleton(beanName,singleton);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return singleton;
    }

    @Override
    public Boolean containsBean(String name) {
        return this.singletons.containsKey(name);
    }

    @Override
    public boolean isSingleton(String name) {
        return this.beanDefinitions.get(name).isSingleton();
    }

    @Override
    public boolean isPrototype(String name) {
        return this.beanDefinitions.get(name).isPrototype();
    }

    @Override
    public Class<?> getType(String name) {
        return this.beanDefinitions.get(name).getClass();
    }

    @Override
    public void registerBeanDefinition(String name, BeanDefinition bd) {
        this.beanDefinitions.put(name,bd);
        this.beanDefinitionNames.add(name);
        if(!bd.isLazyInit())
        {
            try {
                getBean(name);
            } catch (BeansException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void removeBeanDefinition(String name) {
        this.beanDefinitions.remove(name);
        this.beanDefinitionNames.remove(name);
        this.removeSingleton(name);
    }

    @Override
    public BeanDefinition getBeanDefinition(String name) {
        return this.beanDefinitions.get(name);
    }

    @Override
    public boolean containsBeanDefinition(String name) {
        return this.beanDefinitions.containsKey(name);
    }
}
