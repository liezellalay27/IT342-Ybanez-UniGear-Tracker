# UniGear Tracker

UniGear Tracker is a multi-platform project for equipment borrowing and request tracking, built with:

- Spring Boot backend API
- React web app
- Android mobile app (Kotlin)

This README reflects the current implementation status in this repository.

## Current Progress

### Backend (Spring Boot)

Implemented:

- User registration and login
- JWT token generation and JWT request filter
- Google OAuth2 login flow
- Mobile OAuth2 redirect support through /api/auth/mobile/google
- Profile APIs (view and update)
- Equipment request APIs (create, list, get by id, delete pending request)
- Spring Security route protection and stateless sessions
- Supabase/PostgreSQL persistence using Spring Data JPA

Main API groups:

- /api/auth
- /api/profile
- /api/requests

### Web (React)

Implemented:

- Route-based app shell with protected routes
- Register and login pages
- Google OAuth2 callback handling
- Catalog page with search and category filtering (currently sample data)
- Equipment details page
- My Requests page (create, list, delete request)
- Profile page (view/edit name and profile picture)

### Mobile (Android, Kotlin)

Implemented:

- Splash screen
- Email/password login
- Registration flow
- Google OAuth2 mobile flow via custom URI scheme
- OAuth2 callback activity and token persistence
- Home screen with searchable, filterable equipment catalog UI (currently sample data)

## Current Scope vs Next Scope

Done now:

- Authentication foundation across backend, web, and mobile
- User profile management
- User-side request lifecycle (create/list/view/delete pending)

In progress / next:

- Replace sample catalog data with backend-driven inventory
- Add admin workflows (approve/reject/complete requests)
- Add role-based authorization and audit/reporting features

## Tech Stack

- Java 19, Spring Boot 3.4.x, Spring Security, Spring Data JPA
- PostgreSQL (Supabase)
- React 18, React Router, Axios
- Android (Kotlin, SDK 35, min SDK 24)

## Project Structure

```text
IT342_UniGear-Tracker_G4_Ybanez/
|- backend/   Spring Boot API
|- web/       React frontend
|- mobile/    Android app (Kotlin)
|- docs/      Project documentation
```

## Quick Start

### 1) Prerequisites

- Java 19+
- Maven 3.9+
- Node.js 18+ and npm
- Android Studio (for mobile)
- Supabase/PostgreSQL credentials
- Google OAuth client credentials (for OAuth login)

### 2) Backend Setup

Create backend/.env (or set equivalent environment variables) with:

```env
DB_URL=jdbc:postgresql://<host>:6543/postgres
DB_USERNAME=<db-username>
DB_PASSWORD=<db-password>

JWT_SECRET=<your-long-secret>
JWT_EXPIRATION=86400000

GOOGLE_CLIENT_ID=<google-client-id>
GOOGLE_CLIENT_SECRET=<google-client-secret>
OAUTH2_REDIRECT_URI=http://localhost:3000
```

Run backend:

```bash
cd backend
mvn spring-boot:run
```

Backend default URL: http://localhost:8080

### 3) Web Setup

Optional web env file:

```env
REACT_APP_API_BASE_URL=http://localhost:8080/api
```

Run web app:

```bash
cd web
npm install
npm start
```

Web default URL: http://localhost:3000

### 4) Mobile Setup

- Open mobile in Android Studio
- Use Android emulator when testing locally
- Backend base URL in mobile is set to http://10.0.2.2:8080 (emulator -> host machine)
- Build and run app module

## API Snapshot

Authentication:

- POST /api/auth/register
- POST /api/auth/login
- GET /api/auth/mobile/google?redirect_uri=unigear://auth

Profile:

- GET /api/profile
- PUT /api/profile

Requests:

- POST /api/requests
- GET /api/requests
- GET /api/requests/{id}
- DELETE /api/requests/{id}

All non-public APIs require Authorization: Bearer <jwt-token>.

## Notes

- The catalog UI in both web and mobile currently uses sample equipment data.
- The request feature is already connected to backend persistence.
- Security and OAuth flow are implemented, with platform-specific redirect handling.
