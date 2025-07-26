## 🔒 Security Implementation

The microservices architecture includes comprehensive security features built around OAuth2 and JWT (JSON Web Token) authentication and authorization.

### � **OAuth2 Authorization Server** - Port: 8084

The Auth Service is configured as an OAuth2 Authorization Server with the following features:

```properties
spring.application.name=auth-service
server.port=8084

# Database Configuration
spring.datasource.url=jdbc:h2:mem:authdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true

# OAuth2 & JWT Configuration
spring.security.oauth2.authorizationserver.client.default.client-id=client
spring.security.oauth2.authorizationserver.client.default.client-secret=secret
spring.security.oauth2.authorizationserver.client.default.authorization-grant-types=authorization_code,refresh_token,client_credentials,password
```

#### Key Components:

1. **AuthServerConfig** - OAuth2 Authorization Server configuration:

   - JWT token customization
   - Client registration
   - Grant type configuration
   - Token endpoint configuration

2. **ResourceServerConfig** - Resource Server configuration:

   - Protected endpoint configuration
   - Authorization rules
   - Token validation

3. **JWT Configuration**:

   - RSA key pair for signing and verification
   - Token converter and enhancer
   - Scope and role-based authorization

4. **REST Endpoints**:
   - `/oauth2/token` - OAuth2 token endpoint
   - `/oauth2/revoke` - Token revocation
   - `/oauth2/introspect` - Token introspection
   - `/public/**` - Public endpoints
   - `/api/**` - Protected resource endpoints
   - `/admin/**` - Admin-only endpoints

### 🔐 **Gateway Security**

The Gateway Service acts as the security enforcement point for the microservices ecosystem using OAuth2 Resource Server configuration:

```properties
# OAuth2 Resource Server Configuration in Gateway
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8084
```

#### Key Components:

1. **SecurityConfig** - Gateway security configuration:

   - OAuth2 Resource Server setup
   - JWT validation and processing
   - Route-specific security rules
   - Role-based authorization

2. **Route Configuration** - Security rules:
   - Public routes (no authentication required): `/auth/**`, actuator endpoints
   - Protected routes (authentication required): All other services
   - Admin routes (admin role required): `/admin/**` endpoints

### 🛡️ **Security Flow**

The application supports multiple authentication flows:

1. **OAuth2 Client Credentials Flow**:

   ```
   Client → Auth Service (/oauth2/token) [client_id + client_secret] → Access Token → Subsequent Requests with Bearer Token
   ```

2. **OAuth2 Password Grant Flow**:

   ```
   Client → Auth Service (/oauth2/token) [username + password] → Access Token + Refresh Token → Subsequent Requests with Bearer Token
   ```

3. **OAuth2 Refresh Token Flow**:

   ```
   Client → Auth Service (/oauth2/token) [refresh_token] → New Access Token → Subsequent Requests with Bearer Token
   ```

4. **Protected Resource Access**:

   ```
   Client → Gateway → OAuth2 Token Validation → Route to Microservice → Response
   ```

5. **Role-Based Authorization**:
   - User roles: USER, ADMIN
   - Role information included in JWT token
   - Endpoint authorization based on roles and scopes

### 🔧 **Enabling/Disabling OAuth2 Security**

The OAuth2 security is configured in both the Auth Service and Gateway Service:

#### In Auth Service:

1. The `AuthServerConfig.java` and `ResourceServerConfig.java` files contain the OAuth2 configuration.
2. Default client credentials and user credentials are configured in `data.sql`.

#### In Gateway Service:

1. Open `SecurityConfig.java` to modify the security rules:

   ```java
   @Bean
   public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
       return http
           .csrf(ServerHttpSecurity.CsrfSpec::disable)
           .authorizeExchange(exchanges -> exchanges
               .pathMatchers("/auth/**", "/actuator/**").permitAll()
               .pathMatchers("/admin/**").hasRole("ADMIN")
               .anyExchange().authenticated()
           )
           .oauth2ResourceServer(oauth2 -> oauth2
               .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
           )
           .build();
   }
   ```

2. To disable security temporarily for development, you can modify the configuration:

   ```java
   @Bean
   public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
       return http
           .csrf(ServerHttpSecurity.CsrfSpec::disable)
           .authorizeExchange(exchanges -> exchanges
               .anyExchange().permitAll()  // Allow all requests without authentication
           )
           .build();
   }
   ```

3. Save and restart the Gateway Service

### ⚙️ **Start Auth Service**

```bash
cd auth-service
mvn clean spring-boot:run
```

The Auth Service must be running for OAuth2 authentication to work properly. Use the included start script to launch all services:

```bash
.\start-oauth2-services.bat
```

The project includes comprehensive security testing using OAuth2 and JWT. Use the integrated Postman collection to test all security features:

#### JWT Authentication Testing

1. **Register a User**:

   ```
   POST http://localhost:9090/auth/signup

   {
     "username": "testuser",
     "email": "test@example.com",
     "password": "password123",
     "roles": ["user"]
   }
   ```

2. **Get Authentication Token**:

   ```
   POST http://localhost:9090/auth/signin

   {
     "username": "testuser",
     "password": "password123"
   }
   ```

   - Response contains JWT token: `{"token": "eyJhbGciOiJIUzI1..."}`

3. **Access Protected Resource**:

   ```
   GET http://localhost:9090/products
   Header: Authorization: Bearer eyJhbGciOiJIUzI1...
   ```

#### OAuth2 Authentication Testing

The project now includes OAuth2 security implementation with the following flows:

1. **Register New User**:

   - Creates users with specific roles (USER or ADMIN)

2. **Client Credentials Flow**:

   - Obtain tokens using client credentials
   - Test access with different scopes

3. **Password Grant Flow**:

   - Authenticate with username/password
   - Obtain and validate access tokens

4. **Refresh Token Flow**:

   - Use refresh tokens to obtain new access tokens
   - Test token expiration

5. **Direct Service Security Tests**:

   - Test accessing protected resources directly
   - Validate role-based authorization

6. **Gateway Security Tests**:

   - Test accessing protected resources through the gateway
   - Validate token propagation

7. **Token Inspection & Management**:
   - Examine token contents
   - Test token revocation

Use the integrated Postman collection `Ecommerce_Microservices_Complete_Final.postman_collection.json` which contains all pre-configured security tests under the "🔒 Authentication Service APIs", "🛡️ Security Tests", and "� OAuth2 Security Testing" folders.

### ⚙️ **Start Auth Service**

```bash
cd auth-service
mvn clean spring-boot:run
```

The Auth Service must be running for JWT authentication to work properly.
