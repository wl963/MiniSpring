package com.minis.web.servlet;


import com.minis.beans.BeansException;
import com.minis.beans.factory.annotation.Autowired;
import com.minis.web.AnnotationConfigWebApplicationContext;
import com.minis.web.RequestMapping;
import com.minis.web.WebApplicationContext;
import com.minis.web.XmlScanComponentHelper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * Servlet implementation class DispatcherServlet
 */
public class DispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = DispatcherServlet.class.getName() + ".CONTEXT";

	private String sContextConfigLocation;

	private WebApplicationContext webApplicationContext;

	private WebApplicationContext parentWebApplicationContext;        //Listener启动的上下文

	private List<String> packageNames = new ArrayList<>();
	private Map<String,Object> controllerObjs = new HashMap<>();
	private List<String> controllerNames = new ArrayList<>();
	private Map<String,Class<?>> controllerClasses = new HashMap<>();


	private HandlerMapping handlerMapping;
	private HandlerAdapter handlerAdapter;

	public DispatcherServlet() {
		super();
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		this.parentWebApplicationContext=(WebApplicationContext) this.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

		sContextConfigLocation = config.getInitParameter("contextConfigLocation");
		URL xmlPath = null;
		try {
			xmlPath = this.getServletContext().getResource(sContextConfigLocation);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		packageNames = XmlScanComponentHelper.getNodeValue(xmlPath);
		this.webApplicationContext=new AnnotationConfigWebApplicationContext(sContextConfigLocation,this.parentWebApplicationContext);
		Refresh();
	}

	//对所有的mappingValues中注册的类进行实例化，默认构造函数
	protected void Refresh() {
		initController();
		initHandlerMappings(this.webApplicationContext);
		initHandlerAdapters(this.webApplicationContext);
	}

	protected void initHandlerMappings(WebApplicationContext wac) {
		this.handlerMapping = new RequestMappingHandlerMapping(wac);

	}

	protected void initHandlerAdapters(WebApplicationContext wac) {
		this.handlerAdapter = new RequestMappingHandlerAdapter(wac);
	}

	private void initController() {
		this.controllerNames= Arrays.asList(this.webApplicationContext.getBeanDefinitionNames());
		for (String controllerName:controllerNames) {
			try {
				Class<?> clz=Class.forName(controllerName);


				controllerClasses.put(controllerName,clz);
				controllerObjs.put(controllerName,this.webApplicationContext.getBean(controllerName));
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}  catch (BeansException e) {
				throw new RuntimeException(e);
			}
		}
	}



	protected void service(HttpServletRequest request,HttpServletResponse response) {
		request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE,this.webApplicationContext);
		try {
			doDispatch(request,response);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpServletRequest processedRequest =request;
		HandlerMethod handlerMethod = null;
		handlerMethod = this.handlerMapping.getHandler(request);
		if(handlerMethod==null) return;
		HandlerAdapter ha=this.handlerAdapter;
		ha.handle(request,response,handlerMethod);
	}


}
