package com.mini.beans.factory.support;

import com.mini.beans.factory.config.SingletonBeanRegistry;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
    protected List<String> beanNames=new ArrayList<>();
    protected Map<String,Object> singletons= new ConcurrentHashMap<>(256);

    protected Map<String, Set<String>> dependentBeanMap = new ConcurrentHashMap<>(64);
    protected Map<String,Set<String>> dependenciesForBeanMap = new ConcurrentHashMap<>(64);
    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        synchronized (this.singletons){
            this.beanNames.add(beanName);
            this.singletons.put(beanName, singletonObject);
        }
    }

    @Override
    public Object getSingleton(String beanName) {
        return this.singletons.get(beanName);
    }

    @Override
    public boolean containsSingleton(String beanName) {
        return this.singletons.containsKey(beanName);
    }

    @Override
    public String[] getSingletonNames() {
        return (String[]) this.beanNames.toArray();
    }

    protected void removeSingleton(String beanName)
    {
        this.beanNames.remove(beanName);
        this.singletons.remove(beanName);
    }

    public void registerDependentBean(String beanName,String dependentBeanName)
    {
        Set<String> dependentBeans=this.dependentBeanMap.get(beanName);
        if(dependentBeans !=null &&dependentBeans.contains(dependentBeanName))
        {
            return;
        }

        synchronized (this.dependentBeanMap){
            dependentBeans=this.dependentBeanMap.get(beanName);
            if(dependentBeans==null){
                dependentBeans=new LinkedHashSet<String>(8);
                this.dependentBeanMap.put(beanName,dependentBeans);
            }
            dependentBeans.add(dependentBeanName);
        }

        synchronized (this.dependenciesForBeanMap){
            Set<String> dependenciesFoeBean=this.dependenciesForBeanMap.get(dependentBeanName);
            if(dependenciesFoeBean==null)
            {
                dependenciesFoeBean=new LinkedHashSet<String>(8);
                this.dependenciesForBeanMap.put(dependentBeanName,dependenciesFoeBean);
            }
            dependenciesFoeBean.add(beanName);
        }
    }

    public boolean hasDependentBean(String beanName)
    {
        return this.dependentBeanMap.containsKey(beanName);
    }
    public String[] getDependentBeans(String beanName)
    {
        Set<String> dependentBeans=this.dependentBeanMap.get(beanName);
        if(dependentBeans==null)
        {
            return  new String[0];
        }
        return (String[]) dependentBeans.toArray();
    }

    public String[] getDependenciesForBean(String beanName)
    {
        Set<String> dependenciesForBean = this.dependenciesForBeanMap.get(beanName);
        if (dependenciesForBean == null) {
            return new String[0];
        }
        return (String[]) dependenciesForBean.toArray();
    }

}
