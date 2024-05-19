package com.minis.web;

import com.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.config.ConfigurableListableBeanFactory;
import com.minis.beans.factory.support.DefaultListableBeanFactory;
import com.minis.context.*;
import jakarta.servlet.ServletContext;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AnnotationConfigWebApplicationContext extends AbstractApplicationContext implements WebApplicationContext {
    private ServletContext servletContext;

    private WebApplicationContext parentWebApplicationContext;
    private DefaultListableBeanFactory beanFactory;

    public AnnotationConfigWebApplicationContext(String fileName)
    {
        this(fileName,null);
    }

    public AnnotationConfigWebApplicationContext(String fileName,WebApplicationContext parentWebApplicationContext)
    {
        this.parentWebApplicationContext=parentWebApplicationContext;
        this.servletContext= parentWebApplicationContext.getServletContext();
        URL xmlPath=null;
        try {
            xmlPath=this.getServletContext().getResource(fileName);
            List<String> packageNames=XmlScanComponentHelper.getNodeValue(xmlPath);
            List<String> controllerNames=scanPackages(packageNames);
            DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();
            this.beanFactory=defaultListableBeanFactory;
            this.beanFactory.setParent(this.parentWebApplicationContext.getBeanFactory());
            loadBeanDefinitions(controllerNames);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadBeanDefinitions(List<String> controllerNames)
    {
        for (String controller:controllerNames)
        {
            String beanId=controller;
            String beanClassName=controller;
            BeanDefinition beanDefinition = new BeanDefinition(beanId, beanClassName);
            this.beanFactory.registerBeanDefinition(beanId,beanDefinition);
        }
    }

    private List<String> scanPackages(List<String> packageNames) {
        List<String> tempControllerNames =new ArrayList<>();
        for (String packageName:packageNames) {
            tempControllerNames.addAll(scanPackage(packageName));
        }
        return tempControllerNames;
    }

    private List<String> scanPackage(String packageName) {
        List<String> tempControllerNames=new ArrayList<>();
        URI uri=null;
        try {
            uri=this.getClass().getResource("/"+packageName.replaceAll("\\.","/")).toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        File dir=new File(uri);
        for (File file:dir.listFiles()) {
            if(file.isDirectory()) {
                tempControllerNames.addAll(scanPackage(packageName+"."+file.getName()));
            } else {
                String controllerName=packageName+"."+file.getName().replace(".class","");
                tempControllerNames.add(controllerName);
            }
        }
        return tempControllerNames;
    }


    public void setParent(WebApplicationContext parentWebApplicationContext)
    {
        this.parentWebApplicationContext=parentWebApplicationContext;
        this.beanFactory.setParent(this.parentWebApplicationContext.getBeanFactory());
    }
    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext=servletContext;
    }

    @Override
    public void registerListeners() {
        this.getApplicationEventPublisher().addApplicationListener(new ApplicationListener());
    }

    @Override
    public void initApplicationEventPublisher() {
        this.setApplicationEventPublisher(new SimpleApplicationEventPublisher());
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) {

    }

    @Override
    public void registerBeanPostProcessors(ConfigurableListableBeanFactory bf) {
        beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }

    @Override
    public void onRefresh() {
        beanFactory.refresh();
    }

    @Override
    public void finishRefresh() {

    }

    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return beanFactory;
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        this.getApplicationEventPublisher().publishEvent(event);
    }

    @Override
    public void addApplicationListener(ApplicationListener listener) {
        this.getApplicationEventPublisher().addApplicationListener(listener);
    }
}
