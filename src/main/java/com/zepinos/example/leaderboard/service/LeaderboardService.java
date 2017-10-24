package com.zepinos.example.leaderboard.service;

import com.google.common.collect.Maps;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class LeaderboardService {

	@Resource(name = "redisTemplate")
	private ZSetOperations<String, String> zSetOperations;

	/**
	 * 스코어 등록
	 *
	 * @param position leaderboardInfo 의 위치
	 * @param userId   사용자 아이디
	 * @param score    점수
	 * @return
	 */
	public Map<String, Object> appendScore(long position,
	                                       String userId,
	                                       double score) {

		Map<String, Object> result = Maps.newHashMap();

		// TODO leaderboardInfo 가져오기

		// TODO 점수 등록

		// 결과 저장
		result.put("status", 0);

		return result;

	}

}
