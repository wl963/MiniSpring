package com.mini.beans.factory.support;

import com.mini.beans.BeansException;
import com.mini.beans.factory.ConfigurableListableBeanFactory;
import com.mini.beans.factory.config.BeanDefinition;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements ConfigurableListableBeanFactory {

    @Override
    public boolean containBeanDefinition(String beanName) {
        return this.beanDefinitions.containsKey(beanName);
    }

    @Override
    public int getBeanDefinitionCount(){
        return this.beanDefinitions.size();
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return (String[]) this.beanDefinitionNames.toArray();
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type) {
        List<String> result=new ArrayList<>();
        for (String beanName:this.beanDefinitionNames)
        {
            boolean matchFound =false;
            BeanDefinition mbd=this.getBeanDefinition(beanName);
            Class<?> classToMatch=mbd.getClass();
            if(type.isAssignableFrom(classToMatch))
            {
                matchFound=true;
            }
            else {
                matchFound=false;
            }
            if(matchFound)
            {
                result.add(beanName);
            }
        }
        return (String[]) result.toArray();
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException, ClassNotFoundException {
        String[] beanNames=getBeanNamesForType(type);
        Map<String,T> result=new LinkedHashMap<>(beanNames.length);
        for (String beanName:beanNames)
        {
            Object obj=getBean(beanName);
            result.put(beanName,(T)obj);
        }
        return result;
    }

}
