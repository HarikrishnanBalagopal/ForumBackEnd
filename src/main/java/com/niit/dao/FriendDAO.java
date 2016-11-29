package com.niit.dao;

import java.util.List;

import com.niit.model.Friend;

public interface FriendDAO
{
	public List<Friend> list();

	public Friend get(int id);

	public Friend get(int userID, int friendID);

	public List<Friend> getAll(int userID);

	public boolean request(int userID, int friendID);

	public boolean updateRequest(int userID, int friendID, boolean accepted);

	public boolean save(Friend friend);

	public boolean update(Friend friend);

	public boolean delete(int id);
}