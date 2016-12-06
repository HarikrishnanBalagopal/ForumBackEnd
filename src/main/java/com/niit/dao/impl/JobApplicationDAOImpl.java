package com.niit.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.niit.dao.JobApplicationDAO;
import com.niit.model.JobApplication;

@Repository("JobApplicationDAO")
public class JobApplicationDAOImpl implements JobApplicationDAO
{
	@Autowired
	JobApplication jobApplication;

	@Autowired
	private SessionFactory sessionFactory;

	public JobApplicationDAOImpl(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	@Transactional
	public List<JobApplication> list()
	{
		String hql = "from JobApplication";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		@SuppressWarnings("unchecked")
		List<JobApplication> list = (List<JobApplication>) query.list();

		return list;
	}

	@Transactional
	public List<JobApplication> list(int userID)
	{
		String hql = "from JobApplication where userID = " + userID;
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		@SuppressWarnings("unchecked")
		List<JobApplication> list = (List<JobApplication>) query.list();

		return list;
	}

	@Transactional
	public JobApplication get(int id)
	{
		return sessionFactory.getCurrentSession().get(JobApplication.class, id);
	}

	@Transactional
	public boolean save(JobApplication jobApplication)
	{
		try
		{
			sessionFactory.getCurrentSession().save(jobApplication);
		}catch(Exception e)
		{
			System.out.println("Exception on saving JobApplication: " + e);
			return false;
		}

		return true;
	}

	@Transactional
	public boolean update(JobApplication jobApplication)
	{
		try
		{
			sessionFactory.getCurrentSession().update(jobApplication);
		}catch(Exception e)
		{
			System.out.println("Exception on updating JobApplication: " + e);
			return false;
		}

		return true;
	}

	@Transactional
	public boolean delete(int id)
	{
		jobApplication.setId(id);

		try
		{
			sessionFactory.getCurrentSession().delete(jobApplication);
		}catch(Exception e)
		{
			System.out.println("Exception on deleting JobApplication: " + e);
			return false;
		}

		return true;
	}
	
	@Transactional
	public boolean deleteAll(int id)
	{
		String hql = "delete from JobApplication where jobID = " + id;
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		try
		{
			query.executeUpdate();
		}catch(Exception e)
		{
			System.out.println("Exception on deleting all JobApplications of listing " + id + " :" + e);
			return false;
		}

		return true;
	}
}