package net.jqwik.testcontainers.inheritance;

import net.jqwik.api.*;
import net.jqwik.testcontainers.*;

import static org.assertj.core.api.Assertions.*;

class InheritedTests extends AbstractTestBase {

	@Container
	private final RedisContainer myRedis = new RedisContainer();

	@Example
	void step1() {
		assertThat(redisPerClass.getJedis().incr("key").longValue()).isEqualTo(1);
		assertThat(redisPerTest.getJedis().incr("key").longValue()).isEqualTo(1);
		assertThat(myRedis.getJedis().incr("key").longValue()).isEqualTo(1);
	}

	@Example
	void step2() {
		assertThat(redisPerClass.getJedis().incr("key").longValue()).isEqualTo(2);
		assertThat(redisPerTest.getJedis().incr("key").longValue()).isEqualTo(1);
		assertThat(myRedis.getJedis().incr("key").longValue()).isEqualTo(1);
	}
}
