package com.niit.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.niit.dao.UserCommentDAO;
import com.niit.model.UserComment;

@Repository("userCommentDAO")
public class UserCommentDAOImpl implements UserCommentDAO
{
	@Autowired
	UserComment userComment;

	@Autowired
	private SessionFactory sessionFactory;

	public UserCommentDAOImpl(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	@Transactional
	public List<UserComment> list(char type, int threadID)
	{
		String hql = "from UserComment where type = '" + type + "' and threadID = " + threadID + " order by createdOn";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		@SuppressWarnings("unchecked")
		List<UserComment> list = (List<UserComment>) query.list();

		return list;
	}

	@Transactional
	public UserComment get(int id)
	{
		return sessionFactory.getCurrentSession().get(UserComment.class, id);
	}

	@Transactional
	public boolean save(UserComment userComment)
	{
		try
		{
			sessionFactory.getCurrentSession().save(userComment);
		}catch(Exception e)
		{
			System.out.println("Exception on saving UserComment: " + e);
			return false;
		}

		return true;
	}

	@Transactional
	public boolean update(UserComment userComment)
	{
		try
		{
			sessionFactory.getCurrentSession().update(userComment);
		}catch(Exception e)
		{
			System.out.println("Exception on updating UserComment: " + e);
			return false;
		}

		return true;
	}

	@Transactional
	public boolean delete(int id)
	{
		userComment.setId(id);

		try
		{
			sessionFactory.getCurrentSession().delete(userComment);
		}catch(Exception e)
		{
			System.out.println("Exception on deleting UserComment: " + e);
			return false;
		}

		return true;
	}
}