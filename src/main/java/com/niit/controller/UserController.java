package com.niit.controller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.niit.dao.UserDetailsDAO;
import com.niit.model.UserDetails;
import com.niit.util.FileUtil;

@RestController
public class UserController
{
	@Autowired
	UserDetailsDAO userDetailsDAO;

	@Autowired
	HttpSession session;

	Logger log = LoggerFactory.getLogger(UserController.class);

	private boolean isAdmin()
	{
		log.debug("Method Start: isAdmin");
		UserDetails ud = (UserDetails) session.getAttribute("user");
		log.debug("Method End: isAdmin");
		return ud != null && ud.getRole() == 'A';
	}

	@PostMapping("/UploadImage")
	public ResponseEntity<Void> uploadImage(@RequestParam("file") MultipartFile file, HttpServletRequest request)
	{
		log.debug("Method Start: uploadImage");
		UserDetails userDetails = (UserDetails) session.getAttribute("user");
		if(userDetails == null || userDetails.getStatus() != 'Y')
		{
			log.debug("Method End: uploadImage-FORBIDDEN");
			return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
		}

		if(!file.isEmpty())
		{
			ServletContext context = request.getServletContext();
			String path = context.getRealPath("./resources/images/" + userDetails.getId() + ".jpg");

			log.info("Path = " + path);
			log.info("File name = " + file.getOriginalFilename());

			FileUtil.saveImage(file, path);
			log.debug("Method End: uploadImage-OK");
			return new ResponseEntity<Void>(HttpStatus.OK);
		}

		log.debug("Method End: uploadImage-CONFLICT");
		return new ResponseEntity<Void>(HttpStatus.CONFLICT);
	}

	@PostMapping("/GetUsername")
	public ResponseEntity<String> getUsername(@RequestParam("id") int id)
	{
		log.debug("Method Start: getUsername");
		log.debug("Method End: getUsername");
		return new ResponseEntity<String>(userDetailsDAO.get(id).getUsername(), HttpStatus.OK);
	}

	@GetMapping("/UserDetailsIDAdmin/{id}")
	public ResponseEntity<UserDetails> getUserDetailsIDAdmin(@PathVariable("id") int id)
	{
		log.debug("Method Start: getUserDetailsIDAdmin");
		if(isAdmin())
		{
			UserDetails userDetails = userDetailsDAO.get(id);
			if(userDetails != null)
			{
				log.debug("Method End: getUserDetailsIDAdmin-OK");
				return new ResponseEntity<UserDetails>(userDetails, HttpStatus.OK);
			}else
			{
				log.debug("Method End: getUserDetailsIDAdmin-NOT_FOUND");
				return new ResponseEntity<UserDetails>(userDetails, HttpStatus.NOT_FOUND);
			}
		}else
		{
			log.debug("Method End: getUserDetailsIDAdmin-FORBIDDEN");
			return new ResponseEntity<UserDetails>(HttpStatus.FORBIDDEN);
		}
	}

	@GetMapping("/UserDetailsAll")
	public ResponseEntity<List<UserDetails>> getUserDetailsAll()
	{
		log.debug("Method Start: getUserDetailsAll");

		List<UserDetails> l = userDetailsDAO.list();
		for(UserDetails d : l)
			d.setPassword("");
		log.debug("Method End: getUserDetailsAllAdmin-OK");
		return new ResponseEntity<List<UserDetails>>(l, HttpStatus.OK);
	}

	@GetMapping("/UserDetailsAllAdmin")
	public ResponseEntity<List<UserDetails>> getUserDetailsAllAdmin()
	{
		log.debug("Method Start: getUserDetailsAllAdmin");
		if(isAdmin())
		{
			log.debug("Method End: getUserDetailsAllAdmin-OK");
			return new ResponseEntity<List<UserDetails>>(userDetailsDAO.list(), HttpStatus.OK);
		}else
		{
			log.debug("Method End: getUserDetailsAllAdmin-FORBIDDEN");
			return new ResponseEntity<List<UserDetails>>(HttpStatus.FORBIDDEN);
		}
	}

