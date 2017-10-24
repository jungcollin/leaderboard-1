package com.zepinos.example.leaderboard.service;

import com.google.common.collect.Maps;
import com.zepinos.example.leaderboard.domain.LeaderboardInfo;
import com.zepinos.example.leaderboard.domain.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class LeaderboardService {

	@Resource(name = "redisTemplate")
	private ZSetOperations<String, String> zSetOperations;

	@Autowired
	private AdminService adminService;

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

		// leaderboardInfo 가져오기
		LeaderboardInfo leaderboardInfo = adminService.getLeaderboardInfo(position);
		// TODO 오류 처리를 위해 Custom Exception 을 만들어야 함.
		if (leaderboardInfo == null) {

			result.put("status", 1001);
			result.put("message", "리더보드 정보가 존재하지 않습니다.");

			return result;

		} else if (!leaderboardInfo.getStatus().equals(Status.USE)) {

			result.put("status", 1002);
			result.put("message", "사용 가능한 리더보드가 아닙니다다.");

		}

		// TODO 점수 등록

		// 결과 저장
		result.put("status", 0);

		return result;

	}

}
