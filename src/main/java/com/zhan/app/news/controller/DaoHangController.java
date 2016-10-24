package com.zhan.app.news.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhan.app.common.News;
import com.zhan.app.common.NewsDetial;
import com.zhan.app.common.User;
import com.zhan.app.common.Video;
import com.zhan.app.news.exception.ERROR;
import com.zhan.app.news.service.DaoHangUserService;
import com.zhan.app.news.service.NewsService;
import com.zhan.app.news.service.WeatherService;
import com.zhan.app.news.util.ResultUtil;
import com.zhan.app.news.util.TextUtils;

@Controller
@RequestMapping("/dao_hang")
public class DaoHangController {

	@Resource
	private WeatherService weatherService;
	@Resource
	private NewsService newsService;

	@Resource
	private DaoHangUserService daoHanguserService;

	int cacheCount = 0;

	@RequestMapping("add_token")
	@ResponseBody
	public ModelMap add_token(HttpServletRequest request, User user) {
		if (TextUtils.isEmpty(user.getDeviceId()) || TextUtils.isEmpty(user.getToken())) {
			return ResultUtil.getResultMap(ERROR.ERR_PARAM);
		}

		long count = daoHanguserService.countDevice(user.getDeviceId());
		if (count > 0) {
			daoHanguserService.deleteByDetive(user.getDeviceId());
		}
		count = daoHanguserService.countToken(user.getToken());
		if (count > 0) {
			daoHanguserService.deleteByToken(user.getToken());
		}
		
		user.setCreate_time(System.currentTimeMillis()/1000);
		String id = daoHanguserService.insert(user);
		ModelMap result = ResultUtil.getResultOKMap();
		result.put("user_id", id);
		return result;
	}

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

	
	@RequestMapping(value = "news_baidu", produces = "text/html;charset=UTF-8")
	public void news_baidu(HttpServletRequest request, HttpServletResponse response, String jsoncallback,
			String publish_time, Integer count) {

		if (count == null || count <= 0) {
			count = 10;
		} else if (count > 50) {
			count = 20;
		}

		List<News> toutiao = newsService.news_baidu(count);

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
		long cache_count = daoHanguserService.getBrowseCount();
		long realCount = cache_count + cacheCount;
		if (cacheCount > 50) {
			daoHanguserService.setBrowseCount(realCount);
			cacheCount = 0;
		}

		List<Video> videos = newsService.listVideosRandom(count);
		JSONObject result = new JSONObject();

		if (videos != null) {
			result.put("videos", videos);
		}
		result.put("browse_count", realCount);
		cacheCount++;
		writeJsonP(response, jsoncallback, result.toJSONString());
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