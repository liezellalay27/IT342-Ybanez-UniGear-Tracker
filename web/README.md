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
- (Optional) Unsplash API key for equipment image gallery

### Installation

1. **Install Dependencies**
   ```bash
   npm install
   ```

2. **Configure Environment Variables** (Optional)
   - Edit `.env` file in the `web/` directory
   - Add your Unsplash API key for equipment images:
     ```env
     REACT_APP_API_BASE_URL=http://localhost:8080/api
     REACT_APP_UNSPLASH_ACCESS_KEY=your_unsplash_key_here
     ```
   - To get a free Unsplash API key:
     1. Visit https://unsplash.com/developers
     2. Sign up for a free account
     3. Create an application
     4. Copy your Access Key and paste it in `.env`

3. **Start Development Server**
   ```bash
   npm start
   ```

4. **Application will start on**: `http://localhost:3000`

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

### Equipment Catalog
- Browse and search available equipment
- Filter by category
- View detailed equipment information
- External API integration for equipment images (Unsplash)
- Image gallery with navigation controls

### Equipment Details
- Detailed equipment specifications
- Availability status
- Location information
- **Equipment Image Gallery** (External API: Unsplash)
  - Fetches real equipment images from Unsplash API
  - Interactive gallery with navigation
  - Photo credits linked to photographer profiles
  - Gracefully falls back to placeholder if API is unavailable
- **Item Availability Calendar**
  - Visual calendar showing equipment availability by date
  - Click dates to select borrow period
  - Highlights unavailable (already borrowed) dates
  - Shows selected date range duration
  - Pre-fills dates when creating new request

### Equipment Requests
- Create borrowing requests with date ranges
- Upload event approval documents (PDF)
- Track request status (Pending, Approved, Rejected, Completed)
- View and delete pending requests
- Download uploaded documents

### User Dashboard
- View profile information
- Edit profile details and picture
- Track active and completed requests
- Request history

### Admin Dashboard
- **Overview Tab**: KPIs for pending requests, active loans, low stock, overdue items
- **Equipment Management**: Add and manage equipment inventory
- **User Management**: View all users and their roles
- **Borrowed Equipment**: Track active loans and process returns
- **Request Management**: Review and approve/reject equipment requests

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
