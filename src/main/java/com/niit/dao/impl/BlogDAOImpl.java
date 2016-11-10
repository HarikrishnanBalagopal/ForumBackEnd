package com.niit.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.niit.dao.BlogDAO;
import com.niit.model.Blog;

@Repository("blogDAO")
public class BlogDAOImpl implements BlogDAO
{
	@Autowired
	Blog blog;

	@Autowired
	private SessionFactory sessionFactory;

	public BlogDAOImpl(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	@Transactional
	public List<Blog> list()
	{
		String hql = "from Blog";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		@SuppressWarnings("unchecked")
		List<Blog> list = (List<Blog>) query.list();

		return list;
	}

	@Transactional
	public Blog get(int id)
	{
		return sessionFactory.getCurrentSession().get(Blog.class, id);
	}

	@Transactional
	public boolean save(Blog blog)
	{
		try
		{
			sessionFactory.getCurrentSession().save(blog);
		}catch(Exception e)
		{
			System.out.println("Exception on saving Blog: " + e);
			return false;
		}

		return true;
	}

	@Transactional
	public boolean update(Blog blog)
	{
		try
		{
			sessionFactory.getCurrentSession().update(blog);
		}catch(Exception e)
		{
			System.out.println("Exception on updating Blog: " + e);
			return false;
		}

		return true;
	}

	@Transactional
	public boolean delete(int id)
	{
		blog.setId(id);

		try
		{
			sessionFactory.getCurrentSession().delete(blog);
		}catch(Exception e)
		{
			System.out.println("Exception on deleting Blog: " + e);
			return false;
		}

		return true;
	}
}