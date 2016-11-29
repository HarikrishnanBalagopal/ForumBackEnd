package com.niit.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.niit.dao.FriendDAO;
import com.niit.dao.UserDetailsDAO;
import com.niit.model.Friend;
import com.niit.model.UserDetails;

@RestController
public class FriendController
{
	@Autowired
	FriendDAO friendDAO;

	@Autowired
	UserDetailsDAO userDetailsDAO;
	
	@Autowired
	HttpSession session;

	Logger log = LoggerFactory.getLogger(FriendController.class);

	@PostMapping("/RequestFriend")
	public ResponseEntity<Void> requestFriend(@RequestParam("id") int id)
	{
		log.debug("Method Start: requestFriend");
		UserDetails userDetails = (UserDetails)session.getAttribute("user");
		if(userDetails == null || userDetails.getStatus() != 'Y')
		{
			log.debug("Method End: requestFriend-FORBIDDEN");
			return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
		}
		UserDetails friendDetails = userDetailsDAO.get(id);
		if(friendDetails == null || friendDetails.getStatus() != 'Y')
		{
			log.debug("Method End: requestFriend-NOT_FOUND");
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		if(friendDAO.request(userDetails.getId(), friendDetails.getId()))
		{
			log.debug("Method End: requestFriend-OK");
			return new ResponseEntity<Void>(HttpStatus.OK);	
		}
		else
		{
			log.debug("Method End: requestFriend-CONFLICT");
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}	
	}
	
	@PostMapping("/UpdateFriendRequest")
	public ResponseEntity<Void> updateFriendRequest(@RequestParam("id") int id, @RequestParam("accepted") boolean accepted)
	{
		log.debug("Method Start: updateFriendRequest");
		UserDetails userDetails = (UserDetails)session.getAttribute("user");
		if(userDetails == null || userDetails.getStatus() != 'Y')
		{
			log.debug("Method End: updateFriendRequest-FORBIDDEN");
			return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
		}
		UserDetails friendDetails = userDetailsDAO.get(id);
		if(friendDetails == null || friendDetails.getStatus() != 'Y')
		{
			log.debug("Method End: updateFriendRequest-NOT_FOUND");
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		if(friendDAO.updateRequest(userDetails.getId(), friendDetails.getId(), accepted))
		{
			log.debug("Method End: updateFriendRequest-OK");
			return new ResponseEntity<Void>(HttpStatus.OK);	
		}
		else
		{
			log.debug("Method End: updateFriendRequest-CONFLICT");
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}	
	}
}