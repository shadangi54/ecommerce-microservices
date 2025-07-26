package com.shadangi54.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.shadangi54.gateway.ratelimiter.CustomRateLimiterGatewayFilterFactory;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayServiceApplication {

	private static final Logger logger = LoggerFactory.getLogger(GatewayServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
	}

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder,
//			RedisRateLimiter redisRateLimiter
			CustomRateLimiterGatewayFilterFactory customRateLimiter
			) {

		return builder.routes()
				
				// Auth Service Route (without JWT Filter)
	            .route("auth-service",
	                    r -> r.path("/auth/**")
	                          .uri("lb://auth-service"))
	            
	            // Auth Service H2-Console route
	            .route("auth-service-h2-console",
	                    r -> r.path("/auth/h2-console/**")
	                          .filters(f -> f
	                                 .rewritePath("/auth/h2-console/(?<segment>.*)", "/h2-console/${segment}")
	                                 .addResponseHeader("X-Frame-Options", "SAMEORIGIN")
	                                 .addResponseHeader("Content-Security-Policy", "frame-ancestors 'self'"))
	                          .uri("lb://auth-service"))
	                          
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
								.uri("lb://product-service"))
				
				// Order Service Routes with Circuit Breaker
				.route("order-service",
						r -> r.path("/orders/**")
								.filters(f -> f
										.circuitBreaker(config -> config.setName("order-service-circuit-breaker")
										.setFallbackUri("forward:/fallback/order"))
										.retry(retryConfig -> retryConfig.setRetries(3))
//										.requestRateLimiter(rl-> rl
//												.setRateLimiter(redisRateLimiter)
//												.setKeyResolver(exchange -> Mono.just("order-service-rate-limit")))
										.filter(customRateLimiter.apply(c->c.setRouteId("order-service-rate-limit")))
										)
								.uri("lb://order-service"))
				
				// Inventory Service Routes with Circuit Breaker
				.route("inventory-service",
						r -> r.path("/inventory/**")
								.filters(f -> f
										.circuitBreaker(config -> config.setName("inventory-service-circuit-breaker")
										.setFallbackUri("forward:/fallback/inventory"))
										.retry(retryConfig -> retryConfig.setRetries(3))
//										.requestRateLimiter(rl-> rl
//												.setRateLimiter(redisRateLimiter)
//												.setKeyResolver(exchange -> Mono.just("inventory-service-rate-limit")))
										.filter(customRateLimiter.apply(c->c.setRouteId("inventory-service-rate-limit")))
										)
								.uri("lb://inventory-service"))
				.route("product-service-health", r -> r.path("/health/product")
						.filters(f -> f.rewritePath("/health/product", "/actuator/health"))
						.uri("lb://product-service"))
				
				// Health Check Routes (without circuit breaker for monitoring)
				.route("order-service-health",
						r -> r.path("/health/order")
						.filters(f -> f.rewritePath("/health/order", "/actuator/health"))
						.uri("lb://order-service"))
				.route("inventory-service-health", r -> r.path("/health/inventory")
						.filters(f -> f.rewritePath("/health/inventory", "/actuator/health"))
						.uri("lb://inventory-service"))
				.route("notification-service-health",
						r -> r.path("/health/notification")
								.filters(f -> f.rewritePath("/health/notification", "/actuator/health"))
								.uri("lb://notification-service"))
				.build();
	}

	@Bean
	public CorsWebFilter corsWebFilter() {
		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.addAllowedOriginPattern("*");
		corsConfig.addAllowedMethod("*");
		corsConfig.addAllowedHeader("*");
		corsConfig.setAllowCredentials(true);
		corsConfig.addExposedHeader("X-Frame-Options");

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);

		return new CorsWebFilter(source);
	}
	
	@Bean
    public RouteLocator h2ConsoleRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("h2-console-route", r -> r
                .path("/auth/h2-console/**")
                .filters(f -> f
                    .removeRequestHeader("X-Frame-Options")
                    .addResponseHeader("X-Frame-Options", "SAMEORIGIN")
                    .rewritePath("/auth/h2-console/(?<segment>.*)", "/h2-console/${segment}"))
                .uri("lb://auth-service"))
            .build();
    }

}
