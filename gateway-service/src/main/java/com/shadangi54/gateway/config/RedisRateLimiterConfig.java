package com.shadangi54.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.core.publisher.Mono;

@Configuration
public class RedisRateLimiterConfig {
	
	@Bean
	public RedisRateLimiter redisRateLimiter() {
		//Configure default rate(requests per second) and burst capacity
		return new RedisRateLimiter(5, 10);
	}
	
	@Bean
	public KeyResolver useKeyResolver() {
		//Use the request path as the key for rate limiting
		return exchange -> Mono.just(exchange.getRequest().getPath().value());
	}
}
