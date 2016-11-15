package com.niit.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "user_comment")
@Component
public class UserComment
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERCOMMENT_SEQ")
	@SequenceGenerator(name = "USERCOMMENT_SEQ", sequenceName = "USERCOMMENT_SEQ", allocationSize = 1)
	private int id;
	
	@Column(name = "user_id")
	private int userID;
	private char type;

	@Column(name = "thread_id")
	private int threadID;
	
	@Lob
	private String content;
	
	@CreationTimestamp
	@Column(name = "created_on", updatable = false)
	private Date createdOn;

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

	public char getType()
	{
		return type;
	}

	public void setType(char type)
	{
		this.type = type;
	}

	public int getThreadID()
	{
		return threadID;
	}

	public void setThreadID(int threadID)
	{
		this.threadID = threadID;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public Date getCreatedOn()
	{
		return createdOn;
	}

	public void setCreatedOn(Date createdOn)
	{
		this.createdOn = createdOn;
	}
}