package net.jqwik.testcontainers;

import org.testcontainers.containers.*;

import net.jqwik.api.*;
import net.jqwik.api.lifecycle.*;

import static org.assertj.core.api.Assertions.*;

import static net.jqwik.testcontainers.JqwikTestImages.*;

@Testcontainers
public class TestcontainersContainersRestartedWithinAGroupTest {

	private static GenericContainer<?> containerOfGroup1;
	private static GenericContainer<?> containerOfGroup2;
	@Container
	private final GenericContainer<?> topLevelContainer = new GenericContainer<>(HTTPD_IMAGE.toString())
			.withExposedPorts(80);

	@AfterContainer
	static void grouped_containers_should_not_be_running() {
		assertThat(containerOfGroup1.isRunning()).isFalse();
		assertThat(containerOfGroup2.isRunning()).isFalse();
	}

	@Example
	public void top_level_example() {
		assertThat(topLevelContainer.isRunning()).isTrue();
	}

	@Group
	public class Group1 {

		@Container
		private final GenericContainer<?> groupedContainer = new GenericContainer<>(HTTPD_IMAGE.toString())
				.withExposedPorts(80);

		private String lastGroupedContainerId;

		@Example
		public void example_with_grouped_container() {
			assertThat(groupedContainer.isRunning()).isTrue();
			containerOfGroup1 = groupedContainer;
			assertThatContainerOfGroup2IsNotRunning();
			if (lastGroupedContainerId == null) {
				lastGroupedContainerId = groupedContainer.getContainerId();
			} else {
				assertThat(lastGroupedContainerId).isNotEqualTo(groupedContainer.getContainerId());
			}
		}

		@Example
		public void other_example_with_grouped_container() {
			assertThat(groupedContainer.isRunning()).isTrue();
			containerOfGroup1 = groupedContainer;
			assertThatContainerOfGroup2IsNotRunning();
			if (lastGroupedContainerId == null) {
				lastGroupedContainerId = groupedContainer.getContainerId();
			} else {
				assertThat(lastGroupedContainerId).isNotEqualTo(groupedContainer.getContainerId());
			}
		}

		private void assertThatContainerOfGroup2IsNotRunning() {
			if (containerOfGroup2 != null) {
				assertThat(containerOfGroup2.isRunning()).isFalse();
			}
		}
	}

	@Group
	public class Group2 {

		@Container
		private final GenericContainer<?> groupedContainer = new GenericContainer<>(HTTPD_IMAGE.toString())
				.withExposedPorts(80);

		private String lastGroupedContainerId;

		@Example
		public void example_with_grouped_container() {
			assertThat(groupedContainer.isRunning()).isTrue();
			containerOfGroup2 = groupedContainer;
			assertThatContainerOfGroup1IsNotRunning();
			if (lastGroupedContainerId == null) {
				lastGroupedContainerId = groupedContainer.getContainerId();
			} else {
				assertThat(lastGroupedContainerId).isNotEqualTo(groupedContainer.getContainerId());
			}
		}

		@Example
		public void other_example_with_grouped_container() {
			assertThat(groupedContainer.isRunning()).isTrue();
			containerOfGroup2 = groupedContainer;
			assertThatContainerOfGroup1IsNotRunning();
			if (lastGroupedContainerId == null) {
				lastGroupedContainerId = groupedContainer.getContainerId();
			} else {
				assertThat(lastGroupedContainerId).isNotEqualTo(groupedContainer.getContainerId());
			}
		}

		private void assertThatContainerOfGroup1IsNotRunning() {
			if (containerOfGroup1 != null) {
				assertThat(containerOfGroup1.isRunning()).isFalse();
			}
		}
	}
}
