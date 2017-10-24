package com.zepinos.example.leaderboard.controller;

import com.zepinos.example.leaderboard.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping(path = "/leaderboard")
@RestController
public class LeaderboardController {

	@Autowired
	private LeaderboardService leaderboardService;

	@PostMapping(path = "appendScore")
	public Map<String, Object> appendScore(@RequestParam long position,
	                                       @RequestParam String userId,
	                                       @RequestParam double score) {

		Map<String, Object> result = leaderboardService.appendScore(position,
				userId,
				score);

		return result;

	}

}
