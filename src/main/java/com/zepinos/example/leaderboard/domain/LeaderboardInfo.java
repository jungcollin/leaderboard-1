package com.zepinos.example.leaderboard.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class LeaderboardInfo implements Serializable {

	@NotNull
	private String name;
	@NotNull
	private Period period;
	@NotNull
	private Sort sort;

}
