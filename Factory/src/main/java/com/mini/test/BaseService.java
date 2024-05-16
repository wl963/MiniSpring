package com.mini.test;

import com.mini.beans.factory.annptation.Autowired;


public class BaseService {
    @Autowired
    private BaseBaseService bbs;
    public BaseBaseService getBbs() {
        return bbs;
    }
    public void setBbs(BaseBaseService bbs) {
        this.bbs = bbs;
    }
    public BaseService() {
    }
    public void sayHello() {
        System.out.println("Base Service says Hello");
        bbs.sayHello();
    }
}