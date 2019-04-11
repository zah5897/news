package com.zhan.app.news.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zhan.app.common.News;
import com.zhan.app.common.NewsDetial;
import com.zhan.app.news.dao.NewsDao;

@Service
public class NewsService {
	@Resource
	private NewsDao newsDao;

	 

	public List<?> listNews(String publish_time, int limit) {
		return newsDao.list(publish_time, limit);
	}

	public List<News> news_toutiao(int count) {
		List<News> news = (List<News>) newsDao.list_random_toutiao(null);

		if (news == null && news.size() == 0) {
			return new ArrayList<News>();
		}
		int size = news.size();

		if (size < count) {
			return news;
		}

		int i = 0;

		List<News> random = new ArrayList<News>();
		Random ran = new Random();
		List<Integer> hasAddIndex = new ArrayList<Integer>();

		while (i < count) {
			int index = ran.nextInt(size);
			if (hasAddIndex.contains(index)) {
				continue;
			}
			random.add(news.get(index));
			hasAddIndex.add(index);
			i++;
		}
		return random;
	}
	public List<News> browser_news_toutiao(String publishTime,int count) {
		return newsDao.list_toutiao(publishTime,count);
	}

	public List<News> news_baidu(int count) {
		List<News> news = (List<News>) newsDao.list_random_baidu(null);

		if (news == null && news.size() == 0) {
			return new ArrayList<News>();
		}
		int size = news.size();

		if (size < count) {
			return news;
		}

		int i = 0;

		List<News> random = new ArrayList<News>();
		Random ran = new Random();
		List<Integer> hasAddIndex = new ArrayList<Integer>();

		while (i < count) {
			int index = ran.nextInt(size);
			if (hasAddIndex.contains(index)) {
				continue;
			}
			random.add(news.get(index));
			hasAddIndex.add(index);
			i++;
		}
		return random;
	}

	public NewsDetial findNews(String id) {
		return newsDao.find(id);
	}

//	public List<Video> listVideosRandom(int count) {
//		List<Video> limit100 = newsDao.listVideos(100);
//		int video_size = limit100.size();
//		List<Video> limitCounts = new ArrayList<Video>();
//		Random ran = new Random();
//		List<Integer> existIndexs = new ArrayList<Integer>();
//
//		if (video_size < count) {
//			count = video_size;
//		}
//
//		int i = 0;
//		while (i < count) {
//			int index = ran.nextInt(video_size);
//			if (existIndexs.contains(index)) {
//				continue;
//			}
//			limitCounts.add(limit100.get(index));
//			existIndexs.add(index);
//			i++;
//		}
//		return limitCounts;
//	}

//	public Video findVideoById(String id) {
//		return newsDao.findVideo(id);
//	}

}
