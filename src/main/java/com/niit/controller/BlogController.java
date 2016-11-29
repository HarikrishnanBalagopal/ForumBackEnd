package com.niit.controller;

import java.util.Date;
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

import com.niit.dao.BlogDAO;
import com.niit.dao.UserCommentDAO;
import com.niit.dao.UserDetailsDAO;
import com.niit.model.Blog;
import com.niit.model.UserComment;
import com.niit.model.UserDetails;

import oracle.sql.DATE;

@RestController
public class BlogController
{
	@Autowired
	BlogDAO blogDAO;

	@Autowired
	UserDetailsDAO userDetailsDAO;

	@Autowired
	UserCommentDAO userCommentDAO;

	@Autowired
	HttpSession session;

	@GetMapping("/Blog")
	public ResponseEntity<List> getAllBlogs()
	{
		List listBlog = blogDAO.list();
		if(listBlog == null || listBlog.isEmpty())
			return new ResponseEntity<List>(HttpStatus.NO_CONTENT);

		return new ResponseEntity<List>(listBlog, HttpStatus.OK);
	}

	@GetMapping("/Blog/{id}")
	public ResponseEntity<Blog> getBlog(@PathVariable("id") int id)
	{
		Blog blog = blogDAO.get(id);
		if(blog == null)
			return new ResponseEntity<Blog>(HttpStatus.NO_CONTENT);

		return new ResponseEntity<Blog>(blog, HttpStatus.OK);
	}

	@GetMapping("/BlogComments/{id}")
	public ResponseEntity<List<UserComment>> getAllComments(@PathVariable("id") int id)
	{
		List<UserComment> listComments = userCommentDAO.list('B', id);
		if(listComments == null || listComments.isEmpty())
			return new ResponseEntity<List<UserComment>>(HttpStatus.NO_CONTENT);

		return new ResponseEntity<List<UserComment>>(listComments, HttpStatus.OK);
	}

	@PostMapping("/CreateBlog")
	public ResponseEntity<String> createBlog(@RequestBody Blog blog, UriComponentsBuilder ucBuilder)
	{
		UserDetails userDetails = (UserDetails) session.getAttribute("user");
		if(userDetails == null || userDetails.getStatus() != 'Y')
			return new ResponseEntity<String>(HttpStatus.FORBIDDEN);
		blog.setUserID(userDetails.getId());
		if(blog.getTitle() == null || blog.getTitle().equals(""))
			return new ResponseEntity<String>("title", HttpStatus.CONFLICT);
		blog.setStatus('N');
		blog.setLikes(0);
		blog.setDislikes(0);
		blog.setTotalComments(0);
		blogDAO.save(blog);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("Blog/{id}/").buildAndExpand(blog.getId()).toUri());
		return new ResponseEntity<String>("created", headers, HttpStatus.CREATED);
	}

	@PostMapping("/CreateBlogComment")
	public ResponseEntity<Void> createComment(@RequestBody UserComment userComment, UriComponentsBuilder ucBuilder)
	{
		UserDetails userDetails = (UserDetails) session.getAttribute("user");
		if(userDetails == null || userDetails.getStatus() != 'Y')
			return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
		Blog blog = blogDAO.get(userComment.getThreadID());
		if(blog == null)
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		userComment.setUserID(userDetails.getId());
		userComment.setType('B');
		userCommentDAO.save(userComment);
		blog.setTotalComments(blog.getTotalComments() + 1);
		blogDAO.update(blog);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("Blog/{id}/").buildAndExpand(userComment.getThreadID()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
}