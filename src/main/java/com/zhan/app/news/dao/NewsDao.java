package com.zhan.app.news.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.zhan.app.common.News;
import com.zhan.app.common.NewsDetial;
import com.zhan.app.news.util.TextUtils;

@Repository("newsDao")
public class NewsDao extends BaseDao {
	@Resource
	protected MongoTemplate mongoTemplate;

	public String save(News news) {
		mongoTemplate.save(news);
		return news.id;
	}

	public void save(NewsDetial news) {
		mongoTemplate.save(news);
	}

	public long getCount(News news) {
		Query query = new Query();
		Criteria criteria = Criteria.where("url").is(news.url);
		query.addCriteria(criteria);
		return mongoTemplate.count(query, News.class);
	}

	public List<?> list(String publish_time, int limit) {
		Query query = new Query();
		query.with(new Sort(new Order(Direction.DESC, "publish_time")));
		query.limit(limit);
		if (!TextUtils.isEmpty(publish_time)) {
			Criteria criteria = Criteria.where("publish_time").lt(publish_time);
			query.addCriteria(criteria);
		}

		return mongoTemplate.find(query, News.class);
	}

	public NewsDetial find(String id) {
		Query query = new Query();
		Criteria criteria = Criteria.where("_id").is(id);
		query.addCriteria(criteria);
		return mongoTemplate.findOne(query, NewsDetial.class);
	}

}
