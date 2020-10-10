package net.jqwik.testcontainers.inheritance;

import net.jqwik.testcontainers.*;

@Testcontainers
abstract class AbstractTestBase {

	@Container
	static RedisContainer redisPerClass = new RedisContainer();

	@Container
	RedisContainer redisPerTest = new RedisContainer();

}
