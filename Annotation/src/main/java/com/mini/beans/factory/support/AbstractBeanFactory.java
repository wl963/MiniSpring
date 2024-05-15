package com.mini.beans.factory.support;

import com.mini.beans.factory.config.BeanDefinition;
import com.mini.beans.BeansException;
import com.mini.beans.factory.BeanFactory;
import com.mini.beans.factory.config.ConstructorArgumentValue;
import com.mini.beans.factory.config.ConstructorArgumentValues;
import com.mini.entity.PropertyValue;
import com.mini.entity.PropertyValues;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory,BeanDefinitionRegistry{

    private Map<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap<>();

    private Map<String,Object> earlySingletonObjects=new ConcurrentHashMap<>();

    private List<String> beanDefinitionNames = new ArrayList<>();
    public AbstractBeanFactory() {
    }

    //getBean，容器的核心方法
    public Object getBean(String beanName) throws BeansException {
        //先尝试直接拿Bean实例
        Object singleton = this.getSingleton(beanName);
        //如果此时还没有这个Bean的实例，则获取它的定义来创建实例
        if (singleton == null) {
            singleton=this.earlySingletonObjects.get(beanName);
            if (singleton == null) {
                BeanDefinition bd=beanDefinitions.get(beanName);
                singleton=createBean(bd);
                this.registerSingleton(beanName,singleton);
                //beanpostprocessor位置
                applyBeanPostProcessorBeforeInitialization(singleton,beanName);

                if(bd.getInitMethodName()!=null&&!bd.equals(""))
                {
                    invokeInitMethod(bd,singleton);
                }

                applyBeanPostProcessorAfterInitialization(singleton,beanName);
            }
        }
        return singleton;
    }

    private void invokeInitMethod(BeanDefinition bd,Object obj)
    {
        try {
            Class<?> clz=Class.forName(bd.getClassName());
            Method method=null;
            method= clz.getMethod(bd.getInitMethodName());
            method.invoke(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object createBean(BeanDefinition beanDefinition)
    {
        Class<?> clz=null;
        Object obj=doCreateBean(beanDefinition);
        this.earlySingletonObjects.put(beanDefinition.getId(),obj);

        try {
            clz=Class.forName(beanDefinition.getClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        handleProperties(beanDefinition,clz,obj);
        return obj;
    }

    private Object doCreateBean(BeanDefinition beanDefinition)
    {
        Class<?> clz=null;
        Object obj=null;
        Constructor<?> con=null;
        try {
            clz=Class.forName(beanDefinition.getClassName());
            ConstructorArgumentValues constructorArgumentValues =beanDefinition.getConstructorArgumentValues();
            if(!constructorArgumentValues.isEmpty())
            {
                Class<?>[] type=new Class<?>[constructorArgumentValues.getArgumentCount()];
                Object[] value=new Object[constructorArgumentValues.getArgumentCount()];
                for (int i = 0; i< constructorArgumentValues.getArgumentCount(); i++)
                {
                    ConstructorArgumentValue constructorArgumentValue = constructorArgumentValues.getIndexedArgumentValue(i);
                    if("String".equals(constructorArgumentValue.getType())||"java.lang.String".equals(constructorArgumentValue.getType()))
                    {
                        type[i]=String.class;
                        value[i]= constructorArgumentValue.getValue();
                    } else if ("Integer".equals(constructorArgumentValue.getType())||"java.lang.Integer".equals(constructorArgumentValue.getType())) {
                        type[i]=Integer.class;
                        value[i]= constructorArgumentValue.getValue();
                    } else if ("int".equals(constructorArgumentValue.getType())) {
                        type[i]=int.class;
                        value[i]=Integer.valueOf((String) constructorArgumentValue.getValue());
                    }else {
                        type[i]=String.class;
                        value[i]= constructorArgumentValue.getValue();
                    }
                }
                try {
                    con= clz.getConstructor(type);
                    obj=con.newInstance(value);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                } catch (SecurityException e) {
                    throw new RuntimeException(e);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }else {
                obj=clz.newInstance();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println(beanDefinition.getId()+"bean created."+beanDefinition.getClassName()+obj.toString());

        return obj;
    }

    private void handleProperties(BeanDefinition bd,Class<?> clz,Object obj)
    {
        System.out.println("handle properties for been :" + bd.getId());
        PropertyValues propertyValues=bd.getPropertyValues();
        if(!propertyValues.isEmpty())
        {
            for (int i=0;i< propertyValues.size();i++)
            {
                PropertyValue propertyValue=propertyValues.getPropertyValueList().get(i);
                String type=propertyValue.getType();
                String name=propertyValue.getName();
                Object value=propertyValue.getValue();
                boolean isRef=propertyValue.isIfRef();
                Class<?>[] pType=new Class[1];
                Object[] pValue=new Object[1];
                if(!isRef) {
                    if ("String".equals(type) || "java.lang.String".equals(type)) {
                        pType[0] = String.class;
                        pValue[0] = value;
                    } else if ("Integer".equals(type) || "java.lang.Integer".equals(type)) {
                        pType[0] = Integer.class;
                        pValue[0] = value;
                    } else if ("int".equals(type)) {
                        pType[0] = int.class;
                        pValue[0] = Integer.valueOf((String) value);
                    } else {
                        pType[0] = String.class;
                        pValue[0] = value;
                    }

                }
                else
                {
                    try{
                        pType[0]=Class.forName(type);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        pValue[0]=getBean((String) value);
                    } catch (BeansException e) {
                        throw new RuntimeException(e);
                    }
                }

                String methodName="set"+name.substring(0,1).toUpperCase()+name.substring(1);
                Method method=null;
                try {
                    method= clz.getMethod(methodName,pType);
                    method.invoke(obj,pValue);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
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

    public void refresh(){
        for (String beanName:beanDefinitionNames)
        {
            try {
                getBean(beanName);
            } catch (BeansException e) {
                throw new RuntimeException(e);
            }
        }
    }

    abstract public Object applyBeanPostProcessorBeforeInitialization(Object existingBean, String beanName) throws BeansException;

    abstract public Object applyBeanPostProcessorAfterInitialization(Object existingBean, String beanName) throws BeansException;
}
