package com.zhan.app.news.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhan.app.news.controller.NewsController;
import com.zhan.app.news.util.HttpUtil;
import com.zhan.app.news.util.IPUtil;
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
	@Resource
	protected RedisTemplate<String, String> redisTemplate;
	private static Logger log = Logger.getLogger(WeatherService.class);

	public JSONObject getWeather(String param) {
		String enCity = null;
		try {
			enCity = URLEncoder.encode(param, "utf-8");

			String paramStr = "key=" + SECRET + "&location=" + enCity + "&language=zh-Hans&unit=c";
			String result = HttpUtil.sendGet(WEATHER_URL, paramStr);
			if (!TextUtils.isEmpty(result)) {
				JSONObject resultObj = JSONObject.parseObject(result);
				if (resultObj != null) {
					JSONArray locationArray = resultObj.getJSONArray("results");

					if (locationArray != null && locationArray.size() > 0) {
						JSONObject weather = locationArray.getJSONObject(0);
						JSONObject now = weather.getJSONObject("now");
						now.put("weather_icon", "/weather_icon/" + now.getString("code") + ".png");
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

	public JSONObject getCacheWeatherByCityName(String cityName) {
		try {
			Object cacheWeather = redisTemplate.opsForHash().get(RedisKeys.KEY_WEATHER_DATA, cityName);
			// 当前系统时间
			long time = System.currentTimeMillis() / 1000;
			if (cacheWeather != null) {

				// 上次缓存时间
				Object timeObj = redisTemplate.opsForHash().get(RedisKeys.KEY_WEATHER_TIME, cityName);
				if (timeObj != null) {
					long cacheTime = Long.parseLong(timeObj.toString());
					// 在2小时内直接返回
					if (time - cacheTime < TIME) {
						
						JSONObject cache=JSONObject.parseObject(cacheWeather.toString());
						cache.put("c", "1");
						return cache;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			try{
			redisTemplate.opsForHash().delete(RedisKeys.KEY_WEATHER_DATA, cityName);
			}catch(Exception er){
				
			}
		}
		return null;
	}

	public void cacheWeather(String cityName, String weather) {
		try {
			redisTemplate.opsForHash().put(RedisKeys.KEY_WEATHER_DATA, cityName, weather);
			redisTemplate.opsForHash().put(RedisKeys.KEY_WEATHER_TIME, cityName,
					String.valueOf(System.currentTimeMillis() / 1000));
		} catch (Exception e) {
			log.error(e);
		}
	}

	public String getPm25(String city) {
		String url = "http://www.pm25.in/api/querys/pm2_5.json?city=" + city
				+ "&token=5j1znBVAsnSf5xQyNQyq&stations=no";
		String result = HttpUtil.sendGet(url, null);

		if (!TextUtils.isEmpty(result)) {

			try {
				JSONArray stations = JSONArray.parseArray(result);
				if (stations.size() > 0) {
					return stations.getJSONObject(0).getString("pm2_5");
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
		return "";
	}
}
