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
import com.niit.model.Forum;

@RestController
public class ForumController
{
	@Autowired
	ForumDAO forumDAO;

	@Autowired
	HttpSession session;

	@GetMapping("/Forum")
	public ResponseEntity<List> getAllTopics()
	{
		List listForum = forumDAO.list();
		if(listForum.isEmpty())
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

	@PostMapping("/CreateForum")
	public ResponseEntity<Void> createForum(@RequestBody Forum forum, UriComponentsBuilder ucBuilder)
	{
		forumDAO.save(forum);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("Forum/{id}/").buildAndExpand(forum.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
}