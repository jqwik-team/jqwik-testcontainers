package net.jqwik.testcontainers;

import org.testcontainers.utility.*;

public interface JqwikTestImages {
	DockerImageName POSTGRES_IMAGE = DockerImageName.parse("postgres:9.6.12");
	DockerImageName REDIS_IMAGE = DockerImageName.parse("redis:3.2.11");
	DockerImageName MYSQL_IMAGE = DockerImageName.parse("mysql:5.7.34");
	DockerImageName HTTPD_IMAGE = DockerImageName.parse("httpd:2.4-alpine");
}
