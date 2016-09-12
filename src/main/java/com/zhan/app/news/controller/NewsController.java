package com.zhan.app.news.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhan.app.common.NewsDetial;
import com.zhan.app.news.exception.ERROR;
import com.zhan.app.news.service.NewsService;
import com.zhan.app.news.util.ResultUtil;
import com.zhan.app.news.util.TextUtils;

@RestController
@RequestMapping("/hot")
public class NewsController {
	private static Logger log = Logger.getLogger(NewsController.class);

	@Resource
	private NewsService newsService;

	@RequestMapping("list")
	public ModelMap list(HttpServletRequest request, String publish_time, int count) {
		if (count > 100) {
			count = 20;
		}
		if (count <= 0) {
			return ResultUtil.getResultOKMap();
		}
		List news = newsService.listNews(publish_time, count);
		ModelMap result = ResultUtil.getResultOKMap();
		result.put("news", news);
		return result;
	}

	@RequestMapping("news")
	public ModelMap news(HttpServletRequest request, String publish_time, int count) {
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

	@RequestMapping("videos")
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

	@RequestMapping("get_video_by_id")
	public ModelMap videos(HttpServletRequest request, String id) {
		if (TextUtils.isEmpty(id)) {
			return ResultUtil.getResultMap(ERROR.ERR_FAILED.setNewText("视频id找不到"));
		}
		ModelMap result = ResultUtil.getResultOKMap();
		result.put("video", newsService.findVideoById(id));
		return result;
	}

	private void writeJsonP(HttpServletResponse response, String jsoncallback, String result) {
		String resultString;
		if(!TextUtils.isEmpty(jsoncallback)){
			resultString = jsoncallback + "(" + result + ")";
		}else{
			resultString=result;
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