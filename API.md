# API Documentation

This document provides detailed information about the LoanMe API endpoints, request/response formats, and authentication requirements.

## Authentication

The LoanMe API uses JWT (JSON Web Tokens) for authentication and authorization. All authenticated endpoints require a valid JWT token in the `Authorization` header.

### Token Format

```
Authorization: Bearer <jwt_token>
```

### Authentication Endpoints

#### POST `/api/auth/login`

Authenticate a user and receive access and refresh tokens.

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "User@123"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4...",
  "tokenType": "Bearer",
  "expiresIn": 86400
}
```

#### POST `/api/auth/register`

Register a new user account.

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "SecurePassword123",
  "phoneNumber": "+1234567890",
  "dateOfBirth": "1990-01-01"
}
```

**Response:**
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "+1234567890",
  "dateOfBirth": "1990-01-01",
  "accountStatus": "ACTIVE",
  "emailVerified": false,
  "createdAt": "2023-01-01T00:00:00Z"
}
```

#### POST `/api/auth/refresh`

Refresh an expired access token using a refresh token.

**Request Body:**
```json
{
  "refreshToken": "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4..."
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400
}
```

## Loan Calculator

### POST `/api/loans/calculate`

Calculate loan payment details based on loan amount, interest rate, and term.

**Request Body:**
```json
{
  "loanAmount": 10000,
  "interestRate": 5.5,
  "loanTermMonths": 60
}
```

**Response:**
```json
{
  "loanAmount": 10000,
  "interestRate": 5.5,
  "loanTermMonths": 60,
  "monthlyPayment": 191.16,
  "totalInterest": 1469.6,
  "totalAmount": 11469.6,
  "amortizationSchedule": [
    {
      "month": 1,
      "payment": 191.16,
      "principal": 145.33,
      "interest": 45.83,
      "balance": 9854.67
    }
    // ... additional months
  ]
}
```

## Loan Applications

### POST `/api/applications`

Submit a new loan application.

**Request Body:**
```json
{
  "loanAmount": 15000,
  "loanTermMonths": 36,
  "loanType": "PERSONAL",
  "loanPurpose": "DEBT_CONSOLIDATION",
  "annualIncome": 50000,
  "monthlyExpenses": 2000,
  "creditScore": 720,
  "employmentStatus": "EMPLOYED_FULL_TIME",
  "employmentYears": 3,
  "employerName": "Tech Corp",
  "existingDebt": 5000,
  "hasCollateral": false,
  "additionalNotes": "Looking to consolidate credit card debt"
}
```

**Response:**
```json
{
  "id": 1,
  "loanAmount": 15000,
  "loanTermMonths": 36,
  "loanType": "PERSONAL",
  "loanPurpose": "DEBT_CONSOLIDATION",
  "annualIncome": 50000,
  "monthlyExpenses": 2000,
  "creditScore": 720,
  "employmentStatus": "EMPLOYED_FULL_TIME",
  "employmentYears": 3,
  "employerName": "Tech Corp",
  "existingDebt": 5000,
  "hasCollateral": false,
  "status": "SUBMITTED",
  "monthlyPayment": 452.5,
  "totalInterest": 1290,
  "totalAmount": 16290,
  "debtToIncomeRatio": 0.4,
  "riskLevel": "MEDIUM",
  "submittedAt": "2023-01-01T00:00:00Z",
  "createdAt": "2023-01-01T00:00:00Z"
}
```

### GET `/api/applications`

Retrieve all loan applications for the authenticated user.

**Response:**
```json
[
  {
    "id": 1,
    "loanAmount": 15000,
    "loanTermMonths": 36,
    "loanType": "PERSONAL",
    "loanPurpose": "DEBT_CONSOLIDATION",
    "status": "APPROVED",
    "monthlyPayment": 452.5,
    "submittedAt": "2023-01-01T00:00:00Z",
    "approvedAt": "2023-01-05T00:00:00Z"
  }
]
```

### GET `/api/applications/{id}`

Retrieve a specific loan application by ID.

**Response:**
```json
{
  "id": 1,
  "loanAmount": 15000,
  "loanTermMonths": 36,
  "loanType": "PERSONAL",
  "loanPurpose": "DEBT_CONSOLIDATION",
  "annualIncome": 50000,
  "monthlyExpenses": 2000,
  "creditScore": 720,
  "employmentStatus": "EMPLOYED_FULL_TIME",
  "employmentYears": 3,
  "employerName": "Tech Corp",
  "existingDebt": 5000,
  "hasCollateral": false,
  "status": "APPROVED",
  "monthlyPayment": 452.5,
  "totalInterest": 1290,
  "totalAmount": 16290,
  "debtToIncomeRatio": 0.4,
  "riskLevel": "MEDIUM",
  "submittedAt": "2023-01-01T00:00:00Z",
  "approvedAt": "2023-01-05T00:00:00Z",
  "reviewNotes": "Good credit history and stable employment",
  "createdAt": "2023-01-01T00:00:00Z",
  "updatedAt": "2023-01-05T00:00:00Z"
}
```

## Admin Endpoints

### GET `/api/admin/applications`

Retrieve all loan applications for admin users with filtering and pagination support.

**Query Parameters:**
- `status`: Filter by application status (e.g., SUBMITTED, APPROVED, REJECTED)
- `loanType`: Filter by loan type (e.g., PERSONAL, AUTO, MORTGAGE)
- `page`: Page number (default: 0)
- `size`: Page size (default: 20)

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "userId": 10,
      "applicantName": "John Doe",
      "applicantEmail": "john.doe@example.com",
      "loanAmount": 15000,
      "loanType": "PERSONAL",
      "status": "SUBMITTED",
      "riskLevel": "MEDIUM",
      "submittedAt": "2023-01-01T00:00:00Z",
      "daysPending": 5,
      "assignedReviewer": null
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalElements": 1,
  "totalPages": 1
}
```

