package com.zhan.app.news.controller;

import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.zhan.app.news.service.WeatherService;
import com.zhan.app.news.util.IPUtil;
import com.zhan.app.news.util.TextUtils;

@Controller
@RequestMapping("/weather")
public class WeatherController {
	private static Logger log = Logger.getLogger(WeatherController.class);

	@Resource
	private WeatherService weatherService;

	@RequestMapping(value = "now", produces = "text/html;charset=UTF-8")
	public void now(HttpServletRequest request, HttpServletResponse response, String jsoncallback, String lat,
			String lng, String city_name) {
		JSONObject weather = null;
		String cityName = city_name;

		if (TextUtils.isEmpty(cityName)) {
			if (!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lng)) {
				cityName = IPUtil.getCityNameByLatLng(lat, lng);
			}
		}
		if (TextUtils.isEmpty(cityName)) {
			cityName = IPUtil.getCityName(IPUtil.getIpAddress(request));
		}

		if (!TextUtils.isEmpty(cityName)) {
			weather = weatherService.getCacheWeatherByCityName(cityName);
			if (weather != null) {
				writeJsonP(response, jsoncallback, weather);
				return;
			} else {
				weather = weatherService.getWeather(cityName);
				if (weather != null) {
					writeJsonP(response, jsoncallback, weather);
					weatherService.cacheWeather(cityName, weather.toJSONString());
					return;
				}
			}
		} else {
			weather = weatherService.getWeather("ip");
			if (weather != null) {
				writeJsonP(response, jsoncallback, weather);
				return;
			}
		}

		if (weather == null) {
			weather = new JSONObject();
			weather.put("code", -1);
			weather.put("msg", "系统异常");
		}
		writeJsonP(response, jsoncallback, weather);
	}

	private void writeJsonP(HttpServletResponse response, String jsoncallback, JSONObject result) {
		String resultString;
		if (!TextUtils.isEmpty(jsoncallback)) {
			resultString = jsoncallback + "(" + result.toJSONString() + ")";
		} else {
			resultString = result.toJSONString();
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