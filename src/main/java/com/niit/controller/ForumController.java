package com.niit.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.niit.dao.ForumDAO;
import com.niit.dao.UserCommentDAO;
import com.niit.dao.UserDetailsDAO;
import com.niit.model.Forum;
import com.niit.model.UserComment;
import com.niit.model.UserDetails;

@RestController
public class ForumController
{
	@Autowired
	ForumDAO forumDAO;

	@Autowired
	UserDetailsDAO userDetailsDAO;
	
	@Autowired
	UserCommentDAO userCommentDAO;

	@Autowired
	HttpSession session;

	@GetMapping("/Forum")
	public ResponseEntity<List> getAllTopics()
	{
		List listForum = forumDAO.list();
		if(listForum == null || listForum.isEmpty())
			return new ResponseEntity<List>(HttpStatus.NO_CONTENT);

		return new ResponseEntity<List>(listForum, HttpStatus.OK);
	}

	@GetMapping("/Forum/{id}")
	public ResponseEntity<Forum> getTopic(@PathVariable("id") int id)
	{
		Forum forum = forumDAO.get(id);
		if(forum == null)
			return new ResponseEntity<Forum>(HttpStatus.NO_CONTENT);

		return new ResponseEntity<Forum>(forum, HttpStatus.OK);
	}

	@GetMapping("/ForumComments/{id}")
	public ResponseEntity<List<UserComment>> getAllComments(@PathVariable("id") int id)
	{
		List<UserComment> listComments = userCommentDAO.list('F', id);
		if(listComments == null || listComments.isEmpty())
			return new ResponseEntity<List<UserComment>>(HttpStatus.NO_CONTENT);

		return new ResponseEntity<List<UserComment>>(listComments, HttpStatus.OK);
	}

	@PostMapping("/CreateForum")
	public ResponseEntity<Void> createForum(@RequestBody Forum forum, UriComponentsBuilder ucBuilder)
	{
		forumDAO.save(forum);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("Forum/{id}/").buildAndExpand(forum.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	@PostMapping("/CreateComment")
	public ResponseEntity<Void> createComment(@RequestBody UserComment userComment, UriComponentsBuilder ucBuilder)
	{
		UserDetails userDetails = (UserDetails) session.getAttribute("user");
		if(userDetails == null || userDetails.getStatus() != 'Y')
			return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
		Forum forum = forumDAO.get(userComment.getThreadID());
		if(forum == null)
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		userComment.setUserID(userDetails.getId());
		userComment.setType('F');
		userCommentDAO.save(userComment);
		forum.setTotalComments(forum.getTotalComments() + 1);
		forum.setLastComment(userComment.getCreatedOn());
		forumDAO.update(forum);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("Forum/{id}/").buildAndExpand(userComment.getThreadID()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
}