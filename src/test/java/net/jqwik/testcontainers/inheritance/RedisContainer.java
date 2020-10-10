package net.jqwik.testcontainers.inheritance;

import org.testcontainers.containers.*;
import org.testcontainers.utility.*;
import redis.clients.jedis.*;

public class RedisContainer extends GenericContainer<RedisContainer> {

	public RedisContainer() {
		super(new DockerImageName("redis:3.2.11").toString());
		withExposedPorts(6379);
	}

	public Jedis getJedis() {
		return new Jedis(getHost(), getMappedPort(6379));
	}
}
