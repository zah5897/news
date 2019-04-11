package com.zhan.app.news.util;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.zhan.app.news.exception.AppException;
import com.zhan.app.news.exception.ERROR;


public class WriteJsonUtil {
	public static void write(HttpServletRequest request,HttpServletResponse response, Exception ex) {
		response.setContentType("text/json");
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter writer = response.getWriter();
			ERROR err;
			if (ex instanceof AppException) {
				err = ((AppException) ex).getError();
			} else {
				err = ERROR.ERR_SYS;
			}
			
			Map<String, Object> detail=new HashMap<String, Object>();
			detail.put("url", request.getRequestURI());
			detail.put("cause", ex.getMessage());
			
			Map<String, Object> json=new HashMap<String, Object>();
			json.put("code", err.getValue());
			json.put("msg", err.getErrorMsg());
			json.put("detail", detail);
			writer.write(JSONUtil.writeValueAsString(json));
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void write(HttpServletResponse response, ERROR error) {
		response.setContentType("text/json");
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter writer = response.getWriter();
			Map<String, Object> json=new HashMap<String, Object>();
			json.put("code", error.ordinal());
			json.put("msg", error.getErrorMsg());
			writer.write(JSONUtil.writeValueAsString(json));
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
