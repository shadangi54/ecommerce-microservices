# Postman Collections URL Updates Summary

## Overview

Updated all Postman collections to use the correct URLs based on the microservices architecture configuration.

## Architecture Configuration

- **Gateway Service**: Port 9090 (routes to all other services)
- **Product Service**: Port 8080 (direct access)
- **Order Service**: Port 8081 (direct access)
- **Inventory Service**: Port 8082 (direct access)
- **Notification Service**: Port 8083 (direct access)

## Gateway Routes Configuration

Based on `GatewayServiceApplication.java`:

```java
.route("product-service", r->r.path("/products/**").uri(productServiceUrl))
.route("order-service", r->r.path("/orders/**").uri(orderServiceUrl))
.route("inventory-service", r->r.path("/inventory/**").uri(inventoryServiceUrl))
```

## Collections Updated

### 1. Ecommerce_Microservices_Gateway_v5.postman_collection.json

**Changes Made:**

- ✅ Updated gateway port from `8080` to `9090`
- ✅ Removed `/api` prefix from all routes to match gateway configuration
- ✅ Updated description to reflect correct port numbers
- ✅ All routes now use correct format: `localhost:9090/products`, `localhost:9090/orders`, `localhost:9090/inventory`

### 2. ecommerce-microservices-combined.postman_collection.json

**Changes Made:**

- ✅ Added `gatewayServiceUrl` variable: `http://localhost:9090`
- ✅ Added `inventoryServiceUrl` variable: `http://localhost:8082`
- ✅ Added `notificationServiceUrl` variable: `http://localhost:8083`
- ✅ Updated description to explain both gateway and direct access approaches
- ✅ Maintained existing direct service URLs for comparison testing

### 3. Ecommerce_Microservices_Complete_v3.postman_collection.json

**Changes Made:**

- ✅ Added `gatewayServiceUrl` variable: `http://localhost:9090`
- ✅ Added `notificationServiceUrl` variable: `http://localhost:8083`
- ✅ Updated description to include gateway routing information
- ✅ Maintained existing service URLs for direct access

### 4. NEW: Ecommerce_Microservices_Gateway_and_Direct_v6.postman_collection.json

**Created comprehensive collection with:**

- ✅ Gateway Health & Monitoring section
- ✅ Separate folders for Gateway Routes vs Direct Access
- ✅ Side-by-side comparison capabilities
- ✅ All service URLs properly configured
- ✅ Detailed descriptions for each approach

## Individual Service Collections (No Changes Required)

- **ProductService_v2.postman_collection.json**: ✅ Correctly uses `localhost:8080`
- **OrderService_v1.postman_collection.json**: ✅ Correctly uses `localhost:8081`

## URL Patterns Summary

### Gateway Service (Port 9090)

- Health Check: `http://localhost:9090/actuator/health`
- Gateway Routes: `http://localhost:9090/actuator/gateway/routes`
- Products: `http://localhost:9090/products/**`
- Orders: `http://localhost:9090/orders/**`
- Inventory: `http://localhost:9090/inventory/**`
- Service Health Checks:
  - Product: `http://localhost:9090/health/product`
  - Order: `http://localhost:9090/health/order`
  - Inventory: `http://localhost:9090/health/inventory`
  - Notification: `http://localhost:9090/health/notification`

### Direct Service Access

- Product Service: `http://localhost:8080/products/**`
- Order Service: `http://localhost:8081/orders/**`
- Inventory Service: `http://localhost:8082/inventory/**`
- Notification Service: `http://localhost:8083/**`

## Testing Recommendations

1. **Use Gateway v5 Collection** for testing through the API Gateway
2. **Use Gateway & Direct v6 Collection** for comparing both approaches
3. **Use Combined Collection** for flexible testing with variables
4. **Use Complete v3 Collection** for comprehensive integration testing
5. **Use individual service collections** for direct service testing

## Variables Available in Collections

All updated collections now include these variables:

- `gatewayServiceUrl`: `http://localhost:9090`
- `productServiceUrl`: `http://localhost:8080`
- `orderServiceUrl`: `http://localhost:8081`
- `inventoryServiceUrl`: `http://localhost:8082`
- `notificationServiceUrl`: `http://localhost:8083`
- `productId`: `1` (for testing)
- `customerName`: `John Doe` (for testing)
- Additional collection-specific variables as needed

## Validation

All URLs have been updated to match:

1. ✅ Gateway service configuration in `application.properties`
2. ✅ Route definitions in `GatewayServiceApplication.java`
3. ✅ Actual service endpoint mappings in controller classes
4. ✅ Service port configurations across all microservices

The Postman collections are now fully aligned with the microservices architecture and ready for testing.
