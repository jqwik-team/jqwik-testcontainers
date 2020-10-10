package net.jqwik.testcontainers;

import net.jqwik.api.*;

@Disabled
@Testcontainers
class WrongAnnotationUsageTests {

	@Container
	private final String notStartable = "foobar";

	@Example
	void extension_throws_exception() {
		assert true;
	}

}
