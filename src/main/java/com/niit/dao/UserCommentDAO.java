package com.niit.dao;

import java.util.List;

import com.niit.model.UserComment;

public interface UserCommentDAO
{
	public List<UserComment> list(char type, int threadID);

	public UserComment get(int id);

	public boolean save(UserComment userComment);

	public boolean update(UserComment userComment);

	public boolean delete(int id);
}