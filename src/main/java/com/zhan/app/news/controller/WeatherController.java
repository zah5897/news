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
	public void now(HttpServletRequest request, HttpServletResponse response, String jsoncallback, String city_name) {
		String ip=IPUtil.getIpAddress(request);
		JSONObject weather = null;
		String cityName = city_name;
		if (TextUtils.isEmpty(cityName)) {
			try {
				cityName = IPUtil.getCityName(ip);
			} catch (Exception e) {
				log.error(e);
			}
		}
		String msg="";
		try {
			if (!TextUtils.isEmpty(cityName)) {
				msg="cityName="+cityName;
				weather = weatherService.getCacheWeatherByCityName(cityName);
				if (weather != null) {
					writeJsonP(response, jsoncallback, weather);
					return;
				} else {
					msg+=",获取缓存天气数据异常";
					weather = weatherService.getWeather(cityName);
					if (weather != null) {
						writeJsonP(response, jsoncallback, weather);
						weatherService.cacheWeather(cityName, weather.toJSONString());
						return;
					}else{
						msg+=",通过城市名字获取天气异常";
					}
				}
			} else {
				weather = weatherService.getWeather(ip);
				if (weather != null) {
					writeJsonP(response, jsoncallback, weather);
					return;
				}else{
					msg="ip="+ip+",通过ip获取天气异常";
				}
			}
		} catch (Exception e) {
			log.error(e);
		}
		if (weather == null) {
			weather = weatherService.getWeather(ip);
		}
		if (weather == null) {
			weather = new JSONObject();
			weather.put("code", -1);
			weather.put("msg", "系统异常");
			weather.put("msg_detail", msg);
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