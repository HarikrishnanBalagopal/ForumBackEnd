package com.niit.model;

import java.sql.Clob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Min;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.stereotype.Component;

@Entity
@Component
public class Blog
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BLOG_SEQ")
	@SequenceGenerator(name = "BLOG_SEQ", sequenceName = "BLOG_SEQ", allocationSize = 1)
	private int id;

	@Column(name = "user_id")
	private int userID;

	@NotBlank(message = "Title cannot be blank")
	private String title;
	private Clob content;
	private char status;
	private String reason;

	@Min(0)
	private int likes;

	@Min(0)
	private int dislikes;

	@Column(name = "total_comments")
	@Min(0)
	private int totalComments;

	@CreationTimestamp
	@Column(name = "created_on")
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

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public Clob getContent()
	{
		return content;
	}

	public void setContent(Clob content)
	{
		this.content = content;
	}

	public char getStatus()
	{
		return status;
	}

	public void setStatus(char status)
	{
		this.status = status;
	}

	public String getReason()
	{
		return reason;
	}

	public void setReason(String reason)
	{
		this.reason = reason;
	}

	public int getLikes()
	{
		return likes;
	}

	public void setLikes(int likes)
	{
		this.likes = likes;
	}

	public int getDislikes()
	{
		return dislikes;
	}

	public void setDislikes(int dislikes)
	{
		this.dislikes = dislikes;
	}

	public int getTotalComments()
	{
		return totalComments;
	}

	public void setTotalComments(int totalComments)
	{
		this.totalComments = totalComments;
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