# Fintech Loan Application Portal

A comprehensive loan calculator and application management system built with enterprise-grade security and scalability in mind. This project demonstrates full-stack development capabilities suitable for fintech and banking environments.

## 🏗️ Architecture Overview

- **Backend**: Spring Boot 3.x with Java 17, implementing clean architecture patterns
- **Frontend**: React 18 with TypeScript for type-safe, scalable UI development  
- **Database**: PostgreSQL with Redis caching for optimal performance
- **Security**: JWT-based authentication with Spring Security, BCrypt password hashing
- **Testing**: Comprehensive test suite with JUnit, Testcontainers, and Cypress
- **Deployment**: Docker containerization with CI/CD pipeline support

## 🚀 Features

### Public Access
- **Loan Calculator**: Interactive tool for calculating monthly payments and amortization schedules
- **Responsive Design**: Mobile-first approach with modern UI/UX

### Authenticated Users
- **Secure Registration/Login**: JWT-based authentication with session management
- **Loan Application Portal**: Multi-step form with comprehensive validation
- **Application Tracking**: Real-time status updates and history

### Admin Dashboard  
- **Application Management**: Review, approve, or reject loan applications
- **User Management**: View and manage user accounts
- **Audit Trail**: Complete logging of all system activities
- **Analytics Dashboard**: Key metrics and reporting

### Security & Compliance
- **OWASP Compliance**: Protection against XSS, SQL injection, CSRF
- **Data Encryption**: Sensitive data encryption at rest and in transit
- **Audit Logging**: Comprehensive audit trail for regulatory compliance
- **Role-Based Access**: Granular permissions and access control

## 🛠️ Quick Start

### Prerequisites
- Docker and Docker Compose
- Java 17+ (for local development)
- Node.js 18+ (for local development)

### Using Docker (Recommended)
```bash
# Clone the repository
git clone <repository-url>
cd loan-portal

# Copy environment variables
cp .env.example .env

# Start all services
docker-compose up -d

# Wait for services to be ready, then access:
# Frontend: http://localhost:3000
# Backend API: http://localhost:8080
# API Documentation: http://localhost:8080/swagger-ui.html
```

### Local Development Setup
```bash
# Backend
cd backend
./mvnw spring-boot:run

# Frontend (in another terminal)
cd frontend
npm install
npm start

# Database (PostgreSQL with Docker)
docker run -d --name postgres-loan \
  -e POSTGRES_DB=loanportal \
  -e POSTGRES_USER=admin \
  -e POSTGRES_PASSWORD=admin123 \
  -p 5432:5432 postgres:15
```

## 👥 Test Credentials

| Role | Email | Password | Capabilities |
|------|-------|----------|-------------|
| Admin | admin@loanportal.com | Admin@123 | Full system access |
| User | user@example.com | User@123 | Loan applications |

## 📊 Database Schema

### Core Tables
- **users**: User account information with encrypted passwords
- **loan_applications**: Comprehensive loan application data
- **audit_logs**: Complete system activity tracking
- **user_roles**: Role-based permission management

### Key Relationships
- Users can have multiple loan applications
- Each application change is logged in audit_logs
- Role hierarchy enforces proper access control

## 🔒 Security Implementation

### Authentication & Authorization
- **JWT Tokens**: Stateless authentication with configurable expiration
- **BCrypt Hashing**: Industry-standard password security  
- **Role-Based Access**: Granular permission system
- **Session Management**: Secure token refresh and logout

### Data Protection
- **Input Validation**: Server and client-side validation
- **SQL Injection Prevention**: Parameterized queries with JPA
- **XSS Protection**: Content Security Policy headers
- **CSRF Protection**: Spring Security CSRF tokens

## 🧪 Testing Strategy

### Backend Testing
```bash
cd backend
./mvnw test                    # Unit tests
./mvnw test -Pintegration      # Integration tests
./mvnw verify                  # All tests + code coverage
```

### Frontend Testing  
```bash
cd frontend
npm test                       # Unit tests
npm run test:e2e              # End-to-end tests
npm run test:coverage         # Coverage report
```

### Test Coverage Goals
- **Backend**: >90% line coverage
- **Frontend**: >85% component coverage
- **E2E**: Critical user journeys covered

## 📈 Performance & Scalability

### Optimization Features
- **Database Indexing**: Optimized queries for loan applications
- **Redis Caching**: Session and calculation result caching
- **Connection Pooling**: HikariCP for database connections
- **Lazy Loading**: Efficient data loading strategies

### Monitoring Ready
- **Health Checks**: Spring Actuator endpoints
- **Metrics**: Micrometer integration for monitoring
- **Logging**: Structured logging with correlation IDs

## 🚀 Deployment

### Production Deployment
```bash
# Build production images
docker-compose -f docker-compose.prod.yml build

# Deploy to production
docker-compose -f docker-compose.prod.yml up -d
```

### CI/CD Integration
The project includes GitHub Actions workflows for:
- Automated testing on pull requests
- Security scanning and dependency checks
- Docker image building and deployment
- Database migration management

## 📚 API Documentation

Interactive API documentation is available at:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI Spec**: http://localhost:8080/v3/api-docs

### Key Endpoints
- `POST /api/auth/login` - User authentication
- `POST /api/loans/calculate` - Loan calculation
- `POST /api/applications` - Submit loan application  
- `GET /api/admin/applications` - Admin application management

## 🔧 Development Workflow

### Code Quality Standards
- **Checkstyle**: Java code style enforcement
- **ESLint/Prettier**: TypeScript/React code formatting
- **SonarQube**: Code quality and security analysis
- **Conventional Commits**: Standardized commit messages

### Git Workflow
1. Feature branch from `develop`
2. Pull request with automated testing
3. Code review and approval
4. Merge to `develop`
5. Release branches for production deployment

## 🏢 Enterprise Features

### Regulatory Compliance
- **Audit Trail**: Complete activity logging for compliance
- **Data Retention**: Configurable data retention policies
- **Privacy Controls**: GDPR-compliant user data management

### Scalability Design
- **Microservices Ready**: Modular architecture for easy scaling
- **Load Balancer Compatible**: Stateless design for horizontal scaling
- **Database Sharding**: Prepared for database scaling strategies

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes  
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 About

This project demonstrates enterprise-level full-stack development skills including:
- **Clean Architecture**: Separation of concerns and SOLID principles
- **Security Best Practices**: OWASP compliance and secure coding
- **Testing Excellence**: Comprehensive test coverage and quality assurance
- **DevOps Integration**: Docker, CI/CD, and deployment automation
- **Documentation**: Thorough documentation and API specifications

Perfect for showcasing fintech and banking software engineering capabilities.