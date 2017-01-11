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

import com.niit.dao.JobApplicationDAO;
import com.niit.dao.JobDAO;
import com.niit.dao.UserDetailsDAO;
import com.niit.model.Job;
import com.niit.model.JobApplication;
import com.niit.model.UserDetails;

@RestController
public class JobController
{
	@Autowired
	JobDAO jobDAO;

	@Autowired
	JobApplicationDAO jobApplicationDAO;

	@Autowired
	UserDetailsDAO userDetailsDAO;

	@Autowired
	HttpSession session;

	Logger log = LoggerFactory.getLogger(JobController.class);

	private boolean isAdmin()
	{
		log.debug("Method Start: isAdmin");
		UserDetails ud = (UserDetails) session.getAttribute("user");
		log.debug("Method End: isAdmin");
		return ud != null && ud.getRole() == 'A';
	}

	@GetMapping("/GetListing/{id}")
	public ResponseEntity<Job> getListing(@PathVariable("id") int id)
	{
		log.debug("Method Start: getListing");
		Job job = jobDAO.get(id);
		if(job != null)
		{
			log.debug("Method End: getListing-OK");
			return new ResponseEntity<Job>(job, HttpStatus.OK);
		}else
		{
			log.debug("Method End: getListing-NOT_FOUND");
			return new ResponseEntity<Job>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/ListingAll")
	public ResponseEntity<List<Job>> getListingAll()
	{
		log.debug("Method Start: getListingAll");
		List<Job> l = jobDAO.list();
		if(l == null || l.isEmpty())
		{
			log.debug("Method End: getListingAll-NO_CONTENT");
			return new ResponseEntity<List<Job>>(HttpStatus.NO_CONTENT);
		}else
		{
			log.debug("Method End: getListingAll-OK");
			return new ResponseEntity<List<Job>>(l, HttpStatus.OK);
		}
	}

	@PostMapping("/CreateListingAdmin")
	public ResponseEntity<String> createListingAdmin(@RequestBody Job job, UriComponentsBuilder ucBuilder)
	{
		log.debug("Method Start: createListingAdmin");
		if(isAdmin())
		{
			if(jobDAO.save(job))
			{
				HttpHeaders headers = new HttpHeaders();
				headers.setLocation(ucBuilder.path("GetListing/{id}/").buildAndExpand(job.getId()).toUri());
				log.debug("Method End: createListingAdmin-created");
				return new ResponseEntity<String>("created", headers, HttpStatus.OK);
			}else
			{
				log.debug("Method End: createListingAdmin-exception");
				return new ResponseEntity<String>("exception", HttpStatus.CONFLICT);
			}
		}else
		{
			log.debug("Method End: createListingAdmin-FORBIDDEN");
			return new ResponseEntity<String>(HttpStatus.FORBIDDEN);
		}
	}

	@PutMapping("/UpdateListingAdmin")
	public ResponseEntity<Void> updateListingAdmin(@RequestBody Job job)
	{
		log.debug("Method Start: updateListingAdmin");
		if(isAdmin())
		{
			if(jobDAO.get(job.getId()) == null)
			{
				log.debug("Method End: updateListingAdmin-NOT_FOUND");
				return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
			}
			if(jobDAO.update(job))
			{
				log.debug("Method End: updateListingAdmin-OK");
				return new ResponseEntity<Void>(HttpStatus.OK);
			}else
			{
				log.debug("Method End: updateListingAdmin-CONFLICT");
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			}
		}else
		{
			log.debug("Method End: updateListingAdmin-FORBIDDEN");
			return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
		}
	}

	@DeleteMapping("/DeleteListingAdmin/{id}")
	public ResponseEntity<Void> deleteListingAdmin(@PathVariable("id") int id)
	{
		log.debug("Method Start: deleteListingAdmin");
		if(isAdmin())
		{
			if(jobDAO.delete(id) && jobApplicationDAO.deleteAll(id))
			{
				log.debug("Method End: deleteListingAdmin-OK");
				return new ResponseEntity<Void>(HttpStatus.OK);
			}else
			{
				log.debug("Method End: deleteListingAdmin-NOT_FOUND");
				return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
			}
		}else
		{
			log.debug("Method End: deleteListingAdmin-FORBIDDEN");
			return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
		}
	}

	@GetMapping("/GetApplication/{id}")
	public ResponseEntity<JobApplication> getApplication(@PathVariable("id") int id)
	{
		log.debug("Method Start: getApplication");
		JobApplication jobApplication = jobApplicationDAO.get(id);
		if(jobApplication != null)
		{
			log.debug("Method End: getApplication-OK");
			return new ResponseEntity<JobApplication>(jobApplication, HttpStatus.OK);
		}else
		{
			log.debug("Method End: getApplication-NOT_FOUND");
			return new ResponseEntity<JobApplication>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/ApplicationAllUser")
	public ResponseEntity<List<JobApplication>> getApplicationAllUser()
	{
		log.debug("Method Start: getApplicationAllUser");
		UserDetails userDetails = (UserDetails) session.getAttribute("user");
		if(userDetails == null || userDetails.getStatus() != 'Y')
		{
			log.debug("Method End: getApplicationAllUser-FORBIDDEN");
			return new ResponseEntity<List<JobApplication>>(HttpStatus.FORBIDDEN);
		}
		List<JobApplication> l = jobApplicationDAO.list(userDetails.getId());
		if(l == null || l.isEmpty())
		{
			log.debug("Method End: getApplicationAllUser-NO_CONTENT");
			return new ResponseEntity<List<JobApplication>>(HttpStatus.NO_CONTENT);
		}else
		{
			log.debug("Method End: getApplicationAllUser-OK");
			return new ResponseEntity<List<JobApplication>>(l, HttpStatus.OK);
		}
	}

	@GetMapping("/ApplicationAllAdmin")
	public ResponseEntity<List<JobApplication>> getApplicationAllAdmin()
	{
		log.debug("Method Start: getApplicationAllAdmin");
		if(isAdmin())
		{
			List<JobApplication> l = jobApplicationDAO.list();
			log.debug("Method End: getApplicationAllAdmin-OK");
			return new ResponseEntity<List<JobApplication>>(l, HttpStatus.OK);
		}else
		{
			log.debug("Method End: getApplicationAllAdmin-FORBIDDEN");
			return new ResponseEntity<List<JobApplication>>(HttpStatus.FORBIDDEN);
		}
	}

	@PostMapping("/CreateApplication")
	public ResponseEntity<String> createApplication(@RequestBody JobApplication jobApplication, UriComponentsBuilder ucBuilder)
	{
		log.debug("Method Start: createApplication");
		UserDetails userDetails = (UserDetails) session.getAttribute("user");
		if(userDetails == null || userDetails.getStatus() != 'Y')
		{
			log.debug("Method End: createApplication-FORBIDDEN");
			return new ResponseEntity<String>(HttpStatus.FORBIDDEN);
		}
		if(jobDAO.get(jobApplication.getJobID()) == null)
		{
			log.debug("Method End: createApplication-NOT_FOUND");
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		jobApplication.setUserID(userDetails.getId());
		jobApplication.setStatus('N');
		if(jobApplicationDAO.save(jobApplication))
		{
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(ucBuilder.path("GetApplication/{id}/").buildAndExpand(jobApplication.getId()).toUri());
			log.debug("Method End: createApplication-created");
			return new ResponseEntity<String>("created", headers, HttpStatus.OK);
		}else
		{
			log.debug("Method End: createApplication-exception");
			return new ResponseEntity<String>("exception", HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/UpdateApplicationAdmin")
	public ResponseEntity<Void> updateApplicationAdmin(@RequestBody JobApplication jobApplication)
	{
		log.debug("Method Start: updateApplicationAdmin");
		if(isAdmin())
		{
			if(jobApplicationDAO.get(jobApplication.getId()) == null)
			{
				log.debug("Method End: updateApplicationAdmin-NOT_FOUND");
				return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
			}
			if(jobApplicationDAO.update(jobApplication))
			{
			 	log.debug("Method End: updateApplicationAdmin-OK");
				return new ResponseEntity<Void>(HttpStatus.OK);
			}else
			{
				log.debug("Method End: updateApplicationAdmin-CONFLICT");
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			}
		}else
		{
			log.debug("Method End: updateApplicationAdmin-FORBIDDEN");
			return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
		}
	}

	@DeleteMapping("/DeleteApplicationAdmin/{id}")
	public ResponseEntity<Void> deleteApplicationAdmin(@PathVariable("id") int id)
	{
		log.debug("Method Start: deleteApplicationAdmin");
		if(isAdmin())
		{
			if(jobApplicationDAO.delete(id))
			{
				log.debug("Method End: deleteApplicationAdmin-OK");
				return new ResponseEntity<Void>(HttpStatus.OK);
			}else
			{
				log.debug("Method End: deleteApplicationAdmin-NOT_FOUND");
				return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
			}
		}else
		{
			log.debug("Method End: deleteApplicationAdmin-FORBIDDEN");
			return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
		}
	}
}