package com.zhan.app.news.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhan.app.common.NewsDetial;
import com.zhan.app.news.service.NewsService;
import com.zhan.app.news.util.ResultUtil;

@RestController
@RequestMapping("/hot")
public class NewsController {
	private static Logger log = Logger.getLogger(NewsController.class);

	@Resource
	private NewsService newsService;

	@RequestMapping("list")
	public ModelMap list(HttpServletRequest request, String publish_time, Integer count) {
		int realCount = 0;

		if (count == null) {
			realCount = 10;
		} else if (count > 100) {
			realCount = 20;
		} else if (count <= 0) {
			return ResultUtil.getResultOKMap();
		} else {
			realCount = count;
		}
		List<?> news = newsService.listNews(publish_time, realCount);
		ModelMap result = ResultUtil.getResultOKMap();
		result.put("news", news);
		return result;
	}

	@RequestMapping("news")
	public ModelMap news(HttpServletRequest request, String publish_time, Integer count) {
		return list(request, publish_time, count);
	}

	@RequestMapping("news_detial")
	public ModelMap news_detial(HttpServletRequest request, String id) {
		return detial(request, id);
	}

	@RequestMapping("detial")
	public ModelMap detial(HttpServletRequest request, String id) {
		NewsDetial news = newsService.findNews(id);
		ModelMap result = ResultUtil.getResultOKMap();
		result.put("news", news);
		return result;
	}

}