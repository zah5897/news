package com.zhan.app.news.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhan.app.common.User;
import com.zhan.app.news.exception.ERROR;
import com.zhan.app.news.service.UserService;
import com.zhan.app.news.util.ResultUtil;
import com.zhan.app.news.util.TextUtils;

@RestController
@RequestMapping("/user")
public class UserController {
	@Resource
	private UserService userService;

	@RequestMapping("add_token")
	public ModelMap add_token(HttpServletRequest request, User user) {
		if (TextUtils.isEmpty(user.getDeviceId()) || TextUtils.isEmpty(user.getToken())) {
			return ResultUtil.getResultMap(ERROR.ERR_PARAM);
		}

		String zh_cn = request.getParameter("zh-cn");
		if (!TextUtils.isEmpty(zh_cn)) {
			user.setZh_cn(zh_cn);
		}
		user.setCreate_time(System.currentTimeMillis() / 1000);
		long id = 0;
		String aid = user.getAid();
		int count = userService.countToken(aid, user.getToken());
		if (count > 0) {
			userService.deleteByToken(aid, user.getToken());
		}
		id = userService.insert(user);

		ModelMap result = ResultUtil.getResultOKMap();
		result.put("user_id", id);
		return result;
	}

	@RequestMapping("info")
	public ModelMap info(HttpServletRequest request, String aid, String device_id, String token) {
		ModelMap result = ResultUtil.getResultOKMap();
		User user = null;
		if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(device_id)) {
			user = userService.getUser(aid, device_id, token);
		} else if (!TextUtils.isEmpty(token)) {
			user = userService.getUserByToken(aid, token);
		} else if (!TextUtils.isEmpty(device_id)) {
			user = userService.getUserByDevice(aid, device_id);
		}
		if (user != null) {
			result.put("user", user);
		}
		return result;

	}

}