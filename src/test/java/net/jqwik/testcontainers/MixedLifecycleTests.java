package net.jqwik.testcontainers;

import org.testcontainers.containers.*;

import net.jqwik.api.*;

import static org.assertj.core.api.Assertions.*;

@Testcontainers
class MixedLifecycleTests {

	// will be shared between properties
	@Container
	private static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>(JqwikTestImages.MYSQL_IMAGE);

	// will be started before and stopped after each property
	@Container
	private final PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>(JqwikTestImages.POSTGRES_IMAGE)
			.withDatabaseName("foo")
			.withUsername("foo")
			.withPassword("secret");

	@Property
	void test() {
		assertThat(MY_SQL_CONTAINER.isRunning()).isTrue();
		assertThat(postgresqlContainer.isRunning()).isTrue();
	}
}
