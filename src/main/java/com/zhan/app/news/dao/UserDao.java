package com.zhan.app.news.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.zhan.app.common.User;

@Repository("userDao")
public class UserDao extends BaseDao<Object> {
	public long save(User user) {
		insertObj(user);
		return user.getId();
	}

 

	public String saveDaoHangUser(User user) {
		insertObj(user, "push_daohang_user");
		return String.valueOf(user.getId());
	}

	public int countByToken(String aid,String token) {
		return jdbcTemplate.queryForObject("select count(*) from push_news_user where aid=? and  token=?", new Object[] { aid,token },
				Integer.class);
	}

	  
	public void deleteByToken(String aid ,String token) {
		jdbcTemplate.update("delete from push_news_user where aid=? and  token=?", new Object[] {aid, token});
	}

	 

	public User getUser(String aid,String deviceId, String token) {
		return jdbcTemplate.query("select *from push_news_user where aid=? and  deviceId=? and token=?",
				new Object[] {aid, deviceId, token }, new BeanPropertyRowMapper<User>(User.class)).get(0);

	}

	public User getUserByDevice(String aid,String deviceId) {
		return jdbcTemplate.query("select *from push_news_user where aid=? and  deviceId=?",
				new Object[] {aid, deviceId }, new BeanPropertyRowMapper<User>(User.class)).get(0);
	}

	public User getUserByToken(String aid,String token) {
		return jdbcTemplate.query("select *from push_news_user where  aid=? and token=?",
				new Object[] { aid,token }, new BeanPropertyRowMapper<User>(User.class)).get(0);
	}

}
