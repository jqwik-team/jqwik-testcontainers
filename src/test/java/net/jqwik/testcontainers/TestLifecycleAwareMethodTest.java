package net.jqwik.testcontainers;

import net.jqwik.api.*;
import net.jqwik.api.lifecycle.*;

import static org.assertj.core.api.Assertions.*;

import static net.jqwik.testcontainers.TestLifecycleAwareContainerMock.*;

@AddLifecycleHook(TestLifecycleAwareMethodTest.SharedContainerAfterAllTestExtension.class)
@Testcontainers
class TestLifecycleAwareMethodTest {
	@Container
	private static final TestLifecycleAwareContainerMock SHARED_CONTAINER = new TestLifecycleAwareContainerMock();
	private static TestLifecycleAwareContainerMock startedTestContainer;
	@Container
	private final TestLifecycleAwareContainerMock testContainer = new TestLifecycleAwareContainerMock();

	@BeforeContainer
	static void beforeAll() {
		assertThat(SHARED_CONTAINER.getLifecycleMethodCalls()).containsExactly(BEFORE_TEST);
	}

	@BeforeProperty
	void should_prepare_before_and_after_test() {
		// we can only test for a call to afterTest() after this test has been finished.
		if (startedTestContainer == null) {
			startedTestContainer = testContainer;
		}
	}

	@Example
	void should_call_beforeTest_first() {
		assertThat(startedTestContainer.getLifecycleMethodCalls()).contains(BEFORE_TEST);
	}

	@Example
	void should_have_a_filesystem_friendly_name_container_has_started() {
		assertThat(startedTestContainer.getLifecycleFilesystemFriendlyNames())
				.containsExactly(
						"should+have+a+filesystem+friendly+name+container+has+started"
				);
	}

	@Example
	void static_container_should_have_a_filesystem_friendly_name_after_container_has_started() {
		assertThat(SHARED_CONTAINER.getLifecycleFilesystemFriendlyNames())
				.containsExactly(
						"TestLifecycleAwareMethodTest"
				);
	}

	static class SharedContainerAfterAllTestExtension implements AfterContainerHook {

		@Override
		public void afterContainer(ContainerLifecycleContext context) {
			assertThat(SHARED_CONTAINER.getLifecycleMethodCalls())
					.containsExactly(BEFORE_TEST, AFTER_TEST);
		}

		@Override
		public int afterContainerProximity() {
			// Run after the TestcontainersExtensions
			return -12;
		}
	}
}
