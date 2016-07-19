package com.zhan.app.news.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
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
	private static Logger log = Logger.getLogger(UserController.class);

	@Resource
	private UserService userService;

	@RequestMapping("add_token")
	public ModelMap add_token(HttpServletRequest request, User user) {
		if (TextUtils.isEmpty(user.getDeviceId()) || TextUtils.isEmpty(user.getToken())) {
			return ResultUtil.getResultMap(ERROR.ERR_PARAM);
		}

		long count = userService.countDevice(user.getDeviceId());
		if (count > 0) {
			userService.deleteByDetive(user.getDeviceId());
		}
		count = userService.countToken(user.getToken());
		if (count > 0) {
			userService.deleteByToken(user.getToken());
		}

		String zh_cn = request.getParameter("zh-cn");
		user.setZh_cn(zh_cn);
		String id = userService.insert(user);
		ModelMap result = ResultUtil.getResultOKMap();
		result.put("user_id", id);
		return result;
	}

	@RequestMapping("info")
	public ModelMap info(HttpServletRequest request, String device_id, String token) {
		ModelMap result = ResultUtil.getResultOKMap();
		User user = null;
		if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(device_id)) {
			user = userService.getUser(device_id, token);
		}else if(!TextUtils.isEmpty(token)){
			user = userService.getUserByToken(token);
		}else if(!TextUtils.isEmpty(device_id)){
			user=userService.getUserByDevice(device_id);
		}
		if (user != null) {
			result.put("user", user);
		}
		return result;

	}

}