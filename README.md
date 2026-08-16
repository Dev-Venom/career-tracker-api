# Career Tracker API

Career Tracker is a backend REST API designed to help users manage and monitor their job-search journey.

The API provides functionality for user authentication, job application tracking, interview management, notifications, dashboard data, and career analytics. It is built with Spring Boot using a layered architecture and secured with Spring Security and JSON Web Tokens.

## Overview

Career Tracker provides a centralized system for managing the different stages of a job search.

The backend is responsible for:

- User authentication and authorization
- Job application management
- Application status tracking
- Interview scheduling and management
- Notifications
- Dashboard statistics
- Career analytics
- Secure REST API access

The backend is designed to work with a separate frontend application that consumes the REST APIs.

## Features

### Authentication and Security

- User registration and login
- JWT-based authentication
- Access token and refresh token support
- Spring Security integration
- BCrypt password hashing
- Protected API endpoints
- Custom JWT authentication filter
- Externalized database and JWT configuration
- CORS configuration

### Job Applications

- Create job applications
- Retrieve applications
- Retrieve individual applications
- Update applications
- Delete applications
- Filter applications by status
- Track application progress

Supported application statuses include:

- Applied
- Interview
- Offer
- Rejected

### Interview Management

- Schedule interviews
- Retrieve interviews
- Update interview information
- Delete interviews
- Track upcoming interviews

### Notifications

- Retrieve user notifications
- Track unread notifications
- Manage notification state

### Dashboard

The dashboard API aggregates important career-tracking information, including:

- Total applications
- Applications by status
- Recent applications
- Upcoming interviews
- Career activity

### Analytics

The analytics module provides career-search insights through:

- Application activity
- Application status distribution
- Career journey information
- Career insights

## Technology Stack

| Technology | Purpose |
| --- | --- |
| Java | Backend programming language |
| Spring Boot | REST API framework |
| Spring Security | Authentication and authorization |
| JSON Web Token (JWT) | Stateless authentication |
| Spring Data JPA | Data access layer |
| Hibernate | Object-relational mapping |
| MySQL | Relational database |
| Maven | Dependency management |

## Architecture

The project follows a layered architecture that separates API handling, business logic, data access, and persistence responsibilities.

```text
Client
  |
  v
Controller Layer
  |
  v
Service Layer
  |
  v
Repository Layer
  |
  v
MySQL Database
```

## Authentication

Authentication is handled separately through Spring Security and the JWT authentication filter.

```text

Client
  |
  | Authentication Request
  v
Auth Controller
  |
  v
Authentication
  |
  v
JWT Token
  |
  v
Authenticated API Request
  |
  v
JWT Filter
  |
  v
Spring Security
  |
  v
Protected Controller
```

## API MODULES

| Module         | Responsibility                       |
| -------------- | ------------------------------------ |
| Authentication | Login and token-based authentication |
| Users          | User-related operations              |
| Applications   | Job application management           |
| Interviews     | Interview management                 |
| Notifications  | Notification management              |
| Analytics      | Career analytics                     |
| Dashboard      | Aggregated career-tracking data      |


## RELATED FRONTEND

Career Tracker uses a separate React frontend application for the user interface.

Frontend repository : https://github.com/Dev-Venom/career-tracker-ui
