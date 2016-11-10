package com.niit.dao;

import java.util.List;

import com.niit.model.Forum;

public interface ForumDAO
{
	public List list();

	public Forum get(int id);

	public boolean save(Forum forum);

	public boolean update(Forum forum);

	public boolean delete(int id);
}