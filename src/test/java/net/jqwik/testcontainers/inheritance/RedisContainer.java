package net.jqwik.testcontainers.inheritance;

import org.testcontainers.containers.*;
import redis.clients.jedis.*;

import static net.jqwik.testcontainers.JqwikTestImages.*;

public class RedisContainer extends GenericContainer<RedisContainer> {

	public RedisContainer() {
		super(REDIS_IMAGE);
		withExposedPorts(6379);
	}

	public Jedis getJedis() {
		return new Jedis(getHost(), getMappedPort(6379));
	}
}
