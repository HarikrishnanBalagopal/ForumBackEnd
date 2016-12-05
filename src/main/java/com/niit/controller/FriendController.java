package com.niit.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	@GetMapping("/GetFriendList")
	public ResponseEntity<List> getFriendList()
	{
		log.debug("Method Start: getFriendList");
		UserDetails userDetails = (UserDetails) session.getAttribute("user");
		if(userDetails == null || userDetails.getStatus() != 'Y')
		{
			log.debug("Method End: getFriendList-FORBIDDEN");
			return new ResponseEntity<List>(HttpStatus.FORBIDDEN);
		}
		List l = friendDAO.list(userDetails.getId());
		if(l == null || l.isEmpty())
		{
			log.debug("Method End: getFriendList-NO_CONTENT");
			return new ResponseEntity<List>(HttpStatus.NO_CONTENT);
		}

		log.debug("Method End: getFriendList-OK");
		return new ResponseEntity<List>(l, HttpStatus.OK);
	}

	@PostMapping("/RequestFriend")
	public ResponseEntity<Void> requestFriend(@RequestParam("id") int id)
	{
		log.debug("Method Start: requestFriend");
		UserDetails userDetails = (UserDetails) session.getAttribute("user");
		if(userDetails == null || userDetails.getStatus() != 'Y' || userDetails.getId() == id)
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
		}else
		{
			log.debug("Method End: requestFriend-CONFLICT");
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
	}

	@PostMapping("/UpdateFriendRequest")
	public ResponseEntity<Void> updateFriendRequest(@RequestParam("id") int id, @RequestParam("accepted") boolean accepted)
	{
		log.debug("Method Start: updateFriendRequest");
		UserDetails userDetails = (UserDetails) session.getAttribute("user");
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
		}else
		{
			log.info("LoggedIn:" + userDetails.toString());
			log.info("Friend:" + friendDetails.toString());
			log.debug("Method End: updateFriendRequest-CONFLICT");
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
	}
}