package com.niit.dao;

import java.util.List;

import com.niit.model.Friend;

public interface FriendDAO
{
	public List<Friend> list();

	public Friend get(int id);

	public boolean save(Friend friend);

	public boolean update(Friend friend);

	public boolean delete(int id);
}