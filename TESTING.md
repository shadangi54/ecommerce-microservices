## 🧪 Testing the Application

### Using Postman Collection

The repository includes comprehensive Postman collections for testing all aspects of the microservices:

1. **Import the Collection**:

   - Import `Ecommerce_Microservices_Complete_Final.postman_collection.json` into Postman
   - Import `Ecommerce_Microservices_Environment.postman_environment.json` for environment variables

2. **Organize Test Execution**:

   - **🔍 Discovery Tests** - Verify service registration
   - **🔒 Authentication Service APIs** - Test security features
   - **🛒 Product Service** - CRUD operations for products
   - **📦 Order Service** - Create and manage orders
   - **📊 Inventory Service** - Stock management operations
   - **🛡️ Security Tests** - Test JWT authentication and authorization

3. **Security Testing**:
   - **Register User**: Use `/auth/signup` endpoint to create users
   - **Login**: Use `/auth/signin` to get JWT token
   - **Access Control Tests**: Test secured endpoints with and without tokens
   - **Role-Based Access**: Test ADMIN vs USER permissions

### Manual API Testing

**1. Authentication (when security is enabled):**

```bash
# Get JWT token
curl -X POST http://localhost:9090/auth/signin \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' \
  | jq -r ".token" > token.txt

# Store token in variable (Unix/Mac)
TOKEN=$(cat token.txt)
```

**2. Product Service:**

```bash
# Get all products
curl -H "Authorization: Bearer $TOKEN" http://localhost:9090/products

# Get specific product
curl -H "Authorization: Bearer $TOKEN" http://localhost:9090/products/1

# Create product (ADMIN role)
curl -X POST -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"New Product","price":99.99,"sku":"TEST-SKU-999"}' \
  http://localhost:9090/products
```

**3. Order Service:**

```bash
# Create order
curl -X POST -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"customerName":"Test Customer","customerEmail":"test@example.com","items":[{"skuCode":"IPHONE14PRO-256-BLACK","quantity":1}]}' \
  http://localhost:9090/orders

# Get all orders
curl -H "Authorization: Bearer $TOKEN" http://localhost:9090/orders
```

**4. Inventory Service:**

```bash
# Check stock for SKU
curl -H "Authorization: Bearer $TOKEN" \
  "http://localhost:9090/inventory?skuCodes=IPHONE14PRO-256-BLACK"
```

### Security Testing Scenarios

1. **Test Authentication Endpoints**:

   - Register a new user with different roles
   - Login and obtain JWT token
   - Verify token contents and expiration

2. **Test Unauthenticated Access**:

   - Access protected endpoints without a token
   - Should return 401 Unauthorized

3. **Test Invalid Authentication**:

   - Use expired or malformed tokens
   - Should return appropriate error responses

4. **Test Role-Based Authorization**:

   - Access admin-only endpoints with a regular user token
   - Should return 403 Forbidden

5. **Test Public Endpoints**:
   - Verify that public endpoints remain accessible without tokens
   - Examples: `/auth/signin`, `/auth/signup`, health checks

For detailed security testing information, refer to [SECURITY.md](SECURITY.md) and the Postman collection documentation.
