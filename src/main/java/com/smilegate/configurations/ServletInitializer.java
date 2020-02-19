package com.smilegate.configurations;

import com.smilegate.configurations.spring.*;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;

/**
 * web.xml 에 기술하는 내용을 java configuration 형식으로 여기에 설정.
 * https://www.baeldung.com/spring-xml-vs-java-config
 * https://www.baeldung.com/java-web-app-without-web-xml
 */
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ApplicationMain.class);
	}

	@Override
	public void onStartup(ServletContext container) throws ServletException {

		//ROOT 콘텍스트
		AnnotationConfigWebApplicationContext rootContext
				= new AnnotationConfigWebApplicationContext();
		rootContext.register(RootContextConfig.class);
		//rootContext.setConfigLocation("com.smilegate.configurations.spring");
		// Manage the lifecycle of the root application context
		container.addListener(new ContextLoaderListener(rootContext));


		//디스팻처 서블릿 콘텍스트
		AnnotationConfigWebApplicationContext dispatcherContext
				= new AnnotationConfigWebApplicationContext();
		dispatcherContext.register(DispatcherContextConfig.class);

		ServletRegistration.Dynamic dispatcher = container
				.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");


		dispatcher.setMultipartConfig( new MultipartConfigElement(null, 1024*1024*5, 1024*1024*5*5, 1024*1024) );


		FilterRegistration charEncodingfilterReg = container.addFilter("CharacterEncodingFilter", CharacterEncodingFilter.class);
		charEncodingfilterReg.setInitParameter("encoding", "UTF-8");
		charEncodingfilterReg.setInitParameter("forceEncoding", "true");
//		charEncodingfilterReg.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), true, "/*");
		charEncodingfilterReg.addMappingForUrlPatterns(null, false, "/*");

	}
}
