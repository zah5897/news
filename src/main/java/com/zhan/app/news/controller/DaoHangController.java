package com.zhan.app.news.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.zhan.app.common.News;
import com.zhan.app.common.NewsDetial;
import com.zhan.app.news.service.NewsService;
import com.zhan.app.news.service.WeatherService;
import com.zhan.app.news.util.TextUtils;

@Controller
@RequestMapping("/dao_hang")
public class DaoHangController {

	@Resource
	private WeatherService weatherService;
	@Resource
	private NewsService newsService;

	@RequestMapping(value = "news_toutiao", produces = "text/html;charset=UTF-8")
	public void news_toutiao(HttpServletRequest request, HttpServletResponse response, String jsoncallback,
			String publish_time, Integer count) {

		if (count == null || count <= 0) {
			count = 10;
		} else if (count > 50) {
			count = 20;
		}

		List<News> toutiao = newsService.news_toutiao(count);

		String jsonText = "";
		jsonText = JSON.toJSONString(toutiao, true);
		writeJsonP(response, jsoncallback, jsonText);
	}

	@RequestMapping(value = "news_detial", produces = "text/html;charset=UTF-8")
	public void news_detial(HttpServletRequest request, HttpServletResponse response, String jsoncallback, String id) {
		NewsDetial news = newsService.findNews(id);
		String result = "";
		if (news != null) {
			result = JSON.toJSONString(news);
		}
		writeJsonP(response, jsoncallback, result);
	}

	@RequestMapping(value = "videos", produces = "text/html;charset=UTF-8")
	public void videos(HttpServletRequest request, HttpServletResponse response, String jsoncallback, Integer count) {
		if (count == null || count < 0) {
			count = 4;
		}
		String jsonText = "";
		List videos = newsService.listVideos(count);
		if (videos != null) {
			jsonText = JSON.toJSONString(videos, true);
		}
		writeJsonP(response, jsoncallback, jsonText);
	}

	private void writeJsonP(HttpServletResponse response, String jsoncallback, String result) {
		String resultString;
		if (!TextUtils.isEmpty(jsoncallback)) {
			resultString = jsoncallback + "(" + result + ")";
		} else {
			resultString = result;
		}
		response.setContentType("text/json");
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter writer = response.getWriter();
			writer.write(resultString);
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}