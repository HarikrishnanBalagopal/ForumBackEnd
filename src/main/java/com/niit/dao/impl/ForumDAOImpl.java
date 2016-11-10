package com.niit.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.niit.dao.ForumDAO;
import com.niit.model.Forum;

@Repository("forumDAO")
public class ForumDAOImpl implements ForumDAO
{
	@Autowired
	Forum forum;

	@Autowired
	private SessionFactory sessionFactory;

	public ForumDAOImpl(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	@Transactional
	public List list()
	{
		String hql = "select F, D.username from Forum F inner join UserDetails D on F.userID = D.id order by F.lastComment desc";
		//String hql = "from Forum order by lastComment";
		return sessionFactory.getCurrentSession().createQuery(hql).list();
	}

	@Transactional
	public Forum get(int id)
	{
		return sessionFactory.getCurrentSession().get(Forum.class, id);
	}

	@Transactional
	public boolean save(Forum forum)
	{
		try
		{
			sessionFactory.getCurrentSession().save(forum);
		}catch(Exception e)
		{
			System.out.println("Exception on saving Forum: " + e);
			return false;
		}

		return true;
	}

	@Transactional
	public boolean update(Forum forum)
	{
		try
		{
			sessionFactory.getCurrentSession().update(forum);
		}catch(Exception e)
		{
			System.out.println("Exception on updating Forum: " + e);
			return false;
		}

		return true;
	}

	@Transactional
	public boolean delete(int id)
	{
		forum.setId(id);

		try
		{
			sessionFactory.getCurrentSession().delete(forum);
		}catch(Exception e)
		{
			System.out.println("Exception on deleting Forum: " + e);
			return false;
		}

		return true;
	}
}