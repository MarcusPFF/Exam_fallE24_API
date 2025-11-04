# App


## Description


## Endpoints & Role Access
- http://localhost:7070/api/routes


### Public Endpoints (No Auth Required)
- `GET /api/auth/healthcheck` - Check API status
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - Register new user
- `GET /api/public/info` - API information
- 
### Guest User Endpoints


### Admin Only Endpoints
- `GET /api/admin/users` - List all users

## Security

### Password Hashing
- Passwords are hashed using **BCrypt** before storage
- Only the hash is saved in the database, never plain text passwords
- BCrypt handles salt generation automatically

### JWT Authentication
- All protected endpoints require a Bearer token in the Authorization header
- Format: `Authorization: Bearer <token>`
- Tokens contain: username, role, and expiration time
- Token validation is automatic for protected routes

## Tech Stack

**Framework**
- Javalin 6.3.0 (REST framework)

**Database & ORM**
- PostgreSQL 42.7.4
- Hibernate 6.2.4 (JPA)

**Security**
- TokenSecurity library (JWT)
- jBCrypt 0.4 (password hashing)

**Utilities**
- Jackson (JSON processing)
- Lombok (code generation)
- SLF4J + Logback (logging)
- HikariCP (connection pooling)

**Testing**
- JUnit 5
- REST Assured
- Testcontainers

## Error Handling

| Status Code | Meaning |
|-------------|---------|
| **200** | OK - Request successful |
| **400** | Bad Request - Invalid input or validation error |
| **401** | Unauthorized - Missing or invalid token |
| **403** | Forbidden - Insufficient role permissions |
| **500** | Internal Server Error - Unexpected server error |

Error responses include a JSON object with `error`, `status`, and `message` fields.

## Getting Started

1. Configure database connection in `src/main/resources/config.properties`
2. Set SECRET_KEY for JWT (or use default)
3. Run the application: `mvn clean package && java -jar target/app.jar`
4. Server starts on `http://localhost:7070/api`
