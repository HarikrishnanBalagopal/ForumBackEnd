package com.niit.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.stereotype.Component;

@Entity
@Component
public class Forum
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FORUM_SEQ")
	@SequenceGenerator(name = "FORUM_SEQ", sequenceName = "FORUM_SEQ", allocationSize = 1)
	private int id;

	@Column(name = "user_id")
	private int userID;

	@NotBlank(message = "Title cannot be blank")
	private String title;
	
	@Lob
	private String content;

	@CreationTimestamp
	@Column(name = "created_on", updatable = false)
	private Date createdOn;

	@UpdateTimestamp
	@Column(name = "last_edit")
	private Date lastEdit;

	@Column(name = "last_comment")
	private Date lastComment;

	@Column(name = "total_comments")
	private int totalComments;

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

	public Date getLastEdit()
	{
		return lastEdit;
	}

	public void setLastEdit(Date lastEdit)
	{
		this.lastEdit = lastEdit;
	}

	public Date getLastComment()
	{
		return lastComment;
	}

	public void setLastComment(Date lastComment)
	{
		this.lastComment = lastComment;
	}

	public int getTotalComments()
	{
		return totalComments;
	}

	public void setTotalComments(int totalComments)
	{
		this.totalComments = totalComments;
	}
}