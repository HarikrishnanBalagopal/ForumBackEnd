package com.niit.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer
{
	private static final Logger log = LoggerFactory.getLogger(AppInitializer.class);

	@Override
	protected Class<?>[] getRootConfigClasses()
	{
		log.debug("Method Start: getRootConfigClasses");
		log.debug("Method End: getRootConfigClasses");
		return new Class[]
		{ AppConfig.class, WebSocketConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses()
	{
		log.debug("Method Start: getServletConfigClasses");
		log.debug("Method End: getServletConfigClasses");
		return new Class[]
		{ AppConfig.class, WebSocketConfig.class };
	}

	@Override
	protected String[] getServletMappings()
	{
		log.debug("Method Start: getServletMappings");
		log.debug("Method End: getServletMappings");
		return new String[]
		{ "/" };
	}
}