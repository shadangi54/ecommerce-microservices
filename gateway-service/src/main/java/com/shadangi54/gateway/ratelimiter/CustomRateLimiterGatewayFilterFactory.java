package com.shadangi54.gateway.ratelimiter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class CustomRateLimiterGatewayFilterFactory
		extends AbstractGatewayFilterFactory<CustomRateLimiterGatewayFilterFactory.Config> {

	private final RedisRateLimiter redisRateLimiter;

	public CustomRateLimiterGatewayFilterFactory(RedisRateLimiter redisRateLimiter) {
		super(Config.class);
		this.redisRateLimiter = redisRateLimiter;
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) ->{
			return redisRateLimiter.isAllowed(config.getRouteId(), exchange.getRequest().getRemoteAddress().getAddress().getHostAddress())
					.flatMap(response -> {
						if(response.isAllowed()) {
							return chain.filter(exchange);
						}
						
						// Rate limit exceeded - set response headers and body
						exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
						exchange.getResponse().getHeaders().add("Content-Type", "application/json");
						exchange.getResponse().getHeaders().add("X-RateLimit-Limit",
								response.getHeaders().get("X-RateLimit-Limit"));
						exchange.getResponse().getHeaders().add("X-RateLimit-Remaining", "0");
						exchange.getResponse().getHeaders().add("X-RateLimit-Retry-After-Seconds", "60");
						
						// Create proper JSON response body
						String jsonResponse = "{"
								+ "\"message\":\"Too many requests\","
								+ "\"reason\":\"rate-limit-exceeded\","
								+ "\"status\":\"RATE_LIMIT_EXCEEDED\","
								+ "\"timestamp\":\"" + java.time.LocalDateTime.now() + "\","
								+ "\"suggestion\":\"Please wait before making more requests\","
								+ "\"errorCode\":\"TOO_MANY_REQUESTS\","
								+ "\"retryAfter\":\"60 seconds\""
								+ "}";
						
						// Make sure to set the Content-Length header
		                byte[] bytes = jsonResponse.getBytes();
		                exchange.getResponse().getHeaders().add("Content-Length", String.valueOf(bytes.length));

		                // Return the response with the body
		                return exchange.getResponse().writeWith(
		                    Mono.just(exchange.getResponse().bufferFactory().wrap(bytes))
		                 );
					});
		};
	}

	public static class Config {
		private String routeId;

		public String getRouteId() {
			return routeId;
		}

		public void setRouteId(String routeId) {
			this.routeId = routeId;
		}
	}

}
