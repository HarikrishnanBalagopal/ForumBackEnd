package com.niit.model;

import java.util.Date;

public class Message
{
	private int id;
	private int senderID;
	private int receiverID;
	private String content;
	private Date time;

	public Message()
	{

	}

	public Message(int id, String content)
	{
		this.id = id;
		this.content = content;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public int getSenderID()
	{
		return senderID;
	}

	public void setSenderID(int senderID)
	{
		this.senderID = senderID;
	}

	public int getReceiverID()
	{
		return receiverID;
	}

	public void setReceiverID(int receiverID)
	{
		this.receiverID = receiverID;
	}

	public Date getTime()
	{
		return time;
	}

	public void setTime(Date time)
	{
		this.time = time;
	}
}