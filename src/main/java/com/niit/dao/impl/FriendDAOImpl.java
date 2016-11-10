package com.niit.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.niit.dao.FriendDAO;
import com.niit.model.Friend;

@Repository("friendDAO")
public class FriendDAOImpl implements FriendDAO
{
	@Autowired
	Friend friend;

	@Autowired
	private SessionFactory sessionFactory;

	public FriendDAOImpl(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	@Transactional
	public List<Friend> list()
	{
		String hql = "from Friend";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		@SuppressWarnings("unchecked")
		List<Friend> list = (List<Friend>) query.list();

		return list;
	}

	@Transactional
	public Friend get(int id)
	{
		return sessionFactory.getCurrentSession().get(Friend.class, id);
	}

	@Transactional
	public boolean save(Friend friend)
	{
		try
		{
			sessionFactory.getCurrentSession().save(friend);
		}catch(Exception e)
		{
			System.out.println("Exception on saving Friend: " + e);
			return false;
		}

		return true;
	}

	@Transactional
	public boolean update(Friend friend)
	{
		try
		{
			sessionFactory.getCurrentSession().update(friend);
		}catch(Exception e)
		{
			System.out.println("Exception on updating Friend: " + e);
			return false;
		}

		return true;
	}

	@Transactional
	public boolean delete(int id)
	{
		friend.setId(id);

		try
		{
			sessionFactory.getCurrentSession().delete(friend);
		}catch(Exception e)
		{
			System.out.println("Exception on deleting Friend: " + e);
			return false;
		}

		return true;
	}
}