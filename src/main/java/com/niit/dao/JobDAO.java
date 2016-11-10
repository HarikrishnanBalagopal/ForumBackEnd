package com.niit.dao;

import java.util.List;

import com.niit.model.Job;

public interface JobDAO
{
	public List<Job> list();

	public Job get(int id);

	public boolean save(Job job);

	public boolean update(Job job);

	public boolean delete(int id);
}