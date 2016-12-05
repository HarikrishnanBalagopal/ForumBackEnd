package com.niit.config;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.niit.model.Blog;
import com.niit.model.Forum;
import com.niit.model.Friend;
import com.niit.model.Job;
import com.niit.model.JobApplication;
import com.niit.model.UserComment;
import com.niit.model.UserDetails;

@Configuration
@ComponentScan("com.niit")
@EnableTransactionManagement
public class ApplicationContextConfig extends WebMvcConfigurerAdapter
{
	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver getResolver() throws IOException
	{
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setMaxUploadSizePerFile(5242880);
		return resolver;
	}

	@Bean(name = "dataSource")
	public DataSource getOracleDataSource()
	{
		DriverManagerDataSource datasource = new DriverManagerDataSource();
		datasource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		datasource.setUrl("jdbc:oracle:thin:@localhost:1521:XE");
		datasource.setUsername("TEST");
		datasource.setPassword("test");

		Properties connectionProperties = new Properties();
		connectionProperties.put("hibernate.show_sql", "true");
		connectionProperties.put("hibernate.format_sql", "true");
		connectionProperties.put("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
		// connectionProperties.put("hibernate.hbm2ddl.auto", "update");
		datasource.setConnectionProperties(connectionProperties);

		return datasource;
	}

	@Autowired
	@Bean(name = "sessionFactory")
	public SessionFactory getSessionFactory(DataSource dataSource)
	{
		LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
		// sessionBuilder.addProperties(getHibernateProperties());
		sessionBuilder.addAnnotatedClass(UserDetails.class);
		sessionBuilder.addAnnotatedClass(UserComment.class);
		sessionBuilder.addAnnotatedClass(Forum.class);
		sessionBuilder.addAnnotatedClass(Blog.class);
		sessionBuilder.addAnnotatedClass(Friend.class);
		sessionBuilder.addAnnotatedClass(Job.class);
		sessionBuilder.addAnnotatedClass(JobApplication.class);
		// sessionBuilder.addAnnotatedClass(Event.class);

		return sessionBuilder.buildSessionFactory();
	}

	@Autowired
	@Bean(name = "transactionManager")
	public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory)
	{
		HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);

		return transactionManager;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry)
	{
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}
}