package net.jqwik.testcontainers;

import org.opentest4j.*;

import net.jqwik.api.*;

import static org.assertj.core.api.Assertions.*;

@Testcontainers
class TestLifecycleAwareExceptionCapturingTest {
	private static TestLifecycleAwareContainerMock startedTestContainer;
	@Container
	private final TestLifecycleAwareContainerMock testContainer = new TestLifecycleAwareContainerMock();

	@Example
	void one_example_failing_with_an_exception_or_checking_the_captured_exception() {
		if (startedTestContainer == null) {
			startedTestContainer = testContainer;
			Assume.that(false);
		}

		Throwable capturedThrowable = startedTestContainer.getCapturedThrowable();
		assertThat(capturedThrowable).isInstanceOf(AssertionFailedError.class);
	}

	@Example
	void another_example_failing_with_an_exception_or_checking_the_captured_exception() {
		if (startedTestContainer == null) {
			startedTestContainer = testContainer;
			Assume.that(false);
		}

		Throwable capturedThrowable = startedTestContainer.getCapturedThrowable();
		assertThat(capturedThrowable).isInstanceOf(TestAbortedException.class);
	}
}