### PUT `/api/admin/applications/{id}/status`

Update the status of a loan application.

**Request Body:**
```json
{
  "status": "APPROVED",
  "reviewNotes": "Applicant meets all requirements"
}
```

**Response:**
```json
{
  "id": 1,
  "loanAmount": 15000,
  "loanTermMonths": 36,
  "loanType": "PERSONAL",
  "loanPurpose": "DEBT_CONSOLIDATION",
  "status": "APPROVED",
  "reviewNotes": "Applicant meets all requirements",
  "approvedAt": "2023-01-05T00:00:00Z",
  "updatedAt": "2023-01-05T00:00:00Z"
}
```

### GET `/api/admin/users`

Retrieve all users for admin management.

**Query Parameters:**
- `accountStatus`: Filter by account status (e.g., ACTIVE, INACTIVE)
- `page`: Page number (default: 0)
- `size`: Page size (default: 20)

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@example.com",
      "phoneNumber": "+1234567890",
      "accountStatus": "ACTIVE",
      "emailVerified": true,
      "lastLogin": "2023-01-05T00:00:00Z",
      "roles": ["USER"],
      "totalApplications": 2,
      "approvedApplications": 1,
      "createdAt": "2023-01-01T00:00:00Z"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalElements": 1,
  "totalPages": 1
}
```

## Error Responses

All API endpoints follow a consistent error response format:

```json
{
  "timestamp": "2023-01-01T00:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed for argument",
  "path": "/api/applications"
}
```

### Common HTTP Status Codes

- `200 OK`: Successful GET, PUT, PATCH requests
- `201 Created`: Successful POST requests
- `204 No Content`: Successful DELETE requests
- `400 Bad Request`: Invalid request data
- `401 Unauthorized`: Missing or invalid authentication
- `403 Forbidden`: Insufficient permissions
- `404 Not Found`: Resource not found
- `409 Conflict`: Resource conflict
- `500 Internal Server Error`: Server-side errors

## Rate Limiting

The API implements rate limiting to prevent abuse:
- 100 requests per minute for authenticated users
- 10 requests per minute for unauthenticated users

Exceeding these limits will result in a `429 Too Many Requests` response.