package com.niit.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.springframework.stereotype.Component;

@Entity
@Component
public class Friend
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FRIEND_SEQ")
	@SequenceGenerator(name = "FRIEND_SEQ", sequenceName = "FRIEND_SEQ", allocationSize = 1)
	private int id;

	@Column(name = "user_id")
	private int userID;

	@Column(name = "friend_id")
	private int friendID;
	private char status;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getUserID()
	{
		return userID;
	}

	public void setUserID(int userID)
	{
		this.userID = userID;
	}

	public int getFriendID()
	{
		return friendID;
	}

	public void setFriendID(int friendID)
	{
		this.friendID = friendID;
	}

	public char getStatus()
	{
		return status;
	}

	public void setStatus(char status)
	{
		this.status = status;
	}
}