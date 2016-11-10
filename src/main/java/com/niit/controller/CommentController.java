package com.niit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.niit.dao.UserCommentDAO;
import com.niit.model.UserComment;

public class CommentController
{
	@Autowired
	UserCommentDAO userCommentDAO;

	@PostMapping("/CommentForum")
	public void commentForum(@RequestBody UserComment comment)
	{

	}

	@PostMapping("/CommentBlog")
	public void commentBlog(@RequestBody UserComment comment)
	{

	}
}