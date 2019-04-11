package com.zhan.app.news.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.zhan.app.news.controller.NewsController;
import com.zhan.app.news.util.HttpUtil;
import com.zhan.app.news.util.IPUtil;
import com.zhan.app.news.util.JSONUtil;
import com.zhan.app.news.util.RedisKeys;
import com.zhan.app.news.util.TextUtils;

@Service
public class WeatherService {
	// api 提供方URL=http://www.thinkpage.cn/doc#sign
	// API密钥：yjs5s0nfb3fgiqxz
	// 我的用户ID：UC69529CCD

	public static final long TIME = 2 * 60 * 60;

	public static final String SECRET = "yjs5s0nfb3fgiqxz";
	public static final String ID = "UC69529CCD";

	public static final String WEATHER_URL = "https://api.thinkpage.cn/v3/weather/now.json";
	private static Logger log = Logger.getLogger(WeatherService.class);

	public Map<String, Object> getWeather(String param) {
		String enCity = null;
		try {
			enCity = URLEncoder.encode(param, "utf-8");

			String paramStr = "key=" + SECRET + "&location=" + enCity + "&language=zh-Hans&unit=c";
			String result = HttpUtil.sendGet(WEATHER_URL, paramStr);
			if (!TextUtils.isEmpty(result)) {
				Map<String, Object> resultObj = JSONUtil.jsonToMap(result);
				if (resultObj != null) {
					List<Map<String, Object>> locationArray = (List<Map<String, Object>>) resultObj.get("results");

					if (locationArray != null && locationArray.size() > 0) {
						Map<String, Object> weather = locationArray.get(0);
						Map<String, Object> now = (Map<String, Object>) weather.get("now");
						now.put("weather_icon", "/weather_icon/" + now.get("code") + ".png");
						now.put("pm_25", getPm25(enCity));
						weather.put("now", now);
						return weather;
					}

				}

			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.error(e);
		}
		return null;
	}

	 
	public String getPm25(String city) {
		String url = "http://www.pm25.in/api/querys/pm2_5.json?city=" + city
				+ "&token=5j1znBVAsnSf5xQyNQyq&stations=no";
		String result = HttpUtil.sendGet(url, null);

		if (!TextUtils.isEmpty(result)) {

//			try {
//				JSONArray stations = JSONArray.parseArray(result);
//				if (stations.size() > 0) {
//					return stations.getJSONObject(0).getString("pm2_5");
//				}
//			} catch (Exception e) {
//				log.error(e);
//			}
		}
		return "";
	}
}
