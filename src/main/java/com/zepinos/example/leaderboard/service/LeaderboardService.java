package com.zepinos.example.leaderboard.service;

import com.google.common.collect.Maps;
import com.zepinos.example.leaderboard.domain.LeaderboardInfo;
import com.zepinos.example.leaderboard.domain.Period;
import com.zepinos.example.leaderboard.domain.Sort;
import com.zepinos.example.leaderboard.domain.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
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
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, Long> epochHashOperations;

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
		// zset : 리더보드, epoch : 아이디의 점수를 마지막으로 저장한 timestamp
		String currentKey = getKey(position, period, localDateTime);
		String currentZSetKey = currentKey + ":zset";
		String currentEpochKey = currentKey + ":epoch";

		if (epochHashOperations.hasKey(currentEpochKey, userId)) {

			// TODO 마지막으로 점수를 저장한 timestamp 을 가져와 ZSet 에서 사용자의 등록된 점수 조회
			Long currentEpoch = epochHashOperations.get(currentEpochKey, userId);

			Double currentScore = zSetOperations.score(currentKey, currentEpoch + ":" + userId);

			if ((sort == Sort.ASC && currentScore.doubleValue() < score) ||
					(sort == Sort.DESC && currentScore > score)) {

				// TODO 현재 timestamp 와 점수를 저장

			}

		} else {

			// TODO 등록된 정보가 없다면 최초 등록이기 때문에 무조건 등록

		}


		// 결과 저장
		result.put("status", 0);

		return result;

	}

	/**
	 * ZSet 에서 사용할 키 생성<br>
	 * 생성방식은 leaderboard:날짜:position:날짜형식
	 *
	 * @param position
	 * @param period
	 * @param localDateTime
	 * @return
	 */
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
