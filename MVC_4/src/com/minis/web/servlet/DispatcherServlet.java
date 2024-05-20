package com.minis.web.servlet;


import com.minis.beans.BeansException;
import com.minis.web.AnnotationConfigWebApplicationContext;
import com.minis.web.WebApplicationContext;
import com.minis.web.XmlScanComponentHelper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.net.MalformedURLException;
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

//	private List<String> packageNames = new ArrayList<>();
//	private Map<String,Object> controllerObjs = new HashMap<>();
//	private List<String> controllerNames = new ArrayList<>();
//	private Map<String,Class<?>> controllerClasses = new HashMap<>();


	private HandlerMapping handlerMapping;
	private HandlerAdapter handlerAdapter;

	private ViewResolver viewResolver;

	public DispatcherServlet() {
		super();
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		this.parentWebApplicationContext=(WebApplicationContext) this.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

		sContextConfigLocation = config.getInitParameter("contextConfigLocation");
//		URL xmlPath = null;
//		try {
//			xmlPath = this.getServletContext().getResource(sContextConfigLocation);
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		}
//		packageNames = XmlScanComponentHelper.getNodeValue(xmlPath);
		this.webApplicationContext=new AnnotationConfigWebApplicationContext(sContextConfigLocation,this.parentWebApplicationContext);
		Refresh();
	}

	//对所有的mappingValues中注册的类进行实例化，默认构造函数
	protected void Refresh() {
//		initController();
		initHandlerMappings(this.webApplicationContext);
		initHandlerAdapters(this.webApplicationContext);
		initViewResolvers(this.webApplicationContext);
	}

	protected void initHandlerMappings(WebApplicationContext wac) {
		this.handlerMapping = new RequestMappingHandlerMapping(wac);

	}

	protected void initHandlerAdapters(WebApplicationContext wac) {
		this.handlerAdapter = new RequestMappingHandlerAdapter(wac);
	}

//	private void initController() {
//		this.controllerNames= Arrays.asList(this.webApplicationContext.getBeanDefinitionNames());
//		for (String controllerName:controllerNames) {
//			try {
//				Class<?> clz=Class.forName(controllerName);
//
//
//				controllerClasses.put(controllerName,clz);
//				controllerObjs.put(controllerName,this.webApplicationContext.getBean(controllerName));
//			} catch (ClassNotFoundException e) {
//				throw new RuntimeException(e);
//			}  catch (BeansException e) {
//				throw new RuntimeException(e);
//			}
//		}
//	}

	protected void initViewResolvers(WebApplicationContext wac) {

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
		ModelAndView mv=null;

		handlerMethod = this.handlerMapping.getHandler(processedRequest);
		if(handlerMethod==null) return;
		HandlerAdapter ha=this.handlerAdapter;
		mv=ha.handle(request,response,handlerMethod);
	}


//	protected void render( HttpServletRequest request, HttpServletResponse response,ModelAndView mv) throws Exception {
//		View view;
//		String viewName = mv.getViewName();
//		if (viewName != null) {
//			// We need to resolve the view name.
//			view = resolveViewName(viewName, mv.getModel(), request);
//		}
//		else {
//			view = mv.getView();
//		}
//
//		view.render(mv.getModel(), request, response);
//	}
//
//	protected View resolveViewName(String viewName, Map<String, Object> model,
//								   HttpServletRequest request) throws Exception {
//		if (this.viewResolver != null) {
//			View view = viewResolver.resolveViewName(viewName);
//			if (view != null) {
//				return view;
//			}
//		}
//		return null;
//	}


}
