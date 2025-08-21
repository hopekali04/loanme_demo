# Setup Guide

This guide provides instructions for setting up the database and running the LoanMe application.

## Database Setup

1.  **Create a `application.properties` file:**

    Create a `application.properties` file in `src/main/resources/` with the following content:

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/loanme
    spring.datasource.username=your_database_username
    spring.datasource.password=your_database_password
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
    ```

    You can use the `application.properties.example` as a template.

2.  **Database Schema:**

    The database schema will be automatically created or updated by Spring Boot based on the entities defined in the application. You can also use the `schema.sql` file to manually create the database schema.

## Running the Application

To build and run the application, use the following command:

```bash
./mvnw spring-boot:run
```
