package com.minis.test.web;


import com.minis.web.RequestMapping;

public class HelloWorldBean {

	@RequestMapping("/test1")
	public String doGet() {
		return "hello world!";
	}

	@RequestMapping("/test2")
	public String doPost() {
		return "hello world!";
	}
}