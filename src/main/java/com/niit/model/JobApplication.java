package com.niit.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Entity
@Table(name = "job_application")
@Component
public class JobApplication
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JOB_APPLICATION_SEQ")
	@SequenceGenerator(name = "JOB_APPLICATION_SEQ", sequenceName = "JOB_APPLICATION_SEQ", allocationSize = 1)
	private int id;

	@Column(name = "job_id")
	private int jobID;

	@Column(name = "user_id")
	private int userID;
	private char status = 'N';
	private String reason;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getJobID()
	{
		return jobID;
	}

	public void setJobID(int jobID)
	{
		this.jobID = jobID;
	}

	public int getUserID()
	{
		return userID;
	}

	public void setUserID(int userID)
	{
		this.userID = userID;
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
}