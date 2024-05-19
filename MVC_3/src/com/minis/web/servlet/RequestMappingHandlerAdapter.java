package com.minis.web.servlet;

import com.minis.web.WebApplicationContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;

public class RequestMappingHandlerAdapter implements HandlerAdapter{

    WebApplicationContext webApplicationContext;

    public RequestMappingHandlerAdapter(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
    }



    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        handlerInternal(request,response,(HandlerMethod) handler);
    }

    private void handlerInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
        Method method = handler.getMethod();
        Object obj = handler.getBean();
        Object result=null;
        try {
            result=method.invoke(obj);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        try {
            response.getWriter().append(result.toString());
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
