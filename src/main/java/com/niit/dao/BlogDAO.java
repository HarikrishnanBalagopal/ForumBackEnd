package com.niit.dao;

import java.util.List;

import com.niit.model.Blog;

public interface BlogDAO
{
	public List<Blog> list();

	public Blog get(int id);

	public boolean save(Blog blog);

	public boolean update(Blog blog);

	public boolean delete(int id);
}