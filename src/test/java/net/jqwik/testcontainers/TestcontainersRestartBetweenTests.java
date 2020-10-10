package net.jqwik.testcontainers;

import org.testcontainers.containers.*;

import net.jqwik.api.*;
import net.jqwik.api.lifecycle.*;

import static org.assertj.core.api.Assertions.*;

import static net.jqwik.testcontainers.JqwikTestImages.*;

@Testcontainers
class TestcontainersRestartBetweenTests {

	private static String lastContainerId;
	private static String currentProperty;
	@Container
	private final GenericContainer<?> genericContainer = new GenericContainer<>(HTTPD_IMAGE.toString())
			.withExposedPorts(80);

	@BeforeProperty
	void container_is_running() {
		assertThat(genericContainer.isRunning()).isTrue();
	}

	@AfterProperty
	void container_is_stopped() {
		assertThat(genericContainer.isRunning()).isTrue();
	}

	@Property
	void first_test() {
		if (lastContainerId == null) {
			currentProperty = "first";
			lastContainerId = genericContainer.getContainerId();
		} else if (!currentProperty.equals("first")) {
			assertThat(lastContainerId).isNotEqualTo(genericContainer.getContainerId());
		}
	}

	@Property
	void second_test() {
		if (lastContainerId == null) {
			lastContainerId = genericContainer.getContainerId();
			currentProperty = "second";
		} else if (!currentProperty.equals("second")) {
			assertThat(lastContainerId).isNotEqualTo(genericContainer.getContainerId());
		}
	}

}
