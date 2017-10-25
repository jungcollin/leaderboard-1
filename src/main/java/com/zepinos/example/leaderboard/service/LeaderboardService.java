package com.zepinos.example.leaderboard.service;

import com.google.common.collect.Maps;
import com.zepinos.example.leaderboard.domain.LeaderboardInfo;
import com.zepinos.example.leaderboard.domain.Period;
import com.zepinos.example.leaderboard.domain.Sort;
import com.zepinos.example.leaderboard.domain.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

		if (leaderboardInfo == null) {

			// TODO 오류 처리를 위해 Custom Exception 을 만들어야 함.
			result.put("status", 1001);
			result.put("message", "리더보드 정보가 존재하지 않습니다.");

			return result;

		} else if (!leaderboardInfo.getStatus().equals(Status.USE)) {

			// TODO 오류 처리를 위해 Custom Exception 을 만들어야 함.
			result.put("status", 1002);
			result.put("message", "사용 가능한 리더보드가 아닙니다.");

		}

		// 기간과 정렬방식, 현재시각 가져오기
		Period period = leaderboardInfo.getPeriod();
		Sort sort = leaderboardInfo.getSort();
		LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

		// leaderboardInfo 에서 정렬 정보로 key 을 생성
		String key = getKey(position, period, localDateTime);

		// TODO 점수 등록

		// 결과 저장
		result.put("status", 0);

		return result;

	}

	private String getKey(long position,
	                      Period period,
	                      LocalDateTime localDateTime) {

		// 현재 시각 정보 조회
		String pattern;
		switch (period) {
			case DAY:
				pattern = "yyyy-MM-dd";
				break;
			case WEEK:
				pattern = "YYYY-ww";
				break;
			case MONTH:
				pattern = "yyyy-MM";
				break;
			case YEAR:
				pattern = "yyyy";
			default:
				// TODO 오류 처리를 위해 Custom Exception 을 만들어야 함.
				throw new IndexOutOfBoundsException();
		}

		String periodName = localDateTime.format(DateTimeFormatter.ofPattern(pattern));

		// 키 형태 : leaderboard:기간:위치:기간이름
		return String.format("leaderboard:%s:%d:%s", period.name(), position, periodName);
	}

}
