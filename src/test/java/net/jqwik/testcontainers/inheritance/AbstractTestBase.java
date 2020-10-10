package net.jqwik.testcontainers.inheritance;

import net.jqwik.testcontainers.Container;
import net.jqwik.testcontainers.Testcontainers;

@Testcontainers
abstract class AbstractTestBase {

    @Container
    static RedisContainer redisPerClass = new RedisContainer();

    @Container
    RedisContainer redisPerTest = new RedisContainer();

}
