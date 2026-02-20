# Setup Guide

This guide provides instructions for setting up the database and running the LoanMe application with both the Spring Boot backend and SvelteKit frontend.

## Prerequisites

Before setting up the application, ensure you have the following installed:
- Java 17 or higher
- Node.js 18 or higher
- Docker and Docker Compose
- Maven 3.8 or higher
- PostgreSQL client (psql)

## Database Setup

### Option 1: Using Docker (Recommended)

The easiest way to set up the database is using Docker Compose, which will automatically create and configure PostgreSQL with the correct schema:

```bash
# Start the database service
docker-compose up -d postgres

# The database will be automatically initialized with the schema from schema.sql
# Connection details:
# Host: localhost
# Port: 5432
# Database: loanportal
# Username: admin
# Password: admin123
```

### Option 2: Manual Database Setup

1.  **Install PostgreSQL:**
    
    Install PostgreSQL 15+ on your system using your preferred method (package manager, official installer, etc.).

2.  **Create Database and User:**
    
    ```sql
    CREATE DATABASE loanportal;
    CREATE USER admin WITH PASSWORD 'admin123';
    GRANT ALL PRIVILEGES ON DATABASE loanportal TO admin;
    ```

3.  **Initialize Schema:**
    
    Run the schema.sql file to create tables and initial data:
    
    ```bash
    psql -U admin -d loanportal -f schema.sql
    ```

## Backend Setup

1.  **Environment Configuration:**
    
    Create a `application.properties` file in `src/main/resources/` with the following content:
    
    ```properties
    # Database Configuration
    spring.datasource.url=jdbc:postgresql://localhost:5432/loanportal
    spring.datasource.username=admin
    spring.datasource.password=admin123
    spring.jpa.hibernate.ddl-auto=validate
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
    
    # Redis Configuration
    spring.redis.host=localhost
    spring.redis.port=6379
    spring.redis.password=redis123
    
    # JWT Configuration
    app.jwt.secret=mySecretKey123456789012345678901234567890
    app.jwt.expiration=86400
    app.jwt.refresh-expiration=604800
    
    # CORS Configuration
    app.cors.allowed-origins=http://localhost:3000,http://localhost:5173
    
    # File Upload Configuration
    spring.servlet.multipart.max-file-size=10MB
    spring.servlet.multipart.max-request-size=10MB
    
    # Logging Configuration
    logging.level.com.fintech.loanportal=DEBUG
    ```

2.  **Running the Backend:**
    
    You can run the backend application in several ways:
    
    ```bash
    # Using Maven plugin
    ./mvnw spring-boot:run
    
    # Or build and run the JAR file
    ./mvnw clean package
    java -jar target/loanportal-0.0.1-SNAPSHOT.jar
    ```

## Frontend Setup (SvelteKit)

1.  **Navigate to the frontend directory:**
    
    ```bash
    cd frontend/loanme-ui
    ```

2.  **Install Dependencies:**
    
    ```bash
    npm install
    ```

3.  **Environment Configuration:**
    
    Create a `.env` file in the `frontend/loanme-ui` directory:
    
    ```env
    VITE_API_BASE_URL=http://localhost:8080/api
    VITE_APP_TITLE=LoanMe
    ```

4.  **Running the Development Server:**
    
    ```bash
    npm run dev
    
    # The application will be available at http://localhost:5173
    ```

5.  **Building for Production:**
    
    ```bash
    npm run build
    
    # Preview the production build
    npm run preview
    ```

## Running the Complete Application

### Option 1: Using Docker Compose (Recommended)

The simplest way to run the entire application is using Docker Compose:

```bash
# Copy the example environment file
cp .env.example .env

# Start all services (database, backend, frontend)
docker-compose up -d

# Access the application:
# Frontend: http://localhost:3000
# Backend API: http://localhost:8080
# API Documentation: http://localhost:8080/swagger-ui.html
```

### Option 2: Running Services Locally

1.  **Start the database and Redis:**
    
    ```bash
    docker-compose up -d postgres redis
    ```

2.  **Start the backend:**
    
    ```bash
    ./mvnw spring-boot:run
    ```

3.  **Start the frontend:**
    
    ```bash
    cd frontend/loanme-ui
    npm run dev
    ```

4.  **Access the application:**
    - Frontend: http://localhost:5173
    - Backend API: http://localhost:8080
    - API Documentation: http://localhost:8080/swagger-ui.html

## Test Credentials

After setting up the application, you can use the following test credentials:

| Role | Email | Password | Capabilities |
|------|-------|----------|-------------|
| Admin | admin@loanportal.com | Admin@123 | Full system access |
| User | user@example.com | User@123 | Loan applications |

These users are created automatically when you initialize the database with the schema.sql file.

## Troubleshooting

### Common Issues

1.  **Port Conflicts:**
    If you encounter port conflicts, you can modify the ports in `docker-compose.yml` or stop the conflicting services.

2.  **Database Connection Issues:**
    Ensure the database is running and the connection details in `application.properties` are correct.

3.  **Frontend Not Connecting to Backend:**
    Check that the `VITE_API_BASE_URL` in the frontend `.env` file matches the backend URL.

4.  **Permission Issues:**
    Ensure the database user has the necessary permissions to create tables and insert data.

### Logs and Debugging

- Backend logs can be found in the console where you started the application or in `backend/logs/` if running in Docker
- Frontend logs are available in the terminal where you ran `npm run dev`
- Database logs can be accessed with: `docker logs loan_portal_db`

For more detailed information about the application architecture and API, refer to the [Architecture.md](Architecture.md) and [API.md](API.md) documentation files.