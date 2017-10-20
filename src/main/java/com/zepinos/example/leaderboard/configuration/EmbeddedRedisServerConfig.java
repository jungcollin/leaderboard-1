package com.zepinos.example.leaderboard.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Profile(value = "embeddedRedisServer")
@Component
public class EmbeddedRedisServerConfig {

	@Value("${spring.redis.port}")
	private int redisPort;

	private RedisServer redisServer;

	@PostConstruct
	public void startRedis() throws IOException {

		redisServer = new RedisServer(redisPort);
		redisServer.start();

	}

	@PreDestroy
	public void stopRedis() {
		redisServer.stop();
	}

}
