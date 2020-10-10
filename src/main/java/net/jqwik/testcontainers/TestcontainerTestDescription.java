package net.jqwik.testcontainers;

import java.util.*;

import org.testcontainers.lifecycle.*;

final class TestcontainersTestDescription implements TestDescription {
	private final String testId;
	private final String filesystemFriendlyName;

	TestcontainersTestDescription(String testId, String filesystemFriendlyName) {
		this.testId = testId;
		this.filesystemFriendlyName = filesystemFriendlyName;
	}

	@Override
	public String getTestId() {
		return testId;
	}

	@Override
	public String getFilesystemFriendlyName() {
		return filesystemFriendlyName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(testId, filesystemFriendlyName);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TestcontainersTestDescription that = (TestcontainersTestDescription) o;
		return Objects.equals(testId, that.testId) &&
				Objects.equals(filesystemFriendlyName, that.filesystemFriendlyName);
	}

	@Override
	public String toString() {
		return "TestcontainersTestDescription{" +
				"testId='" + testId + '\'' +
				", filesystemFriendlyName='" + filesystemFriendlyName + '\'' +
				'}';
	}
}
