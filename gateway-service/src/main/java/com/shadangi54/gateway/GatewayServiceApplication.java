package com.shadangi54.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.shadangi54.gateway.ratelimiter.CustomRateLimiterGatewayFilterFactory;

import reactor.core.publisher.Mono;

@SpringBootApplication
public class GatewayServiceApplication {

	private static final Logger logger = LoggerFactory.getLogger(GatewayServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
	}

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder,
			@Value("${product.service.url}") String productServiceUrl,
			@Value("${order.service.url}") String orderServiceUrl,
			@Value("${inventory.service.url}") String inventoryServiceUrl,
			@Value("${notification.service.url}") String notificationServiceUrl,
//			RedisRateLimiter redisRateLimiter
			CustomRateLimiterGatewayFilterFactory customRateLimiter
			) {

		return builder.routes()
				 // Product Service Routes with Circuit Breaker
				.route("product-service",
						r -> r.path("/products/**")
								.filters(f -> f
										.circuitBreaker(config -> config.setName("product-service-circuit-breaker")
										.setFallbackUri("forward:/fallback/product"))
										.retry(retryConfig -> retryConfig.setRetries(3))
//										.requestRateLimiter(rl-> rl
//												.setRateLimiter(redisRateLimiter)
//												.setKeyResolver(exchange -> Mono.just("product-service-rate-limit")))
										.filter(customRateLimiter.apply(c->c.setRouteId("product-service-rate-limit")))
										)
								.uri(productServiceUrl))
				
				// Order Service Routes with Circuit Breaker
				.route("order-service",
						r -> r.path("/orders/**")
								.filters(f -> f
										.circuitBreaker(config -> config.setName("order-service-circuit-breaker")
										.setFallbackUri("forward:/fallback/product"))
										.retry(retryConfig -> retryConfig.setRetries(3))
//										.requestRateLimiter(rl-> rl
//												.setRateLimiter(redisRateLimiter)
//												.setKeyResolver(exchange -> Mono.just("order-service-rate-limit")))
										.filter(customRateLimiter.apply(c->c.setRouteId("order-service-rate-limit")))
										)
								.uri(orderServiceUrl))
				
				// Inventory Service Routes with Circuit Breaker
				.route("inventory-service",
						r -> r.path("/inventory/**")
								.filters(f -> f
										.circuitBreaker(config -> config.setName("inventory-service-circuit-breaker")
										.setFallbackUri("forward:/fallback/product"))
										.retry(retryConfig -> retryConfig.setRetries(3))
//										.requestRateLimiter(rl-> rl
//												.setRateLimiter(redisRateLimiter)
//												.setKeyResolver(exchange -> Mono.just("inventory-service-rate-limit")))
										.filter(customRateLimiter.apply(c->c.setRouteId("inventory-service-rate-limit")))
										)
								.uri(inventoryServiceUrl))
				.route("product-service-health", r -> r.path("/health/product")
						.filters(f -> f.rewritePath("/health/product", "/actuator/health"))
						.uri(productServiceUrl))
				
				// Health Check Routes (without circuit breaker for monitoring)
				.route("order-service-health",
						r -> r.path("/health/order")
						.filters(f -> f.rewritePath("/health/order", "/actuator/health"))
						.uri(orderServiceUrl))
				.route("inventory-service-health", r -> r.path("/health/inventory")
						.filters(f -> f.rewritePath("/health/inventory", "/actuator/health"))
						.uri(inventoryServiceUrl))
				.route("notification-service-health",
						r -> r.path("/health/notification")
								.filters(f -> f.rewritePath("/health/notification", "/actuator/health"))
								.uri(notificationServiceUrl))
				.build();
	}

	@Bean
	public CorsWebFilter corsWebFilter() {
		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.addAllowedOriginPattern("*");
		corsConfig.addAllowedMethod("*");
		corsConfig.addAllowedHeader("*");
		corsConfig.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);

		return new CorsWebFilter(source);
	}

}
