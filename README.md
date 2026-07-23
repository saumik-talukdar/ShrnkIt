# ShrnkIt

> A production-inspired URL Shortener API built with Spring Boot, PostgreSQL, Redis, and JWT Authentication.

ShrnkIt is a backend-focused project designed to demonstrate modern backend engineering practices beyond simple CRUD operations. The project implements secure authentication, asynchronous event processing, Redis caching, analytics collection, and a clean layered architecture.

---

# Features

## Authentication & Security

- JWT Access Token authentication
- Refresh Token Rotation
- Redis-backed refresh token storage
- HTTP Only Refresh Cookies
- Email Verification
- Forgot Password / Reset Password
- Password Versioning (Logout after password change)
- Logout
- Logout from all devices
- BCrypt password hashing
- Global exception handling
- Input validation

---

## URL Shortening

- Create Short URLs
- Custom Alias Support
- Random Short Code Generation
- URL Expiration
- Activate / Deactivate URLs
- Ownership based access control
- Redirect using short code

---

## Analytics

Current implementation includes:

- Total Click Count
- Unique Visitor Tracking
- Visitor Identification using Cookies
- Browser Detection
- Operating System Detection
- Device Type Detection
- Traffic Source Detection (Referer)

> Timeline analytics, breakdowns and advanced insights are currently under development.

---

## Performance

- Redis Cache-Aside Pattern
- Cached URL Resolution
- Reduced Database Hits
- Async Event Processing
- Non-blocking Email Delivery

---

## Architecture

- Layered Architecture
- Domain Driven Package Structure
- Event Driven Design using Spring Events
- Transactional Event Listeners
- Asynchronous Event Processing
- DTO based API
- Repository Pattern
- Service Layer Business Logic

---

# Tech Stack

| Technology | Usage |
|------------|-------|
| Java 24 | Language |
| Spring Boot 4 | Backend Framework |
| Spring Security | Authentication & Authorization |
| Spring Data JPA | Persistence |
| PostgreSQL | Database |
| Redis | Cache & Refresh Tokens |
| JWT | Authentication |
| Spring Events | Event Driven Architecture |
| Jakarta Validation | Request Validation |
| Maven | Dependency Management |

---

# Project Structure

```
src
 ├── common
 │    ├── exception
 │    ├── http
 │    └── response
 │
 ├── config
 │
 ├── domain
 │    ├── auth
 │    ├── user
 │    ├── url
 │    └── analytics
 │
 └── security
```

---

# Authentication Flow

```
Register
      │
      ▼
Create User
      │
      ▼
Publish UserRegisteredEvent
      │
      ▼
Async Email Verification

------------------------------

Login
      │
      ▼
Generate JWT Access Token
      │
      ▼
Generate Refresh Token
      │
      ▼
Store Refresh Token in Redis

------------------------------

Refresh Token
      │
      ▼
Validate Redis Token
      │
      ▼
Rotate Refresh Token
      │
      ▼
Issue New JWT
```

---

# URL Resolution Flow

```
Client

   │

   ▼

GET /{shortCode}

   │

   ▼

Redis Cache

   │
Cache Miss

   ▼

Database

   │

   ▼

Store in Redis

   │

   ▼

Redirect User

   │

   ▼

Publish UrlVisitedEvent

   │

   ▼

Async Analytics Recording
```

---

# Event Driven Design

Current domain events:

- UserRegisteredEvent
- PasswordResetRequestedEvent
- UrlVisitedEvent

Events are processed asynchronously after the database transaction commits using:

- Spring Events
- @TransactionalEventListener
- @Async

---

# API Endpoints

## Authentication

| Method | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/v1/auth/register` | Register account |
| POST | `/api/v1/auth/login` | Login |
| POST | `/api/v1/auth/refresh` | Refresh access token |
| POST | `/api/v1/auth/logout` | Logout current device |
| POST | `/api/v1/auth/logout-all` | Logout all devices |
| GET | `/api/v1/auth/verify-email` | Verify email |
| POST | `/api/v1/auth/resend-verification` | Resend verification email |
| POST | `/api/v1/auth/forgot-password` | Request password reset |
| POST | `/api/v1/auth/reset-password` | Reset password |

---

## User

| Method | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/v1/users/change-password` | Change password |

---

## URL

| Method | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/v1/urls` | Create short URL |
| GET | `/api/v1/urls` | List user's URLs |
| GET | `/api/v1/urls/{id}` | Get URL details |
| PATCH | `/api/v1/urls/{id}` | Update URL |
| DELETE | `/api/v1/urls/{id}` | Delete URL |
| PATCH | `/api/v1/urls/{id}/activate` | Activate URL |
| PATCH | `/api/v1/urls/{id}/deactivate` | Deactivate URL |

---

## Redirect

| Method | Endpoint | Description |
|---------|----------|-------------|
| GET | `/{shortCode}` | Redirect to original URL |

---

## Analytics

| Method | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/v1/urls/{id}/analytics` | Retrieve click statistics |

---

# Redis Usage

ShrnkIt uses Redis for multiple responsibilities:

- Refresh Token Storage
- Email Verification Tokens
- Password Reset Tokens
- URL Cache
- Cache Eviction

---

# Security Features

- Stateless Authentication
- Refresh Token Rotation
- Password Version Validation
- Secure Password Hashing
- Email Verification
- Authorization by Resource Ownership
- Global Exception Handling

---

# Current Analytics

Each click records:

- Click Timestamp
- Visitor ID
- Browser
- Operating System
- Device Type
- Traffic Source

---

# Running Locally

## Requirements

- Java 24
- PostgreSQL
- Redis
- Maven

Clone the repository

```bash
git clone https://github.com/saumik-talukdar/ShrnkIt.git
```

```bash
cd ShrnkIt
```

Run

```bash
mvn spring-boot:run
```

---

# Future Improvements

- Advanced Analytics Dashboard
- Timeline Analytics
- Geographic Analytics
- QR Code Generation
- Custom Domains
- Rate Limiting
- API Keys
- Public Analytics Sharing
- Docker Deployment
- CI/CD Pipeline

---

# Engineering Highlights

- Clean Layered Architecture
- Event Driven Design
- Async Processing
- Cache Aside Pattern
- Redis Integration
- JWT Authentication
- Transactional Domain Events
- Secure Token Rotation
- Ownership Based Authorization
- Extensible Analytics Architecture

