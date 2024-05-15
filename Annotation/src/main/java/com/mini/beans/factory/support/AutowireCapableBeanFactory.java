package com.mini.beans.factory.support;

import com.mini.beans.BeansException;

import java.util.ArrayList;
import java.util.List;

public class AutowireCapableBeanFactory extends AbstractBeanFactory{
    private final List<AutowiredAnnotationBeanPostProcessor> beanPostProcessors=new ArrayList<>();

    public void addBeanPostProcessor(AutowiredAnnotationBeanPostProcessor beanPostProcessor)
    {
        beanPostProcessors.remove(beanPostProcessor);
        beanPostProcessors.add(beanPostProcessor);
    }

    public int getBeanPostProcessorCount() { return this.beanPostProcessors.size(); }
    public List<AutowiredAnnotationBeanPostProcessor> getBeanPostProcessors() { return this.beanPostProcessors; }


    @Override
    public Object applyBeanPostProcessorBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result=existingBean;
        for (AutowiredAnnotationBeanPostProcessor beanPostProcessor:beanPostProcessors)
        {
            beanPostProcessor.setBeanFactory(this);
            result=beanPostProcessor.postProcessBeforeInitialization(existingBean,beanName);
            if(result==null)
            {
                return result;
            }
        }
        return result;
    }

    @Override
    public Object applyBeanPostProcessorAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object result=existingBean;
        for(AutowiredAnnotationBeanPostProcessor beanPostProcessor:beanPostProcessors)
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
