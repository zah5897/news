package com.zhan.app.news.dao;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zhan.app.common.News;
import com.zhan.app.common.NewsDetial;
import com.zhan.app.common.node.Node;
import com.zhan.app.news.util.JSONUtil;
import com.zhan.app.news.util.TextUtils;

@Repository("newsDao")
public class NewsDao extends BaseDao<Object> {

	public List<?> list(String publish_time, int limit) {
		if (TextUtils.isEmpty(publish_time)) {
			return jdbcTemplate.query("select *from t_news_simple  order by publish_time desc limit " + limit,
					new BeanPropertyRowMapper<News>(News.class));
		} else {
			return jdbcTemplate.query(
					"select *from t_news_simple  where publish_time<? order by publish_time desc limit ?",
					new Object[] { publish_time, limit }, new BeanPropertyRowMapper<News>(News.class));
		}
	}

	public List<?> list_random_toutiao(String publish_time) {
		return jdbcTemplate.query("select *from t_news_simple where type=0  order by publish_time desc limit 30",
				new BeanPropertyRowMapper<News>(News.class));
	}

	public List<News> list_toutiao(String publish_time, int count) {

		return jdbcTemplate.query(
				"select *from t_news_simple where type=0 and  publish_time<? order by publish_time desc limit 30",
				new Object[] { publish_time }, new BeanPropertyRowMapper<News>(News.class));
	}

	public List<?> list_random_baidu(String publish_time) {
		return jdbcTemplate.query("select *from t_news_simple where type=1  order by publish_time desc limit 30",
				new BeanPropertyRowMapper<News>(News.class));
	}

	public NewsDetial find(String id) {
		return jdbcTemplate.query("select *from t_news_detail where id=?", new Object[] { id },new RowMapper<NewsDetial>() {

			@Override
			public NewsDetial mapRow(ResultSet rs, int rowNum) throws SQLException {
				NewsDetial d=new NewsDetial();
				
				d.setId(rs.getString("id"));
				d.setTitle(rs.getString("title"));
				d.setFrom(rs.getString("_from"));
				d.setPublish_time(rs.getString("publish_time"));
				d.setDetial_url(rs.getString("detial_url"));
				String nodes=rs.getString("nodes");
				d.setNodes(JSONUtil.jsonToList(nodes, new TypeReference<List<Node>>() {
				}));
				return d;
			}
		}).get(0);
	}

//	public List<Video> listVideos(int count) {
//		Query query = new Query();
//		query.with(new Sort(new Order(Direction.DESC, "_id")));
//		query.limit(count);
//		return mongoTemplate.find(query, Video.class);
//	}
//
//	public Video findVideo(String id) {
//		Query query = new Query();
//		Criteria criteria = Criteria.where("_id").is(id);
//		query.addCriteria(criteria);
//		return mongoTemplate.findOne(query, Video.class);
//	}
}
