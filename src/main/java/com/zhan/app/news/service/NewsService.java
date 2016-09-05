package com.zhan.app.news.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zhan.app.common.News;
import com.zhan.app.common.NewsDetial;
import com.zhan.app.common.Video;
import com.zhan.app.news.dao.NewsDao;

@Service
public class NewsService {
	@Resource
	private NewsDao newsDao;

	public String insert(News news) {
		return newsDao.save(news);
	}

	public long insert(NewsDetial news) {
		newsDao.save(news);
		return 0;
	}

	public boolean hasExistNews(News news) {
		long count = newsDao.getCount(news);
		return count > 0;
	}

	public List<?> listNews(String publish_time, int limit) {
		return newsDao.list(publish_time, limit);
	}

	public NewsDetial findNews(String id) {
		return newsDao.find(id);
	}

	public List<Video> listVideos(int count) {
		return newsDao.listVideos(count);
	}

	public Video findVideoById(String id) {
		return newsDao.findVideo(id);
	}

}
