package com.zhan.app.news.service;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhan.app.news.util.EncodeUtil;
import com.zhan.app.news.util.HttpUtil;
import com.zhan.app.news.util.RedisKeys;
import com.zhan.app.news.util.TextUtils;

@Service
public class WeatherService {
	//api 提供方URL=http://www.thinkpage.cn/doc#sign
	// API密钥：yjs5s0nfb3fgiqxz
	// 我的用户ID：UC69529CCD

	public static final long TIME=2*60*60;
	
	public static final String SECRET = "yjs5s0nfb3fgiqxz";
	public static final String ID = "UC69529CCD";

	public static final String WEATHER_URL = "https://api.thinkpage.cn/v3/weather/now.json";
	@Resource
	protected RedisTemplate<String, String> redisTemplate;
	
	private JSONObject getWeather(String host,String param) {
		String paramStr = "key=" + SECRET + "&location=" + param + "&language=zh-Hans&unit=c";
		String result = HttpUtil.sendGet(WEATHER_URL, paramStr);
		System.out.println(EncodeUtil.getEncoding(result));
		if (!TextUtils.isEmpty(result)) {
			JSONObject resultObj = JSONObject.parseObject(result);
			if (resultObj != null) {
				JSONArray locationArray = resultObj.getJSONArray("results");

				if (locationArray != null && locationArray.size() > 0) {
					JSONObject weather = locationArray.getJSONObject(0);
					JSONObject now = weather.getJSONObject("now");
					now.put("weather_icon", "/weather_icon/"+now.getString("code")+".png");
//					now.put("weather_icon", "http://localhost:8899/news/weather_icon/0.png");
					weather.put("now", now);

					return weather;
				}

			}

		}
		return null;
	}

	public JSONObject getWeatherByCityName(String host,String cityName) {
		Object cacheWeather=redisTemplate.opsForHash().get(RedisKeys.KEY_WEATHER_DATA, cityName);
		//当前系统时间
		long time=System.currentTimeMillis()/1000;
		if(cacheWeather!=null){
			
			// 上次缓存时间
			Object timeObj=redisTemplate.opsForHash().get(RedisKeys.KEY_WEATHER_TIME, cityName);
			if(timeObj!=null){
				long cacheTime=Long.parseLong(timeObj.toString());
				//在2小时内直接返回
				if(time-cacheTime<TIME){
					return JSONObject.parseObject(cacheWeather.toString());
				}
			}
		}
		
		JSONObject obj=getWeather(host,cityName);
		if(obj!=null){
			redisTemplate.opsForHash().put(RedisKeys.KEY_WEATHER_DATA, cityName,obj.toJSONString());  	
			redisTemplate.opsForHash().put(RedisKeys.KEY_WEATHER_TIME, cityName,String.valueOf(time));
		}
		return obj;
	}

	public JSONObject getWeatherByLatLng(String host,String lat, String lng) {
		return getWeatherByCityName(host,lat + ":" + lng);
	}
	 
}
