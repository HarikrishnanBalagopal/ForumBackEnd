package com.niit.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.niit.dao.ForumDAO;
import com.niit.dao.UserCommentDAO;
import com.niit.dao.UserDetailsDAO;
import com.niit.model.Forum;
import com.niit.model.UserComment;
import com.niit.model.UserDetails;

import oracle.sql.DATE;

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

	Logger log = LoggerFactory.getLogger(ForumController.class);

	private boolean isAdmin()
	{
		log.debug("Method Start: isAdmin");
		UserDetails ud = (UserDetails) session.getAttribute("user");
		log.debug("Method End: isAdmin");
		return ud != null && ud.getRole() == 'A';
	}

	@GetMapping("/Forum")
	public ResponseEntity<List> getAllTopics()
	{
		log.debug("Method Start: getAllTopics");
		List listForum = forumDAO.list();
		if(listForum == null || listForum.isEmpty())
		{
			log.debug("Method End: getAllTopics-NO_CONTENT");
			return new ResponseEntity<List>(HttpStatus.NO_CONTENT);
		}

		log.debug("Method End: getAllTopics-OK");
		return new ResponseEntity<List>(listForum, HttpStatus.OK);
	}

	@GetMapping("/Forum/{id}")
	public ResponseEntity<Forum> getTopic(@PathVariable("id") int id)
	{
		log.debug("Method Start: getTopic");
		Forum forum = forumDAO.get(id);
		if(forum == null)
		{
			log.debug("Method End: getTopic-NO_CONTENT");
			return new ResponseEntity<Forum>(HttpStatus.NO_CONTENT);
		}

		log.debug("Method End: getTopic-OK");
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

	@PostMapping("/CreateThread")
	public ResponseEntity<String> createThread(@RequestBody Forum forum, UriComponentsBuilder ucBuilder)
	{
		UserDetails userDetails = (UserDetails) session.getAttribute("user");
		if(userDetails == null || userDetails.getStatus() != 'Y')
			return new ResponseEntity<String>(HttpStatus.FORBIDDEN);
		forum.setUserID(userDetails.getId());
		if(forum.getTitle() == null || forum.getTitle().equals(""))
			return new ResponseEntity<String>("title", HttpStatus.CONFLICT);
		forum.setLastComment(new Date());
		forum.setTotalComments(0);
		forumDAO.save(forum);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("Forum/{id}/").buildAndExpand(forum.getId()).toUri());
		return new ResponseEntity<String>("created", headers, HttpStatus.CREATED);
	}

	@PostMapping("/DeleteThreadAdmin")
	public ResponseEntity<Void> deleteThreadAdmin(@RequestParam("id") int id)
	{
		if(isAdmin())
		{
			if(forumDAO.delete(id) && userCommentDAO.deleteAll('F', id))
				return new ResponseEntity<Void>(HttpStatus.OK);
			else
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
	}

	@PostMapping("/CreateThreadComment")
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

	@PostMapping("/EditThreadCommentAdmin")
	public ResponseEntity<Void> editComment(@RequestBody UserComment userComment)
	{
		if(isAdmin())
		{
			if(userCommentDAO.update(userComment))
				return new ResponseEntity<Void>(HttpStatus.OK);
			else
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
	}

	@PostMapping("/DeleteThreadCommentAdmin")
	public ResponseEntity<Void> deleteComment(@RequestParam("id") int id)
	{
		if(isAdmin())
		{
			if(userCommentDAO.delete(id))
				return new ResponseEntity<Void>(HttpStatus.OK);
			else
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
	}
}