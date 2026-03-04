# UniGear Tracker - Web Application

React-based web application for the UniGear Tracker System.

## Technology Stack

- React 18
- React Router DOM
- Axios for API calls
- CSS3 for styling

## Getting Started

### Prerequisites
- Node.js 16 or higher
- npm or yarn
- Backend server running on http://localhost:8080

### Installation

1. **Install Dependencies**
   ```bash
   npm install
   ```

2. **Start Development Server**
   ```bash
   npm start
   ```

3. **Application will start on**: `http://localhost:3000`

## Features

### User Registration
- Full name, email, and password input
- Form validation (client-side)
- Password confirmation
- Duplicate email prevention
- Success message and redirect to login

### User Login
- Email and password authentication
- Form validation
- Error handling for invalid credentials
- Redirect to dashboard on success
- Session management using localStorage

### Dashboard
- Welcome message with user info
- Display user details (ID, name, email)
- Logout functionality
- Protected route (requires authentication)

## Project Structure

```
web/
├── public/
│   └── index.html
├── src/
│   ├── components/
│   │   ├── Register.js       # Registration page
│   │   ├── Login.js          # Login page
│   │   ├── Dashboard.js      # Dashboard/Home page
│   │   ├── Auth.css          # Auth pages styling
│   │   └── Dashboard.css     # Dashboard styling
│   ├── services/
│   │   └── authService.js    # Authentication API calls
│   ├── App.js                # Main app with routing
│   ├── index.js              # Entry point
│   └── index.css             # Global styles
└── package.json
```

## API Integration

The application connects to the Spring Boot backend:
- Base URL: `http://localhost:8080/api/auth`
- Endpoints:
  - `POST /register` - User registration
  - `POST /login` - User login

## Available Scripts

- `npm start` - Run development server
- `npm build` - Build for production
- `npm test` - Run tests
- `npm eject` - Eject from Create React App

## Form Validation

### Registration
- Name: Required, minimum 2 characters
- Email: Required, valid email format
- Password: Required, minimum 6 characters
- Confirm Password: Must match password

### Login
- Email: Required, valid email format
- Password: Required

## Session Management

- User data stored in localStorage after successful login
- Protected routes check authentication status
- Logout clears user data from localStorage
