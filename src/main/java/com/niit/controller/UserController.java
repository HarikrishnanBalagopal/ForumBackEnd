package com.niit.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.niit.dao.UserDetailsDAO;
import com.niit.model.UserDetails;

@RestController
public class UserController
{
	@Autowired
	UserDetailsDAO userDetailsDAO;

	@Autowired
	HttpSession session;

	private boolean isAdmin()
	{
		UserDetails ud = (UserDetails) session.getAttribute("user");
		return ud != null && ud.getRole() == 'A';
	}

	@PostMapping("/GetUsername")
	public ResponseEntity<String> getUsername(@RequestParam("id") int id)
	{
		return new ResponseEntity<String>(userDetailsDAO.get(id).getUsername(), HttpStatus.OK);
	}

	@GetMapping("/UserDetailsAdmin/{id}")
	public ResponseEntity<UserDetails> getUserDetailsAdmin(@PathVariable("id") int id)
	{
		if(isAdmin())
			return new ResponseEntity<UserDetails>(userDetailsDAO.get(id), HttpStatus.OK);
		else
			return new ResponseEntity<UserDetails>(HttpStatus.FORBIDDEN);
	}

	@GetMapping("/UserDetails/{username}")
	public ResponseEntity<UserDetails> getUserDetails(@PathVariable("username") String username)
	{
		UserDetails userDetails = userDetailsDAO.get(username);
		if(userDetails != null)
			userDetails.setPassword("");
		return new ResponseEntity<UserDetails>(userDetails, HttpStatus.OK);
	}

	@PostMapping("/CheckEmail")
	public ResponseEntity<Boolean> checkEmail(@RequestParam("email") String email)
	{
		if(userDetailsDAO.getByEmail(email) != null)
			return new ResponseEntity<Boolean>(false, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}

	@PostMapping("/CheckUsername")
	public ResponseEntity<Boolean> checkUsername(@RequestParam("username") String username)
	{
		if(userDetailsDAO.get(username) != null)
			return new ResponseEntity<Boolean>(false, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}

	@PostMapping("/Login")
	public ResponseEntity<String> login(@RequestParam("username") String username, @RequestParam("password") String password)
	{
		UserDetails userDetails = userDetailsDAO.login(username, password);
		if(userDetails != null)
		{
			if(userDetails.getStatus() == 'Y')
			{
				userDetails.setOnlineStatus('Y');
				userDetailsDAO.update(userDetails);
				session.setAttribute("user", userDetails);
				return new ResponseEntity<String>("success", HttpStatus.OK);
			}else if(userDetails.getStatus() == 'N')
				return new ResponseEntity<String>("registration not yet accepted", HttpStatus.OK);
			else
				return new ResponseEntity<String>("registration rejected:" + userDetails.getReason(), HttpStatus.OK);
		}
		return new ResponseEntity<String>("invalid credentials", HttpStatus.OK);
	}

	@GetMapping("/Logout")
	public ResponseEntity<Void> logout()
	{
		session.invalidate();
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@PostMapping("/Register")
	public ResponseEntity<String> register(@RequestBody UserDetails userDetails, UriComponentsBuilder ucBuilder)
	{
		if(userDetailsDAO.getByEmail(userDetails.getEmail()) != null)
			return new ResponseEntity<String>("email", HttpStatus.CONFLICT);
		if(userDetailsDAO.get(userDetails.getUsername()) != null)
			return new ResponseEntity<String>("username", HttpStatus.CONFLICT);
		if(userDetails.getRole() != 'S' && userDetails.getRole() != 'P')
			return new ResponseEntity<String>("role", HttpStatus.CONFLICT);
		userDetails.setStatus('N');
		if(userDetailsDAO.save(userDetails))
		{
			session.setAttribute("user", userDetails);
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(ucBuilder.path("UserDetailsAdmin/{id}/").buildAndExpand(userDetails.getId()).toUri());
			return new ResponseEntity<String>("created", headers, HttpStatus.OK);
		}else
			return new ResponseEntity<String>("exception", HttpStatus.CONFLICT);
	}

	@PostMapping("/Approve")
	public ResponseEntity<Void> approveUser(@RequestParam("id") int id)
	{
		if(isAdmin())
		{
			UserDetails userDetails = userDetailsDAO.get(id);
			userDetails.setStatus('Y');
			userDetailsDAO.update(userDetails);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
	}

	@PostMapping("/Reject")
	public ResponseEntity<Void> rejectUser(@RequestParam("id") int id, @RequestParam("reason") String reason)
	{
		if(isAdmin())
		{
			UserDetails userDetails = userDetailsDAO.get(id);
			userDetails.setStatus('R');
			userDetails.setReason(reason);
			userDetailsDAO.update(userDetails);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
	}
}