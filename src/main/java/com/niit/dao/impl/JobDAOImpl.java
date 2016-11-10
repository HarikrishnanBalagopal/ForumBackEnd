package com.niit.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.niit.dao.JobDAO;
import com.niit.model.Job;

@Repository("jobDAO")
public class JobDAOImpl implements JobDAO
{
	@Autowired
	Job job;

	@Autowired
	private SessionFactory sessionFactory;

	public JobDAOImpl(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	@Transactional
	public List<Job> list()
	{
		String hql = "from Job";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		@SuppressWarnings("unchecked")
		List<Job> list = (List<Job>) query.list();

		return list;
	}

	@Transactional
	public Job get(int id)
	{
		return sessionFactory.getCurrentSession().get(Job.class, id);
	}

	@Transactional
	public boolean save(Job job)
	{
		try
		{
			sessionFactory.getCurrentSession().save(job);
		}catch(Exception e)
		{
			System.out.println("Exception on saving Job: " + e);
			return false;
		}

		return true;
	}

	@Transactional
	public boolean update(Job job)
	{
		try
		{
			sessionFactory.getCurrentSession().update(job);
		}catch(Exception e)
		{
			System.out.println("Exception on updating Job: " + e);
			return false;
		}

		return true;
	}

	@Transactional
	public boolean delete(int id)
	{
		job.setId(id);

		try
		{
			sessionFactory.getCurrentSession().delete(job);
		}catch(Exception e)
		{
			System.out.println("Exception on deleting Job: " + e);
			return false;
		}

		return true;
	}
}