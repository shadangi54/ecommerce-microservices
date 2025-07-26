package com.shadangi54.auth.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

/**
 * Consolidated security configuration for Auth Service.
 * This combines the functionality of both Authorization Server and Resource Server.
 */
@Configuration
public class SecurityConfig {
    
    /**
     * Configuration for OAuth2 Authorization Server endpoints.
     * Handles OAuth2 protocol flows for token issuance.
     */
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        // SECURITY DISABLED: Original OAuth2 Authorization Server configuration commented out
        /* 
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
            .oidc(Customizer.withDefaults());
        
        http
            .exceptionHandling(exceptions -> 
                exceptions.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")))
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        */
        
        // Permit all requests without security checks
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/**").permitAll()
            );
        
        return http.build();
    }
    
    /**
     * Configuration for authentication endpoints like login and direct auth endpoints.
     * Handles form login and direct authentication flows.
     */
    @Bean
    @Order(2)
    public SecurityFilterChain authenticationSecurityFilterChain(HttpSecurity http) throws Exception {
        // SECURITY DISABLED: Original authentication configuration commented out
        /*
        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.addHeaderWriter((request, response) -> {
                response.setHeader("X-Frame-Options", "SAMEORIGIN");
            }))
            .securityMatcher("/login", "/register", "/oauth2/**", "/actuator/**", "/h2-console/**",
                            "/auth/signin", "/auth/refresh", "/auth/logout", "/auth/register")
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/register", "/actuator/**", "/h2-console/**",
                               "/auth/signin", "/auth/refresh", "/auth/logout", "/auth/register").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(Customizer.withDefaults());
        */
        
        // Permit all requests without security checks
        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.addHeaderWriter((request, response) -> {
                response.setHeader("X-Frame-Options", "SAMEORIGIN");
            }))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/**").permitAll()
            );
        
        return http.build();
    }
    
    /**
     * Configuration for protecting API resources.
     * Enforces JWT-based authentication for protected endpoints.
     */
    @Bean
    @Order(3)
    public SecurityFilterChain resourceServerSecurityFilterChain(HttpSecurity http) throws Exception {
        // SECURITY DISABLED: Original resource server configuration commented out
        /*
        http
            .csrf(csrf -> csrf.disable())
            .securityMatcher(request -> 
                !request.getRequestURI().startsWith("/oauth2/") && 
                !request.getRequestURI().equals("/login") && 
                !request.getRequestURI().equals("/register") && 
                !request.getRequestURI().startsWith("/actuator/") &&
                !request.getRequestURI().startsWith("/h2-console") &&
                !request.getRequestURI().equals("/auth/signin") &&
                !request.getRequestURI().equals("/auth/refresh") &&
                !request.getRequestURI().equals("/auth/logout") &&
                !request.getRequestURI().equals("/auth/register"))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/public/**", 
                                "/auth/register", "/auth/signin", "/auth/refresh", "/auth/logout").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(Customizer.withDefaults())
            );
        */
        
        // Permit all requests without security checks
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/**").permitAll()
            );
        
        return http.build();
    }
    
    /**
     * Configures authentication manager for username/password authentication.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http, 
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) throws Exception {
        
        var authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
        
        return authManagerBuilder.build();
    }
    
    /**
     * Client registration for OAuth2 clients.
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder) {
        RegisteredClient webClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("web-client")
                .clientSecret(passwordEncoder.encode("secret"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .redirectUri("http://localhost:8080/login/oauth2/code/web-client-oidc")
                .redirectUri("http://localhost:8080/authorized")
                .scope(OidcScopes.OPENID)
                .scope("read")
                .scope("write")
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(30))
                        .refreshTokenTimeToLive(Duration.ofDays(1))
                        .build())
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(true)
                        .build())
                .build();

        return new InMemoryRegisteredClientRepository(webClient);
    }
    
    /**
     * Converter for JWT authentication to extract roles.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        // Set the authorities claim name to avoid casting issues
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
    
    /**
     * JWK source for JWT signing and verification.
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    /**
     * Helper method to generate RSA key pair.
     */
    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    /**
     * JWT decoder for verifying tokens.
     */
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }
    
    /**
     * JWT encoder for creating tokens.
     */
    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    /**
     * Authorization server settings.
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings(@Value("${app.jwt.issuer-uri}") String issuerUri) {
        return AuthorizationServerSettings.builder()
                .issuer(issuerUri)  // Use the configurable issuer URI
                .authorizationEndpoint("/oauth2/authorize")
                .tokenEndpoint("/oauth2/token")
                .jwkSetEndpoint("/oauth2/jwks")
                .tokenRevocationEndpoint("/oauth2/revoke")
                .tokenIntrospectionEndpoint("/oauth2/introspect")
                .oidcUserInfoEndpoint("/userinfo")
                .oidcClientRegistrationEndpoint("/connect/register")
                .build();
    }

    /**
     * Password encoder for secure password storage and verification.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
