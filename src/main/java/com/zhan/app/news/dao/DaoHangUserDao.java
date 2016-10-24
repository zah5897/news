package com.zhan.app.news.dao;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.zhan.app.common.User;

@Repository("daoHanguserDao")
public class DaoHangUserDao extends BaseDao {
	
	private static final String collectionName="push_daohang_user";
	@Resource
	protected MongoTemplate mongoTemplate;

	public String save(User user) {
		mongoTemplate.save(user,collectionName);
		return user.getId();
	}
	 
	public long countByToken(String token) {
		Query query = new Query();
		Criteria criteria = Criteria.where("token").is(token);
		query.addCriteria(criteria);
		return mongoTemplate.count(query,  User.class,collectionName);
	}

	public long countByDevice(String deviceId) {
		Query query = new Query();
		Criteria criteria = Criteria.where("deviceId").is(deviceId);
		query.addCriteria(criteria);
		return mongoTemplate.count(query, User.class,collectionName);
	}

	public void deleteByDevice(String deviceID) {
		mongoTemplate.remove(new Query(new Criteria("deviceId").is(deviceID)), User.class,collectionName);
	}

	public void deleteByToken(String token) {
		mongoTemplate.remove(new Query(new Criteria("token").is(token)), User.class,collectionName);
	}

	public User getUser(String deviceId, String token) {
		Query query = new Query();
		Criteria criteria = Criteria.where("deviceId").is(deviceId).and("token").is("token");
		query.addCriteria(criteria);
		return mongoTemplate.findOne(query, User.class,collectionName);
	}

	public User getUserByDevice(String deviceId) {
		Query query = new Query();
		Criteria criteria = Criteria.where("deviceId").is(deviceId);
		query.addCriteria(criteria);
		return mongoTemplate.findOne(query, User.class,collectionName);
	}

	public User getUserByToken(String token) {
		Query query = new Query();
		Criteria criteria = Criteria.where("token").is(token);
		query.addCriteria(criteria);
		return mongoTemplate.findOne(query,User.class,collectionName);
	}

	 



}
