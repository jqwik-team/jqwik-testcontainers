package net.jqwik.testcontainers;

import org.testcontainers.utility.*;

public interface JqwikTestImages {
	DockerImageName POSTGRES_IMAGE = new DockerImageName("postgres:9.6.12");
	DockerImageName HTTPD_IMAGE = new DockerImageName("httpd:2.4-alpine");
}
