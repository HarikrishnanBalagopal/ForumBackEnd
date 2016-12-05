package com.niit.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "user_details")
@Component
public class UserDetails
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERDETAILS_SEQ")
	@SequenceGenerator(name = "USERDETAILS_SEQ", sequenceName = "USERDETAILS_SEQ", allocationSize = 1)
	private int id;

	@NotBlank(message = "Username cannot be blank")
	private String username;

	@NotBlank(message = "Password cannot be blank")
	private String password;

	@NotBlank(message = "Email cannot be blank")
	private String email;
	private char role;
	private char status = 'N';
	private String reason;
	private String address;

	@Column(name = "contact_no")
	private String contactNo;

	@Column(name = "online_status")
	private char onlineStatus;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public char getRole()
	{
		return role;
	}

	public void setRole(char role)
	{
		this.role = role;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public char getStatus()
	{
		return status;
	}

	public void setStatus(char status)
	{
		this.status = status;
	}

	public String getContactNo()
	{
		return contactNo;
	}

	public void setContactNo(String contactNo)
	{
		this.contactNo = contactNo;
	}

	public char getOnlineStatus()
	{
		return onlineStatus;
	}

	public void setOnlineStatus(char onlineStatus)
	{
		this.onlineStatus = onlineStatus;
	}

	public String getReason()
	{
		return reason;
	}

	public void setReason(String reason)
	{
		this.reason = reason;
	}

	@Override
	public String toString()
	{
		return "UserDetails: {id:" + id + ", name:" + username + ", email:" + email + ", password:" + password + ", status:" + status + ", address:" + address + ", contact no:" + contactNo + ", role:" + role + ", online status:" + onlineStatus + "}";
	}
}