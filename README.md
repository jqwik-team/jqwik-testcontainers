# jqwik Testcontainers Support

This project provides an extension to support [testcontainers](https://www.testcontainers.org/).

<!-- use `doctoc --maxlevel 3 README.md` to recreate the TOC -->
<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
### Table of Contents  

- [How to Install](#how-to-install)
  - [Gradle](#gradle)
  - [Maven](#maven)
  - [Supported Testcontainers Versions](#supported-testcontainers-versions)
  - [Supported JUnit Platform Versions](#supported-junit-platform-versions)
- [Standard Usage](#standard-usage)
  - [Groups](#groups)
  - [TestLifecycleAware containers](#testlifecycleaware-containers)
  - [Singleton containers](#singleton-containers)
- [Limitations](#limitations)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## How to Install

### Gradle

Follow the
[instructions here](https://jqwik.net/docs/current/user-guide.html#gradle)
and add the following dependency to your `build.gradle` file:

```
dependencies {
  testImplementation("org.testcontainers:testcontainers:1.14.3")

  // On a Mac:
  // testImplementation("org.testcontainers:testcontainers:1.15.0-rc2")

  testImplementation("net.jqwik:jqwik-testcontainers:0.5.0")
}
```

<!-- 
You can look at a
[sample project](https://github.com/jlink/jqwik-samples/tree/master/jqwik-testcontainers-gradle)
using jqwik, Testcontainers and Gradle.
-->

### Maven

Follow the
[instructions here](https://jqwik.net/docs/current/user-guide.html#maven)
and add the following dependency to your `pom.xml` file:

```
<dependency>
  <groupId>org.testcontainers</groupId>
  <artifactId>testcontainers</artifactId>
  <version>1.14.3</version>
  <scope>test</scope>
</dependency>
<dependency>
  <groupId>net.jqwik</groupId>
  <artifactId>jqwik-testcontainers</artifactId>
  <version>0.5.0</version>
  <scope>test</scope>
</dependency>
```

### Supported Testcontainers Versions

You have to provide your own version of testcontainers through Gradle or Maven. The *jqwik-testcontainers* library 
has been tested with version:
* 1.14.3 (not on Mac)
* 1.15.0-rc2 (on Mac)

Please report any compatibility issues you stumble upon.

### Supported JUnit Platform Versions

You need at least version `1.7.0` of the JUnit platform - otherwise
strange things _could_ happen.

## Standard Usage
The `@Testcontainers` annotation is the entry point of this extension. If the annotation is present on your class, jqwik 
will find all fields annotated with `@Container`. If any of these fields is not `Startable`, the tests won't be run 
resulting in a failure. Shared containers are static fields which are started once before all properties and examples 
and stopped after all properties and examples. Restarted containers are instance fields which are started and stopped 
for every property or example. Restarted try-containers are instance fields with a true `restartPerTry` annotation value. 
They are started and stopped for every property- or example-try. 

jqwik starts shared containers before calling `@BeforeContainer` annotated methods and stops them after calling 
`@AfterContainer` annotated methods. Similar, restarted containers are started before calling `@BeforeProperty`and 
stopped after calling `@AfterProperty`. Finally, restarted try-containers are started before calling `@BeforeTry` and 
stopped after calling `@AfterTry`.

```java
import net.jqwik.api.Example;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.lifecycle.BeforeProperty;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jqwik.Container;
import org.testcontainers.junit.jqwik.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class RedisBackedCacheIntTest {

    private RedisBackedCache underTest;

    @Container
    private static GenericContainer<?> sharedRedis = new GenericContainer(DockerImageName.parse("redis:5.0.3-alpine"))
        .withExposedPorts(6379);

    @Container
    private GenericContainer<?> redis = new GenericContainer(DockerImageName.parse("redis:5.0.3-alpine"))
                                            .withExposedPorts(6379);

    @BeforeProperty
    public void setUp() {
        String address = redis.getHost();
        Integer port = redis.getFirstMappedPort();

        // Now we have an address and port for Redis, no matter where it is running
        underTest = new RedisBackedCache(address, port);
    }

    @Example
    public void retrieve_from_redis() {
        underTest.put("test", "example");


        String retrieved = underTest.get("test");
        assertThat("example").isEqualTo(retrieved);
    }

    @Property
    public void what_has_been_put_in_redis_must_be_retrievable(@ForAll String key, @ForAll String value){
        underTest.put(key, value);
        String retrieved = underTest.get(key);
        assertThat(retrieved).isEqualTo(value);
    }
}
```

The test above uses `@Testcontainers` with two redis `@Container`s. `sharedContainer` will run for the whole test while 
`redis` will be restarted between the `@Example` and the `@Property`. The assumption about redis is, that whatever key
or value is used, the value should be able to be retrieved again by the key. jqwik generates keys and values and tries
to falsify this assumption. By default, the property will be tried 1000 times.

### Groups

A `@Group` is a means to improve the organization and maintainability of your tests. It may contain own restarted 
containers which will be restarted for properties and examples within a group but are not shared with subgroups.

```java
@Testcontainers
public class GroupedContainersTest {
    @Group
    public class GroupWithSubGroup {
        @Container
        private final GenericContainer<?> groupedContainer = new GenericContainer<>(HTTPD_IMAGE)
            .withExposedPorts(80);
    
        @Group
        public class Subgroup {
            @Example
            @Disabled("Container of group is not running in subgroup.")
            public void grouped_container_should_be_running() {
                assertThat(groupedContainer.isRunning()).isTrue();
            }
        }
    }
}
```

Example `grouped_container_should_be_running` would fail if it was not disabled. However, shared containers are running
for all properties and all examples of every group.

```java
@Testcontainers
public class GroupedContainersTest {
    @Container
    private static final GenericContainer<?> sharedContainer = new GenericContainer<>(HTTPD_IMAGE)
        .withExposedPorts(80);

    @Group
    public class GroupAccssingSharedContainer {

        @Group
        public class Subgroup {
            @Example
            public void shared_container_should_be_running() {
                assertThat(sharedContainer.isRunning()).isTrue();
            }
        }
    }
}
```

### TestLifecycleAware containers

Depending on the type of the `TestLifeCycleAware` container, callbacks `beforeTest` and `afterTest` will be called for 
every try, property/example and/or test run. Consider the following example:

```java
@Testcontainers
public class TestcontainersRestartBetweenTrysTest {
	
	@Container(restartPerTry = true)
	private final TestLifecycleAwareContainerMock containerMock = new TestLifecycleAwareContainerMock();

	@Property(tries = 2)
	public void some_property() {
        // runs two times
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
```

The mock container captures lifecycle method calls. After two tries of property `some_property`, there have been four 
 calls in total and two calls each to `beforeTest` and `afterTest`.

### Singleton containers

Note that the [singleton container pattern](https://www.testcontainers.org/test_framework_integration/manual_lifecycle_control#singleton-containers) is also an option when
using JUnit 5.

## Limitations

Lifecycle hooks use proximity to determine when a hook should be run. Proximity, is an integer value with an order
defined over it. This means, a before property hook with a proximity of 1 will be executed after a before property hook
with a proximity of 2. However, these values are hard coded and there might be unwanted effects when there are other
hooks and the order of execution is wrong.

