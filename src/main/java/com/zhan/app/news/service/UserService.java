package com.zhan.app.news.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zhan.app.common.User;
import com.zhan.app.news.dao.UserDao;

@Service
public class UserService {
	@Resource
	private UserDao userDao;

	public String insert(User user) {
		return userDao.save(user);
	}

	public long countToken(String token) {
		return userDao.countByToken(token);
	}

	public long countDevice(String deviceID) {
		return userDao.countByDevice(deviceID);
	}

	public void deleteByDetive(String deviceID) {
		userDao.deleteByDevice(deviceID);
	}

	public void deleteByToken(String token) {
		userDao.deleteByToken(token);
	}
	
	public User getUser(String deviceId,String token){
		return userDao.getUser(deviceId,token);
	}
	public User getUserByDevice(String deviceId){
		return userDao.getUserByDevice(deviceId);
	}
	public User getUserByToken(String token){
		return userDao.getUserByToken(token);
	}
	
 
}
