package com.zhan.app.news.dao;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.zhan.app.common.DaoHangUser;
import com.zhan.app.common.User;

@Repository("daoHanguserDao")
public class DaoHangUserDao extends BaseDao {
	@Resource
	protected MongoTemplate mongoTemplate;

	public String save(DaoHangUser user) {
		mongoTemplate.save(user);
		return user.getId();
	}
	 
	public long countByToken(String token) {
		Query query = new Query();
		Criteria criteria = Criteria.where("token").is(token);
		query.addCriteria(criteria);
		return mongoTemplate.count(query, DaoHangUser.class);
	}

	public long countByDevice(String deviceId) {
		Query query = new Query();
		Criteria criteria = Criteria.where("deviceId").is(deviceId);
		query.addCriteria(criteria);
		return mongoTemplate.count(query, DaoHangUser.class);
	}

	public void deleteByDevice(String deviceID) {
		mongoTemplate.remove(new Query(new Criteria("deviceId").is(deviceID)), DaoHangUser.class);
	}

	public void deleteByToken(String token) {
		mongoTemplate.remove(new Query(new Criteria("token").is(token)), DaoHangUser.class);
	}

	public DaoHangUser getUser(String deviceId, String token) {
		Query query = new Query();
		Criteria criteria = Criteria.where("deviceId").is(deviceId).and("token").is("token");
		query.addCriteria(criteria);
		return mongoTemplate.findOne(query, DaoHangUser.class);
	}

	public DaoHangUser getUserByDevice(String deviceId) {
		Query query = new Query();
		Criteria criteria = Criteria.where("deviceId").is(deviceId);
		query.addCriteria(criteria);
		return mongoTemplate.findOne(query, DaoHangUser.class);
	}

	public DaoHangUser getUserByToken(String token) {
		Query query = new Query();
		Criteria criteria = Criteria.where("token").is(token);
		query.addCriteria(criteria);
		return mongoTemplate.findOne(query, DaoHangUser.class);
	}

	 



}
