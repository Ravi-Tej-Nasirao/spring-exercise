package uk.co.lexisnexis.risk.search.company.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;


/**
 * web client config
 *
 * @author ravin
 * @date 2024/05/25
 */
@Component
@RequiredArgsConstructor
public class WebClientConfig {

	@Bean
	public WebClient webClient() {
		return WebClient.builder().clientConnector(connector()).build();
	}

	private ClientHttpConnector connector() {
		return new ReactorClientHttpConnector(HttpClient.create(ConnectionProvider.newConnection()));
	}

}
