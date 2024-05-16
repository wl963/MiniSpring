package com.mini.beans.factory.support;

import com.mini.beans.BeansException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory{
    private final List<BeanPostProcessor> beanPostProcessors=new ArrayList<>();

    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor)
    {
        beanPostProcessors.remove(beanPostProcessor);
        beanPostProcessors.add(beanPostProcessor);
    }

    public int getBeanPostProcessorCount() { return this.beanPostProcessors.size(); }
    public List<BeanPostProcessor> getBeanPostProcessors() { return this.beanPostProcessors; }


    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException, ClassNotFoundException {
        Object result=existingBean;
        for (BeanPostProcessor beanPostProcessor:getBeanPostProcessors())
        {
            beanPostProcessor.setBeanFactory(this);
            result=beanPostProcessor.postProcessBeforeInitialization(result,beanName);
            if(result==null)
            {
                return result;
            }
        }
        return result;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object result=existingBean;
        for(BeanPostProcessor beanPostProcessor:beanPostProcessors)
        {
            beanPostProcessor.setBeanFactory(this);
            result=beanPostProcessor.postProcessAfterInitialization(result,beanName);
            if(result==null)
            {
                return result;
            }
        }
        return result;
    }
}
