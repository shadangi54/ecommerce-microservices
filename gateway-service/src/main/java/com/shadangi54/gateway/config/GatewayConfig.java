package com.shadangi54.gateway.config;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

@Component
public class GatewayConfig extends AbstractGatewayFilterFactory<GatewayConfig.Config> {

	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayConfig.class);

	public GatewayConfig() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {

			String requestId = UUID.randomUUID().toString();
			long startTime = System.currentTimeMillis();
			
			LOGGER.info("Processing request: {}, path: {}", requestId, exchange.getRequest().getPath());

			 exchange.getRequest().mutate()
					.header(config.getRequestIdHeaderName(), requestId)
			 .header(config.getTimestampHeaderName(), String.valueOf(System.currentTimeMillis()))
                    .build();
			
			
			
			LOGGER.info("Start time for request {}: {}", requestId, startTime);
			
			return chain.filter(exchange).doOnError(error -> {
				LOGGER.error("Error occurred during request processing: {}", error.getMessage(), error);
			}).then(Mono.fromRunnable(() -> {
				long duration = System.currentTimeMillis() - startTime;
		        LOGGER.info("Request {} completed in {} ms", requestId, duration);
		        HttpHeaders headers = exchange.getResponse().getHeaders();
		        headers.add(config.getResponseHeaderName(), config.getResponseHeaderValue());
                headers.add(config.getResponseTimeHeaderName(), String.valueOf(duration));
			}));
		};
	}
	
	@Getter
	@Setter
	public static class Config {
        private String requestIdHeaderName = "X-Gateway-Request-Id";
        private String timestampHeaderName = "X-Gateway-Timestamp";
        private String responseHeaderName = "X-Gateway-Response";
        private String responseHeaderValue = "Processed by API Gateway";
        private String responseTimeHeaderName = "X-Response-Time";
    }

}
