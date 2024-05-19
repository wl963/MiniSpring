package com.minis.web;


import com.minis.beans.BeansException;
import com.minis.beans.factory.annotation.Autowired;
import com.minis.web.servlet.HandlerAdapter;
import com.minis.web.servlet.HandlerMapping;
import com.minis.web.servlet.RequestMappingHandlerAdapter;
import com.minis.web.servlet.RequestMappingHandlerMapping;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet implementation class DispatcherServlet
 */
public class DispatcherServlet extends HttpServlet {

	private String sContextConfigLocation;

	private WebApplicationContext webApplicationContext;

	private WebApplicationContext parentWebApplicationContext;		//Listener启动的上下文

	private List<String> packageNames = new ArrayList<>();
	private Map<String,Object> controllerObjs = new HashMap<>();
	private List<String> controllerNames = new ArrayList<>();
	private Map<String,Class<?>> controllerClasses = new HashMap<>();
	private List<String> urlMappingNames = new ArrayList<>();
	private Map<String,Object> mappingObjs = new HashMap<>();
	private Map<String,Method> mappingMethods = new HashMap<>();


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
		initMapping();
	}



	private void initController() {
		this.controllerNames=scanPackages(this.packageNames);
		for (String controllerName:controllerNames) {
			try {
				Class<?> clz=Class.forName(controllerName);
				Object obj=clz.newInstance();

				controllerClasses.put(controllerName,clz);
				controllerObjs.put(controllerName,obj);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	protected void populate(Object obj,String beanName) {
		Class<?> clz=obj.getClass();
		Field[] fields = clz.getDeclaredFields();

		if(fields!=null) {
			for (Field field : fields) {
				boolean annotationPresent = field.isAnnotationPresent(Autowired.class);
				if (!annotationPresent) continue;
				String methodName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
				try {
					Object bean = webApplicationContext.getBean(field.getName());
					field.set(obj,bean);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				} catch (BeansException e) {
					throw new RuntimeException(e);
				}
			}
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

	private void initMapping(){
		for (String controllerName:controllerNames) {
			Class<?> clz=controllerClasses.get(controllerName);
			Object obj=this.controllerObjs.get(controllerName);
			Method[] methods= clz.getDeclaredMethods();
			for (Method method:methods) {
				boolean isRequestMapping = method.isAnnotationPresent(RequestMapping.class);
				if(isRequestMapping) {
					String methodName=method.getName();
					String urlMapping=method.getAnnotation(RequestMapping.class).value();
					urlMappingNames.add(urlMapping);
					mappingObjs.put(urlMapping,obj);
					mappingMethods.put(urlMapping,method);
				}
			}
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sPath = request.getServletPath(); //获取请求的path
		if(!this.urlMappingNames.contains(sPath)) return;
		Method method = mappingMethods.get(sPath);
		Object obj = mappingObjs.get(sPath);
		Object result=null;
		try {
			result = method.invoke(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		response.getWriter().append(result.toString());
	}

}
