package com.zhan.app.news.controller;

import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.zhan.app.news.exception.ERROR;
import com.zhan.app.news.service.WeatherService;
import com.zhan.app.news.util.ResultUtil;
import com.zhan.app.news.util.TextUtils;

@Controller
@RequestMapping("/weather")
public class WeatherController {
	private static Logger log = Logger.getLogger(WeatherController.class);

	@Resource
	private WeatherService weatherService;

	@RequestMapping(value="now",produces = "text/html;charset=UTF-8")
	public void now(HttpServletRequest request,HttpServletResponse response, String jsoncallback,String lat, String lng, String city_name) {
		String server_name=request.getServerName();
		ModelMap result;
		
		
		JSONObject weather = null;
		if (TextUtils.isEmpty(lat) || TextUtils.isEmpty(lng)) {
			if (TextUtils.isEmpty(city_name)) {
				result = ResultUtil.getResultMap(ERROR.ERR_PARAM.setNewText("请传入经纬度或者城市名称"));
			} else {
				 weather = weatherService.getWeatherByCityName(server_name,city_name);
				if (weather == null) {
					result = ResultUtil.getResultMap(ERROR.ERR_SYS.setNewText("获取天气失败"));
				} else {
					result = ResultUtil.getResultOKMap();
					result.put("weather", weather);
				}
			}
		} else {
			 weather = weatherService.getWeatherByLatLng(server_name,lat, lng);
			if (weather == null) {
				result = ResultUtil.getResultMap(ERROR.ERR_SYS.setNewText("获取天气失败"));
			} else {
				result = ResultUtil.getResultOKMap();
				result.put("weather", weather);
			}
		}
		
		
		
		if(weather==null){
			weather=new JSONObject();
			weather.put("code", -1);
			weather.put("msg", "系统异常");
		}
		String resultString=jsoncallback+"("+weather.toJSONString()+")";
		
		
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