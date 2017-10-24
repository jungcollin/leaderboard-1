package com.zepinos.example.leaderboard.service;

import com.google.common.collect.Maps;
import com.zepinos.example.leaderboard.domain.LeaderboardInfo;
import com.zepinos.example.leaderboard.domain.Period;
import com.zepinos.example.leaderboard.domain.Sort;
import com.zepinos.example.leaderboard.domain.Status;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

	@Resource(name = "redisTemplate")
	private ListOperations<String, LeaderboardInfo> listOperations;

	/**
	 * 리더보드 기본정보 생성
	 *
	 * @param name   리더보드명
	 * @param period 반복주기
	 * @param sort   정렬방식
	 * @return
	 */
	public Map<String, Object> createLeaderboard(String name,
	                                             Period period,
	                                             Sort sort) {

		Map<String, Object> result = Maps.newHashMap();

		// 기존 크기 확인
		long currentSize = listOperations.size("leaderboard_info");

		// 저장할 리더보드 기본 정보 생성
		LeaderboardInfo leaderboardInfo = new LeaderboardInfo();
		leaderboardInfo.setName(name);
		leaderboardInfo.setPeriod(period);
		leaderboardInfo.setSort(sort);
		leaderboardInfo.setStatus(Status.USE);

		// Redis 에 리더보드 기본 정보 생성
		listOperations.rightPush("leaderboard_info", leaderboardInfo);

		// 저장 후 리더보드 크기 확인
		long size = listOperations.size("leaderboard_info");

		// 저장 전과 후의 크기를 비교하여 1 이 늘어나지 않았을 경우 어떤 위치가 자신의 위치인지 확인
		if (size - currentSize > 1) {

			List<LeaderboardInfo> leaderboardInfoList = listOperations.range("leaderboard_info", currentSize, size);

			size = currentSize;
			for (LeaderboardInfo currentLeaderboardInfo : leaderboardInfoList) {

				// 동일한 개체를 찾았으면 해당 번호를 획득한다.
				if (currentLeaderboardInfo.getName().equals(name) &&
						currentLeaderboardInfo.getPeriod().equals(period) &&
						currentLeaderboardInfo.getSort().equals(sort) &&
						currentLeaderboardInfo.getStatus() == Status.USE)
					break;

				size++;

			}

		}

		// 결과 저장
		result.put("status", 0);
		result.put("position", size - 1);
		result.put("leaderboardInfo", leaderboardInfo);

		return result;

	}

	public Map<String, Object> contentLeaderboard(long position) {

		Map<String, Object> result = Maps.newHashMap();

		// 결과 저장
		result.put("status", 0);
		result.put("leaderboardInfo", getLeaderboardInfo(position));

		return result;

	}

	/**
	 * 리더보드 정보 가져오기
	 *
	 * @param position 리더보드 정보 위치
	 * @return
	 */
	public LeaderboardInfo getLeaderboardInfo(long position) {
		return listOperations.index("leaderboard_info", position);
	}

	public Map<String, Object> listLeaderboard(long startPosition, long endPosition) {

		Map<String, Object> result = Maps.newHashMap();

		// 결과 저장
		result.put("status", 0);
		result.put("leaderboardInfoList", listOperations.range("leaderboard_info", startPosition, endPosition));

		return result;

	}

}
