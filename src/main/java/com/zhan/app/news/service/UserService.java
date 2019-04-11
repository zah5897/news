package com.zhan.app.news.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zhan.app.common.User;
import com.zhan.app.news.dao.UserDao;

@Service
public class UserService {
	@Resource
	private UserDao userDao;

	public long insert(User user) {
		return userDao.save(user);
	}
	 

	public String insertDaoHangUser(User user) {
		return userDao.saveDaoHangUser(user);
	}

	public int countToken(String aid,String token) {
		return userDao.countByToken(aid,token);
	}
	 
 
 
 

	public void deleteByToken(String aid,String token) {
		userDao.deleteByToken(aid,token);
	}

	public User getUser(String aid,String deviceId, String token) {
		return userDao.getUser(aid,deviceId, token);
	}

	public User getUserByDevice(String aid ,String deviceId) {
		return userDao.getUserByDevice(aid,deviceId);
	}

	public User getUserByToken(String aid,String token) {
		return userDao.getUserByToken(aid,token);
	}

}
