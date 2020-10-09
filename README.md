# jqwik Testcontainers Support

This project provides an extension to support ...

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

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## How to Install

### Gradle

Follow the
[instructions here](https://jqwik.net/docs/current/user-guide.html#gradle)
and add the following dependency to your `build.gradle` file:

```
dependencies {
  ...
}
```

You can look at a
[sample project](https://github.com/jlink/jqwik-samples/tree/master/jqwik-testcontainers-gradle)
 using jqwik, Testcontainers and Gradle.

### Maven

Follow the
[instructions here](https://jqwik.net/docs/current/user-guide.html#maven)
and add the following dependency to your `pom.xml` file:

```
...
<dependency>
  <groupId>net.jqwik</groupId>
  <artifactId>jqwik-testcontainers</artifactId>
  <version>0.5.0</version>
  <scope>test</scope>
</dependency>
```

### Supported Testcontainers Versions

...

Please report any compatibility issues you stumble upon.

### Supported JUnit Platform Versions

You need at least version `1.7.0` of the JUnit platform - otherwise
strange things _could_ happen.

## Standard Usage
