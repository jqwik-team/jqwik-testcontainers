package net.jqwik.testcontainers;

import java.io.*;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.testcontainers.containers.*;
import org.testcontainers.containers.wait.strategy.*;

import net.jqwik.api.*;
import net.jqwik.api.lifecycle.*;

import static org.assertj.core.api.Assertions.*;

@Testcontainers
class ComposeContainerTests {

	@Container
	private final DockerComposeContainer composeContainer = new DockerComposeContainer(
			new File("src/test/resources/docker-compose.yml"))
			.withExposedService("whoami_1", 80, Wait.forHttp("/"));

	private String host;

	private int port;

	@BeforeProperty
	void setup() {
		host = composeContainer.getServiceHost("whoami_1", 80);
		port = composeContainer.getServicePort("whoami_1", 80);
	}

	@Property(tries = 10)
	void running_compose_defined_container_is_accessible_on_configured_port() throws Exception {
		HttpClient client = HttpClientBuilder.create().build();

		HttpResponse response = client.execute(new HttpGet("http://" + host + ":" + port));

		assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);
	}
}
