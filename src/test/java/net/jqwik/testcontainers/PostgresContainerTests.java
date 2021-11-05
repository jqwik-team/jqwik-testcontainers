package net.jqwik.testcontainers;

import java.sql.*;

import com.zaxxer.hikari.*;
import org.testcontainers.containers.*;

import net.jqwik.api.*;

import static org.assertj.core.api.Assertions.*;

import static net.jqwik.testcontainers.JqwikTestImages.*;

@Testcontainers
class PostgresContainerTests {

	@Container
	private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>(POSTGRES_IMAGE)
			.withDatabaseName("foo")
			.withUsername("foo")
			.withPassword("secret");

	@Property(tries = 10)
	void waits_until_postgres_accepts_jdbc_connections() throws Exception {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setJdbcUrl(POSTGRE_SQL_CONTAINER.getJdbcUrl());
		hikariConfig.setUsername("foo");
		hikariConfig.setPassword("secret");

		try (HikariDataSource ds = new HikariDataSource(hikariConfig)) {
			Statement statement = ds.getConnection().createStatement();
			statement.execute("SELECT 1");
			ResultSet resultSet = statement.getResultSet();
			resultSet.next();

			int resultSetInt = resultSet.getInt(1);
			assertThat(resultSetInt).isEqualTo(1);
		}
	}

}
