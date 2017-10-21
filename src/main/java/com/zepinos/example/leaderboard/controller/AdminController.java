package com.zepinos.example.leaderboard.controller;

import com.zepinos.example.leaderboard.domain.Period;
import com.zepinos.example.leaderboard.domain.Sort;
import com.zepinos.example.leaderboard.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping(path = "/admin")
@RestController
public class AdminController {

	@Autowired
	private AdminService adminService;

	@PostMapping(path = "createLeaderboardInfo")
	public Map<String, Object> createLeaderboardInfo(@RequestParam String name,
	                                                 @RequestParam Period period,
	                                                 @RequestParam Sort sort) {

		Map<String, Object> result = adminService.createLeaderboard(name,
				period,
				sort);

		return result;

	}

}