	@GetMapping("/UserDetailsID/{id}")
	public ResponseEntity<UserDetails> getUserDetailsID(@PathVariable("id") int id)
	{
		log.debug("Method Start: getUserDetailsID");
		UserDetails userDetails = userDetailsDAO.get(id);
		if(userDetails != null)
		{
			userDetails.setPassword("");
			log.debug("Method End: getUserDetailsID-OK");
			return new ResponseEntity<UserDetails>(userDetails, HttpStatus.OK);
		}else
		{
			log.debug("Method End: getUserDetailsID-NOT_FOUND");
			return new ResponseEntity<UserDetails>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/UserDetails/{username}")
	public ResponseEntity<UserDetails> getUserDetails(@PathVariable("username") String username)
	{
		log.debug("Method Start: getUserDetails");
		UserDetails userDetails = userDetailsDAO.get(username);
		if(userDetails != null)
		{
			userDetails.setPassword("");
			log.debug("Method End: getUserDetails-OK");
			return new ResponseEntity<UserDetails>(userDetails, HttpStatus.OK);
		}else
		{
			log.debug("Method End: getUserDetails-NOT_FOUND");
			return new ResponseEntity<UserDetails>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/CheckEmail")
	public ResponseEntity<Boolean> checkEmail(@RequestParam("email") String email)
	{
		log.debug("Method Start: checkEmail");
		if(userDetailsDAO.getByEmail(email) != null)
		{
			log.debug("Method End: checkEmail-false");
			return new ResponseEntity<Boolean>(false, HttpStatus.OK);
		}else
		{
			log.debug("Method End: checkEmail-true");
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}
	}

	@PostMapping("/CheckUsername")
	public ResponseEntity<Boolean> checkUsername(@RequestParam("username") String username)
	{
		log.debug("Method Start: checkUsername");
		if(userDetailsDAO.get(username) != null)
		{
			log.debug("Method End: checkUsername-false");
			return new ResponseEntity<Boolean>(false, HttpStatus.OK);
		}else
		{
			log.debug("Method End: checkUsername-true");
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}
	}

	@PostMapping("/Login")
	public ResponseEntity<String> login(@RequestParam("username") String username, @RequestParam("password") String password)
	{
		log.debug("Method Start: login");
		UserDetails userDetails = userDetailsDAO.login(username, password);
		if(userDetails != null)
		{
			if(userDetails.getStatus() == 'Y')
			{
				userDetails.setOnlineStatus('Y');
				userDetailsDAO.update(userDetails);
				session.setAttribute("user", userDetails);
				log.debug("Method End: login-success");
				return new ResponseEntity<String>("success", HttpStatus.OK);
			}else if(userDetails.getStatus() == 'N')
			{
				log.debug("Method End: login-registration not yet accepted");
				return new ResponseEntity<String>("registration not yet accepted", HttpStatus.OK);
			}else
			{
				log.debug("Method End: login-registration rejected:" + userDetails.getReason());
				return new ResponseEntity<String>("registration rejected:" + userDetails.getReason(), HttpStatus.OK);
			}
		}
		log.debug("Method End: login-invalid credentials");
		return new ResponseEntity<String>("invalid credentials", HttpStatus.OK);
	}

	@GetMapping("/Logout")
	public ResponseEntity<Void> logout()
	{
		log.debug("Method Start: logout");
		UserDetails user = (UserDetails) session.getAttribute("user");
		if(user != null)
		{
			log.info("User is logged in, logging out");
			user.setOnlineStatus('N');
			userDetailsDAO.update(user);
		}
		session.invalidate();
		log.debug("Method End: logout");
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@PostMapping("/Register")
	public ResponseEntity<String> register(@RequestBody UserDetails userDetails, UriComponentsBuilder ucBuilder)
	{
		log.debug("Method Start: register");
		if(userDetailsDAO.getByEmail(userDetails.getEmail()) != null)
		{
			log.debug("Method End: register-email");
			return new ResponseEntity<String>("email", HttpStatus.CONFLICT);
		}
		if(userDetailsDAO.get(userDetails.getUsername()) != null)
		{
			log.debug("Method End: register-username");
			return new ResponseEntity<String>("username", HttpStatus.CONFLICT);
		}
		if(userDetails.getRole() != 'S' && userDetails.getRole() != 'P')
		{
			log.debug("Method End: register-role");
			return new ResponseEntity<String>("role", HttpStatus.CONFLICT);
		}
		userDetails.setStatus('N');
		userDetails.setOnlineStatus('N');
		if(userDetailsDAO.save(userDetails))
		{
			session.setAttribute("user", userDetails);
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(ucBuilder.path("UserDetailsAdmin/{id}/").buildAndExpand(userDetails.getId()).toUri());
			log.debug("Method End: register-created");
			return new ResponseEntity<String>("created", headers, HttpStatus.OK);
		}else
		{
			log.debug("Method End: register-exception");
			return new ResponseEntity<String>("exception", HttpStatus.CONFLICT);
		}
	}

	@PostMapping("/Approve")
	public ResponseEntity<Void> approveUser(@RequestParam("id") int id)
	{
		log.debug("Method Start: approveUser");
		if(isAdmin())
		{
			UserDetails userDetails = userDetailsDAO.get(id);
			userDetails.setStatus('Y');
			userDetailsDAO.update(userDetails);
			log.debug("Method End: approveUser-OK");
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		log.debug("Method End: approveUser-FORBIDDEN");
		return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
	}

	@PostMapping("/Reject")
	public ResponseEntity<Void> rejectUser(@RequestParam("id") int id, @RequestParam("reason") String reason)
	{
		log.debug("Method Start: rejectUser");
		if(isAdmin())
		{
			UserDetails userDetails = userDetailsDAO.get(id);
			userDetails.setStatus('R');
			userDetails.setReason(reason);
			userDetailsDAO.update(userDetails);
			log.debug("Method End: rejectUser-OK");
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		log.debug("Method End: rejectUser-FORBIDDEN");
		return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
	}
}