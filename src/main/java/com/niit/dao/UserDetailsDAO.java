package com.niit.dao;

import java.util.List;

import com.niit.model.UserDetails;

public interface UserDetailsDAO
{
	public List<UserDetails> list();

	public UserDetails get(int id);

	public UserDetails get(String username);

	public UserDetails getByEmail(String email);

	public boolean save(UserDetails userDetails);

	public boolean update(UserDetails userDetails);

	public boolean delete(int id);

	public UserDetails login(String username, String password);
}