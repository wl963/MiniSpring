package com.minis.web;

import com.minis.context.ClassPathXmlApplicationContext;
import jakarta.servlet.ServletContext;

public class XmlWebApplicationContent extends ClassPathXmlApplicationContext implements WebApplicationContext {

    private ServletContext servletContext;

    public XmlWebApplicationContent(String fileName)
    {
        super(fileName);
    }
    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext=servletContext;
    }
}
