package net.jqwik.testcontainers;

import org.testcontainers.containers.*;

import net.jqwik.api.*;
import net.jqwik.api.lifecycle.*;

import static org.assertj.core.api.Assertions.*;

import static net.jqwik.testcontainers.JqwikTestImages.*;

@Testcontainers
class TestcontainersSharedContainerTests {

	@Container
	private static final GenericContainer<?> GENERIC_CONTAINER = new GenericContainer<>(HTTPD_IMAGE.toString())
			.withExposedPorts(80);

	private static String lastContainerId;

	@BeforeContainer
	static void doSomethingWithAContainer() {
		assertThat(GENERIC_CONTAINER.isRunning()).isTrue();
	}

	@Property
	void first_test() {
		if (lastContainerId == null) {
			lastContainerId = GENERIC_CONTAINER.getContainerId();
		} else {
			assertThat(lastContainerId).isEqualTo(GENERIC_CONTAINER.getContainerId());
		}
	}

	@Property
	void second_test() {
		if (lastContainerId == null) {
			lastContainerId = GENERIC_CONTAINER.getContainerId();
		} else {
			assertThat(lastContainerId).isEqualTo(GENERIC_CONTAINER.getContainerId());
		}
	}

}
