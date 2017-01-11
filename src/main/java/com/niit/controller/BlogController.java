package com.niit.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.niit.dao.BlogDAO;
import com.niit.dao.UserCommentDAO;
import com.niit.dao.UserDetailsDAO;
import com.niit.model.Blog;
import com.niit.model.UserComment;
import com.niit.model.UserDetails;

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

	Logger log = LoggerFactory.getLogger(BlogController.class);

	private boolean isAdmin()
	{
		log.debug("Method Start: isAdmin");
		UserDetails ud = (UserDetails) session.getAttribute("user");
		log.debug("Method End: isAdmin");
		return ud != null && ud.getRole() == 'A';
	}

	@GetMapping("/Blog")
	public ResponseEntity<List<Blog>> getAllBlogs()
	{
		List<Blog> listBlog = blogDAO.list();
		if(listBlog == null || listBlog.isEmpty())
			return new ResponseEntity<List<Blog>>(HttpStatus.NO_CONTENT);

		return new ResponseEntity<List<Blog>>(listBlog, HttpStatus.OK);
	}

	@GetMapping("/Blog/{id}")
	public ResponseEntity<Blog> getBlog(@PathVariable("id") int id)
	{
		Blog blog = blogDAO.get(id);
		if(blog == null)
			return new ResponseEntity<Blog>(HttpStatus.NOT_FOUND);

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
		blog.setReason("");
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

	@PutMapping("/UpdateBlogAdmin")
	public ResponseEntity<Void> updateBlogAdmin(@RequestBody Blog blog)
	{
		log.debug("Method Start: updateBlogAdmin");
		if(isAdmin())
		{
			if(blogDAO.get(blog.getId()) == null)
			{
				log.debug("Method End: updateBlogAdmin-NOT_FOUND");
				return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
			}
			if(blogDAO.update(blog))
			{
				log.debug("Method End: updateBlogAdmin-OK");
				return new ResponseEntity<Void>(HttpStatus.OK);
			}else
			{
				log.debug("Method End: updateBlogAdmin-CONFLICT");
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			}
		}else
		{
			log.debug("Method End: updateBlogAdmin-FORBIDDEN");
			return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
		}
	}

	@DeleteMapping("/DeleteBlogAdmin/{id}")
	public ResponseEntity<Void> deleteBlogAdmin(@PathVariable("id") int id)
	{
		log.debug("Method Start: deleteBlogAdmin");
		if(isAdmin())
		{
			if(blogDAO.delete(id) && userCommentDAO.deleteAll('B', id))
			{
				log.debug("Method End: deleteBlogAdmin-OK");
				return new ResponseEntity<Void>(HttpStatus.OK);
			}else
			{
				log.debug("Method End: deleteBlogAdmin-NOT_FOUND");
				return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
			}
		}else
		{
			log.debug("Method End: deleteBlogAdmin-FORBIDDEN");
			return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
		}
	}

	@PostMapping("/AcceptRejectBlogAdmin")
	public ResponseEntity<Void> acceptRejectBlogAdmin(@RequestParam("id") int id, @RequestParam("accepted") boolean accepted, @RequestParam("reason") String reason)
	{
		log.debug("Method Start: acceptRejectBlogAdmin");
		if(isAdmin())
		{
			Blog b = blogDAO.get(id);
			if(b == null)
			{
				log.debug("Method End: acceptRejectBlogAdmin-NOT_FOUND");
				return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
			}
			b.setStatus(accepted ? 'Y' : 'R');
			b.setReason(reason);
			if(blogDAO.update(b))
			{
				log.debug("Method End: acceptRejectBlogAdmin-OK");
				return new ResponseEntity<Void>(HttpStatus.OK);
			}else
			{
				log.debug("Method End: acceptRejectBlogAdmin-CONFLICT");
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			}
		}else
		{
			log.debug("Method End: acceptRejectBlogAdmin-FORBIDDEN");
			return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
		}
	}

	@PutMapping("/UpdateBlogCommentAdmin")
	public ResponseEntity<Void> updateBlogCommentAdmin(@RequestBody UserComment userComment)
	{
		log.debug("Method Start: updateBlogCommentAdmin");
		if(isAdmin())
		{
			if(blogDAO.get(userComment.getThreadID()) == null)
			{
				log.debug("Method End: updateBlogCommentAdmin-NOT_FOUND");
				return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
			}
			if(userCommentDAO.update(userComment))
			{
				log.debug("Method End: updateBlogCommentAdmin-OK");
				return new ResponseEntity<Void>(HttpStatus.OK);
			}else
			{
				log.debug("Method End: updateBlogCommentAdmin-CONFLICT");
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			}
		}else
		{
			log.debug("Method End: updateBlogCommentAdmin-FORBIDDEN");
			return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
		}
	}

	@DeleteMapping("/DeleteBlogCommentAdmin/{id}")
	public ResponseEntity<Void> deleteBlogCommentAdmin(@PathVariable("id") int id)
	{
		log.debug("Method Start: deleteBlogCommentAdmin");
		if(isAdmin())
		{
			UserComment c = userCommentDAO.get(id);
			Blog b = blogDAO.get(c.getThreadID());
			b.setTotalComments(b.getTotalComments() - 1);
			if(blogDAO.update(b) && userCommentDAO.delete(id))
			{
				log.debug("Method End: deleteBlogCommentAdmin-OK");
				return new ResponseEntity<Void>(HttpStatus.OK);
			}else
			{
				log.debug("Method End: deleteBlogCommentAdmin-NOT_FOUND");
				return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
			}
		}else
		{
			log.debug("Method End: deleteBlogCommentAdmin-FORBIDDEN");
			return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
		}
	}
}