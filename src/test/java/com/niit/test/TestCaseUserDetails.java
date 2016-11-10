package com.niit.test;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.niit.dao.UserDetailsDAO;
import com.niit.model.UserDetails;

public class TestCaseUserDetails
{
	@Autowired
	private static UserDetailsDAO userDetailsDAO;

	@Autowired
	private static UserDetails userDetails;

	@Autowired
	private static AnnotationConfigApplicationContext context;

	@BeforeClass
	public static void init()
	{
		context = new AnnotationConfigApplicationContext();
		context.scan("com.niit");
		context.refresh();

		userDetailsDAO = (UserDetailsDAO) context.getBean("userDetailsDAO");
		userDetails = (UserDetails) context.getBean("userDetails");
	}

	@AfterClass
	public static void deinit()
	{
		context.close();
	}

	@Test
	public void categoryTestCase()
	{
		userDetails.setUsername("username");
		userDetails.setEmail("email");
		userDetails.setPassword("password");
		userDetails.setRole('S');
		userDetails.setStatus('N');
		userDetails.setOnlineStatus('N');
		userDetails.setAddress("address");
		userDetails.setContactNo("contactNo");

		assertEquals("Create UserDetails", true, userDetailsDAO.save(userDetails));

		userDetails = userDetailsDAO.get("username");
		assertEquals("Get UserDetails", "username", userDetails.getUsername());

		userDetails.setUsername("test");
		assertEquals("Update UserDetails", true, userDetailsDAO.update(userDetails));

		assertEquals("Delete UserDetails", true, userDetailsDAO.delete(userDetails.getId()));
	}
}