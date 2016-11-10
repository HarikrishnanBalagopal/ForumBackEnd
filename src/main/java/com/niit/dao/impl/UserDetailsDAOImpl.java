package com.niit.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.niit.dao.UserDetailsDAO;
import com.niit.model.UserDetails;

@Repository("userDetailsDAO")
public class UserDetailsDAOImpl implements UserDetailsDAO
{
	@Autowired
	UserDetails userDetails;

	@Autowired
	private SessionFactory sessionFactory;

	public UserDetailsDAOImpl(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	@Transactional
	public List<UserDetails> list()
	{
		String hql = "from UserDetails";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		@SuppressWarnings("unchecked")
		List<UserDetails> list = (List<UserDetails>) query.list();

		return list;
	}

	@Transactional
	public UserDetails get(int id)
	{
		return sessionFactory.getCurrentSession().get(UserDetails.class, id);
	}

	@Transactional
	public UserDetails get(String username)
	{
		String hql = "from UserDetails where username = '" + username + "'";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		@SuppressWarnings("unchecked")
		List<UserDetails> list = (List<UserDetails>) query.list();
		if(list != null && list.size() > 0)
			return list.get(0);

		return null;
	}

	@Transactional
	public UserDetails getByEmail(String email)
	{
		String hql = "from UserDetails where email = '" + email + "'";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		@SuppressWarnings("unchecked")
		List<UserDetails> list = (List<UserDetails>) query.list();
		if(list != null && list.size() > 0)
			return list.get(0);

		return null;
	}

	@Transactional
	public boolean save(UserDetails userDetails)
	{
		try
		{
			sessionFactory.getCurrentSession().save(userDetails);
		}catch(Exception e)
		{
			System.out.println("Exception on saving UserDetails: " + e);
			return false;
		}

		return true;
	}

	@Transactional
	public boolean update(UserDetails userDetails)
	{
		try
		{
			sessionFactory.getCurrentSession().update(userDetails);
		}catch(Exception e)
		{
			System.out.println("Exception on updating UserDetails: " + e);
			return false;
		}

		return true;
	}

	@Transactional
	public boolean delete(int id)
	{
		userDetails.setId(id);

		try
		{
			sessionFactory.getCurrentSession().delete(userDetails);
		}catch(Exception e)
		{
			System.out.println("Exception on deleting UserDetails: " + e);
			return false;
		}

		return true;
	}

	@Transactional
	public UserDetails login(String username, String password)
	{
		String hql = "from UserDetails where username = '" + username + "' and password = '" + password + "'";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		@SuppressWarnings("unchecked")
		List<UserDetails> list = (List<UserDetails>) query.list();
		if(list != null && list.size() > 0)
			return list.get(0);

		return null;
	}
}