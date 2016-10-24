package com.zhan.app.news.service;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.zhan.app.common.User;
import com.zhan.app.news.dao.DaoHangUserDao;

@Service
public class DaoHangUserService {

	private long browseCount = -1;
	@Resource
	protected RedisTemplate<String, String> redisTemplate;
	@Resource
	private DaoHangUserDao daoHanguserDao;

	public String insert(User user) {
		return daoHanguserDao.save(user);
	}

	public long countToken(String token) {
		return daoHanguserDao.countByToken(token);
	}

	public long countDevice(String deviceID) {
		return daoHanguserDao.countByDevice(deviceID);
	}

	public void deleteByDetive(String deviceID) {
		daoHanguserDao.deleteByDevice(deviceID);
	}

	public void deleteByToken(String token) {
		daoHanguserDao.deleteByToken(token);
	}

	public User getUser(String deviceId, String token) {
		return daoHanguserDao.getUser(deviceId, token);
	}

	public User getUserByDevice(String deviceId) {
		return daoHanguserDao.getUserByDevice(deviceId);
	}

	public User getUserByToken(String token) {
		return daoHanguserDao.getUserByToken(token);
	}

	public long getBrowseCount() {

		if (browseCount >= 0) {
			return browseCount;
		}
		try {
			Object value = redisTemplate.opsForValue().get("video_browse_count");
			if (value != null) {
				browseCount = Integer.parseInt(value.toString());
				return browseCount;
			}
		} catch (Exception e) {
			browseCount = -1;
		}
		return 0;
	}

	public int setBrowseCount(long count) {
		browseCount = count;
		try {

			Object value = redisTemplate.opsForValue().get("video_browse_count");
			long serverCacheCount;
			if (value != null) {
				serverCacheCount = Long.parseLong(value.toString());
				if (serverCacheCount < browseCount) {
					serverCacheCount = browseCount;
				}
			} else {
				serverCacheCount = browseCount;
			}
			redisTemplate.opsForValue().set("video_browse_count", String.valueOf(serverCacheCount));
		} catch (Exception e) {

		}
		return 0;
	}

}
