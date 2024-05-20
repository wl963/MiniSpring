package com.minis.web;


import jakarta.servlet.http.HttpServletRequest;

public class WebDataBinderFactory {
    public WebDataBinder createBinder(HttpServletRequest request, Object target, String objectName) {
        WebDataBinder wbd = new WebDataBinder(target, objectName);
        initBinder(wbd, request);
        return wbd;
    }
    protected void initBinder(WebDataBinder dataBinder, HttpServletRequest request) {
    }
}