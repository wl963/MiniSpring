package com.minis.web.servlet;

import com.minis.web.RequestMapping;
import com.minis.web.WebApplicationContext;
import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.Method;

public class RequestMappingHandlerMapping implements HandlerMapping{

    private final MappingRegistry mappingRegistry=new MappingRegistry();

    WebApplicationContext webApplicationContext;

    public RequestMappingHandlerMapping(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
        initMapping();
    }

    private void initMapping() {
        Class<?> clz=null;
        Object obj=null;
        String[] controllerNames = this.webApplicationContext.getBeanDefinitionNames();
        for (String cntrollerName:controllerNames)
        {
            try {
                clz=Class.forName(cntrollerName);
                obj=webApplicationContext.getBean(cntrollerName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Method[] methods = clz.getDeclaredMethods();
            if(methods!=null) {
                for (Method method : methods) {
                    boolean annotationPresent = method.isAnnotationPresent(RequestMapping.class);
                    if(annotationPresent)
                    {
                        String methodName=method.getName();
                        String url=method.getAnnotation(RequestMapping.class).value();
                        mappingRegistry.getUrlMappingNames().add(url);
                        mappingRegistry.getMappingObjs().put(url,obj);
                        mappingRegistry.getMappingMethods().put(url,method);
                    }
                }
            }
        }
    }

    @Override
    public HandlerMethod getHandler(HttpServletRequest request) throws Exception {
        String servletPath = request.getServletPath();
        if(!mappingRegistry.getUrlMappingNames().contains(servletPath)) return null;
        Object obj = mappingRegistry.getMappingObjs().get(servletPath);
        Method method = mappingRegistry.getMappingMethods().get(servletPath);
        HandlerMethod handlerMethod = new HandlerMethod(method, obj);

        return handlerMethod;
    }
}
