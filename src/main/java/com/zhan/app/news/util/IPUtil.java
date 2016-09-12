package com.zhan.app.news.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;

public class IPUtil {
	public static String getIpAddress(HttpServletRequest request) {
		String ipAddress = null;
		// ipAddress = this.getRequest().getRemoteAddr();
		ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}

		}

		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
															// = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}

	public static String getCityName(String ip) {
		String city_name_url = "http://ip.taobao.com/service/getIpInfo.php?ip=" + ip;

		String result = HttpUtil.sendGet(city_name_url, null);
		String cityName = null;
		if (!TextUtils.isEmpty(result)) {
			JSONObject address = JSONObject.parseObject(result);
			if (address != null) {
				JSONObject cityObj = address.getJSONObject("data");
				if (cityObj != null) {
					cityName = cityObj.getString("city");
				}
			}
		}
		return cityName;
	}

	public static String getCityNameByLatLng(String lat, String lng) {

		String url = "http://api.map.baidu.com/geocoder/v2/?ak=o3Xbib1bOROyw4Uw35fdFGhu7ZE9uRnF&location=" + lat + ","
				+ lng + "&output=json";
		String result = HttpUtil.sendGet(url, null);

		String cityName = null;
		if (!TextUtils.isEmpty(result)) {
			JSONObject address = JSONObject.parseObject(result);
			if (address != null) {
				JSONObject resultObj = address.getJSONObject("result");
				if (resultObj != null) {
					JSONObject addressComponent = resultObj.getJSONObject("addressComponent");
					if (addressComponent != null) {
						cityName = addressComponent.getString("city");
					}
				}
			}
		}
		return cityName;
	}

}
