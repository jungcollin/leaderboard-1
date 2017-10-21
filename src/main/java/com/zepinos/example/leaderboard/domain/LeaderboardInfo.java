package com.zepinos.example.leaderboard.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class LeaderboardInfo implements Serializable {

	private String name;
	private Period period;
	private Sort sort;
	private Status status;

}
