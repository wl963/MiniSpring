package com.test.controller;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.web.bind.annotation.RequestMapping;
import com.minis.web.bind.annotation.ResponseBody;
import com.minis.web.servlet.ModelAndView;
import com.test.entity.User;
import com.test.service.BaseService;
import com.test.service.IAction;
import com.test.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;

public class HelloWorldBean {
	@Autowired
	BaseService baseservice;
	
	@Autowired
	UserService userService;
	
	
	@RequestMapping("/test2")
	public void doTest2(HttpServletRequest request, HttpServletResponse response) {
		String str = "test 2, hello world!";
		try {
			response.getWriter().write(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@RequestMapping("/test5")
	public ModelAndView doTest5(User user) {
		ModelAndView mav = new ModelAndView("test","msg",user.getName());
		return mav;
	}
	@RequestMapping("/test6")
	public String doTest6(User user) {
		return "error";
	}	
	
	@RequestMapping("/test7")
	@ResponseBody
	public User doTest7(User user) {
		user.setName(user.getName() + "---");
		user.setBirthday(new Date());
		return user;
	}	
	
	@RequestMapping("/test8")
	@ResponseBody
	public User doTest8(HttpServletRequest request, HttpServletResponse response) {
		int userid = Integer.parseInt(request.getParameter("id"));
		User user = userService.getUserInfo(userid);		
		return user;
	}	
	
//	@RequestMapping("/test9")
//	@ResponseBody
//	public List<User> doTest9(HttpServletRequest request, HttpServletResponse response) {
//		int userid = Integer.parseInt(request.getParameter("id"));
//		List<User> users = userService.getUsers(userid);
//		return users;
//	}

	@RequestMapping("/test10")
	@ResponseBody
	public User doTest9(HttpServletRequest request, HttpServletResponse response) {
		int userid = Integer.parseInt(request.getParameter("id"));
		User user = userService.getUserInfo(userid);
		return user;
	}

	@Autowired
	IAction action;

	@RequestMapping("/testaop")
	public void doTestAop(HttpServletRequest request, HttpServletResponse response) {
		action.doAction();
	}

	@RequestMapping("/testaop2")
	public void doTestAop2(HttpServletRequest request, HttpServletResponse response) {
		action.doAction();
		action.doSomething();
	}

	@Autowired
	IAction action2;

	@RequestMapping("/testaop3")
	public void doTestAop3(HttpServletRequest request, HttpServletResponse response) {
		action2.doAction();

		String str = "test aop 3, hello world!";
		try {
			response.getWriter().write(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/testaop4")
	public void doTestAop4(HttpServletRequest request, HttpServletResponse response) {
		action2.doSomething();

		String str = "test aop 4, hello world!";
		try {
			response.getWriter().write(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
