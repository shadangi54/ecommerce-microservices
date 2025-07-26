## 🔒 Security Implementation

The microservices architecture includes comprehensive security features built around JWT (JSON Web Token) authentication and authorization.

### 🔑 **Auth Service** - Port: 8084

The Auth Service is the central authentication and authorization provider for the entire microservices ecosystem:

```properties
spring.application.name=auth-service
server.port=8084

# JWT Configuration
app.jwt.secret=eW91cl9zZWNyZXRfa2V5X3NoYWRhbmdpNTRfZWNvbW1lcmNlLW1pY3Jvc2VydmljZXM=
app.jwt.expiration=3600000  # 1 hour
```

#### Key Components:

1. **WebSecurityConfig** - Spring Security configuration:

   - Stateless session management
   - BCrypt password encoding
   - JWT token filter integration
   - Public endpoints configuration

2. **JwtUtils** - Token operations:

   - Token generation with user information
   - Token validation
   - Username extraction from tokens

3. **AuthTokenFilter** - Authentication filter:

   - Token extraction from requests
   - User validation and authentication context setup

4. **REST Endpoints**:
   - `/auth/signin` - Login and get JWT token
   - `/auth/signup` - Register new users
   - `/auth/test-response` - Test authentication

### 🔐 **Gateway Security**

The Gateway Service acts as the security enforcement point for the microservices ecosystem:

```properties
# JWT Configuration in Gateway
jwt.secret=eW91cl9zZWNyZXRfa2V5X3NoYWRhbmdpNTRfZWNvbW1lcmNlLW1pY3Jvc2VydmljZXM=
```

#### Key Components:

1. **JwtAuthenticationFilter** - Gateway filter for JWT validation:

   - Intercepts and validates JWT tokens
   - Extracts user information
   - Forwards authenticated requests to microservices
   - Returns 401 Unauthorized for invalid tokens

2. **Route Configuration** - Security rules:
   - Public routes (no JWT required): `/auth/**`, actuator endpoints
   - Protected routes (JWT required): All other services

### 🛡️ **Security Flow**

1. **Authentication Flow**:

   ```
   Client → Auth Service (/auth/signin) → JWT Token → Subsequent Requests with JWT
   ```

2. **Protected Resource Access**:

   ```
   Client → Gateway → JWT Validation → Route to Microservice → Response
   ```

3. **Role-Based Authorization**:
   - User roles: USER, ADMIN, MODERATOR
   - Role information included in JWT token
   - Endpoint authorization based on roles

### 🔧 **Enabling/Disabling JWT Security**

By default, JWT security is ready but commented out in the Gateway configuration. To enable/disable:

#### To Enable JWT Security:

1. Open `GatewayServiceApplication.java`
2. Uncomment the JWT filter in each service route:

   ```java
   // From:
   //.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config()))

   // To:
   .filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config()))
   ```

3. Save and restart the Gateway Service

#### To Disable JWT Security:

1. Open `GatewayServiceApplication.java`
2. Comment out the JWT filter in each service route:

   ```java
   // From:
   .filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config()))

   // To:
   //.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config()))
   ```

3. Save and restart the Gateway Service

### 🧪 **Testing Security**

Use the included Postman collection to test security features:

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

4. **Test Unauthorized Access**:

   ```
   GET http://localhost:9090/products
   // No Authorization header = 401 Unauthorized
   ```

5. **Test Invalid Token**:
   ```
   GET http://localhost:9090/products
   Header: Authorization: Bearer invalid-token
   // Invalid token = 401 Unauthorized
   ```

Use the included Postman collection `Ecommerce_Microservices_Complete_Final.postman_collection.json` which contains pre-configured security tests under the "🔒 Authentication Service APIs" and "🛡️ Security Tests" folders.

### ⚙️ **Start Auth Service**

```bash
cd auth-service
mvn clean spring-boot:run
```

The Auth Service must be running for JWT authentication to work properly.
