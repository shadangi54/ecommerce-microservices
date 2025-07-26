package com.shadangi54.gateway.filter;

import java.security.Key;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

	@Value("${jwt.secret}")
	private String secret;
	
	public JwtAuthenticationFilter() {
		super(Config.class);
	}
	
	@Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            final List<String> apiEndpoints = List.of("/auth/signin", "/auth/signup", 
                    "/auth/h2-console", "/h2-console", "/actuator", "/health");

            Predicate<ServerHttpRequest> isApiSecured = r -> apiEndpoints.stream()
                    .noneMatch(uri -> r.getURI().getPath().contains(uri));

            if (isApiSecured.test(request)) {
                if (!request.getHeaders().containsKey("Authorization")) {
                    return onError(exchange, "No Authorization header", HttpStatus.UNAUTHORIZED);
                }

                String authHeader = request.getHeaders().getOrEmpty("Authorization").get(0);
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    return onError(exchange, "Invalid Authorization header format", HttpStatus.UNAUTHORIZED);
                }

                String token = authHeader.substring(7);
                try {
                    if (!validateToken(token)) {
                        return onError(exchange, "Invalid JWT token", HttpStatus.UNAUTHORIZED);
                    }
                    
                    // Extract user information from token and add to request headers if needed
                    String username = getUsernameFromToken(token);
                    exchange = exchange.mutate()
                            .request(exchange.getRequest().mutate()
                                    .header("X-Auth-User", username)
                                    .build())
                            .build();
                    
                } catch (Exception e) {
                    return onError(exchange, "Invalid JWT token: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
                }
            }

            return chain.filter(exchange);
        };
    }
	 
	 private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
	        ServerHttpResponse response = exchange.getResponse();
	        response.setStatusCode(httpStatus);
	        return response.setComplete();
	    }

	    private String getUsernameFromToken(String token) {
	        Claims claims = Jwts.parserBuilder()
	                .setSigningKey(key())
	                .build()
	                .parseClaimsJws(token)
	                .getBody();
	        
	        return claims.getSubject();
	    }

    private boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // Log the specific error for debugging
            System.out.println("JWT validation error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }	    private Key key() {
	        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	    }

	
	 public static class Config {
	        // Put configuration properties here
	    }

}
