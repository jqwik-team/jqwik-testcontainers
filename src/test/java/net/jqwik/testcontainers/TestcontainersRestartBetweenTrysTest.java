package net.jqwik.testcontainers;

import org.testcontainers.containers.*;

import net.jqwik.api.*;
import net.jqwik.api.lifecycle.*;

import static org.assertj.core.api.Assertions.*;

import static net.jqwik.testcontainers.JqwikTestImages.*;

@Testcontainers
public class TestcontainersRestartBetweenTrysTest {

	private static String restartedBetweenTries = "1x0";
	private static String beforeTryContainerId = "2x0";
	@Container(restartPerTry = true)
	private final GenericContainer<?> restartBetweenTries = new GenericContainer<>(HTTPD_IMAGE.toString())
			.withExposedPorts(80);
	@Container(restartPerTry = true)
	private final TestLifecycleAwareContainerMock containerMock = new TestLifecycleAwareContainerMock();

	@Property(tries = 2)
	public void container_id_should_always_be_different_between_tries() {
		assertThat(restartBetweenTries.isRunning()).isTrue();
		assertThat(restartedBetweenTries).isNotEqualTo(restartBetweenTries.getContainerId());
		restartedBetweenTries = restartBetweenTries.getContainerId();
	}

	@BeforeTry
	public void container_should_be_running_before_try() {
		assertThat(restartBetweenTries.isRunning()).isTrue();
		beforeTryContainerId = restartBetweenTries.getContainerId();
	}

	@AfterTry
	public void container_should_be_running_after_try() {
		assertThat(restartBetweenTries.isRunning()).isTrue();
		assertThat(restartBetweenTries.getContainerId()).isEqualTo(beforeTryContainerId);
	}

	@BeforeProperty
	@AfterProperty
	public void container_restarted_between_tries_should_not_be_running() {
		assertThat(restartBetweenTries.isRunning()).isFalse();
	}

	@AfterProperty
	public void call_lifecycle_methods_before_and_after_try() {
		assertThat(containerMock.getLifecycleMethodCalls()).containsExactly(
				TestLifecycleAwareContainerMock.BEFORE_TEST,
				TestLifecycleAwareContainerMock.AFTER_TEST,
				TestLifecycleAwareContainerMock.BEFORE_TEST,
				TestLifecycleAwareContainerMock.AFTER_TEST
		);
	}
}
