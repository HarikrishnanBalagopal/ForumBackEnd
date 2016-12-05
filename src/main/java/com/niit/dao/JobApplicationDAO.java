package com.niit.dao;

import java.util.List;

import com.niit.model.JobApplication;

public interface JobApplicationDAO
{
	public List<JobApplication> list();

	public List<JobApplication> list(int userID);

	public JobApplication get(int id);

	public boolean save(JobApplication jobApplication);

	public boolean update(JobApplication jobApplication);

	public boolean delete(int id);
}