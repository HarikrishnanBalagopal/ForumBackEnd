package com.niit.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.niit.dao.FriendDAO;
import com.niit.model.Friend;

@Repository("friendDAO")
public class FriendDAOImpl implements FriendDAO
{
	@Autowired
	Friend friend;

	@Autowired
	private SessionFactory sessionFactory;

	public FriendDAOImpl(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	@Transactional
	public List<Friend> list()
	{
		String hql = "from Friend";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		@SuppressWarnings("unchecked")
		List<Friend> list = (List<Friend>) query.list();

		return list;
	}

	@Transactional
	public Friend get(int id)
	{
		return sessionFactory.getCurrentSession().get(Friend.class, id);
	}

	@Transactional
	public Friend get(int userID, int friendID)
	{
		String hql = "from Friend where userID = " + userID + " and friendID = " + friendID;
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		List<Friend> list = (List<Friend>) query.list();
		if(list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	@Transactional
	public List<Friend> getAll(int userID)
	{
		String hql = "from Friend where userID = " + userID;
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<Friend>) query.list();
	}

	// 0 and 1 = New request
	// 0 = Sender, 1 = Receiver
	// Y = Accepted, R = Rejected
	@Transactional
	public boolean request(int senderID, int receiverID)
	{
		Friend friend = get(senderID, receiverID);
		if(friend != null)
			return false;

		Friend f1 = new Friend();
		f1.setUserID(senderID);
		f1.setFriendID(receiverID);
		f1.setStatus('0');
		save(f1);

		Friend f2 = new Friend();
		f2.setUserID(receiverID);
		f2.setFriendID(senderID);
		f2.setStatus('1');
		save(f2);
		return true;
	}

	@Transactional
	public boolean updateRequest(int userID, int friendID, boolean accepted)
	{
		char accept = accepted ? 'Y' : 'R';
		Friend f1 = get(userID, friendID);
		if(f1 == null || friend.getStatus() != '1')
			return false;
		f1.setStatus(accept);
		update(f1);

		Friend f2 = get(friendID, userID);
		f2.setStatus(accept);
		update(f2);
		
		return true;
	}

	@Transactional
	public boolean save(Friend friend)
	{
		try
		{
			sessionFactory.getCurrentSession().save(friend);
		}catch(Exception e)
		{
			System.out.println("Exception on saving Friend: " + e);
			return false;
		}

		return true;
	}

	@Transactional
	public boolean update(Friend friend)
	{
		try
		{
			sessionFactory.getCurrentSession().update(friend);
		}catch(Exception e)
		{
			System.out.println("Exception on updating Friend: " + e);
			return false;
		}

		return true;
	}

	@Transactional
	public boolean delete(int id)
	{
		friend.setId(id);

		try
		{
			sessionFactory.getCurrentSession().delete(friend);
		}catch(Exception e)
		{
			System.out.println("Exception on deleting Friend: " + e);
			return false;
		}

		return true;
	}
}