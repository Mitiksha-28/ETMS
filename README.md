# Event Ticket Management System

A Java-based desktop application for managing events, tickets, and payments.

## Prerequisites

- Java Development Kit (JDK) 11 or higher
- Maven
- MySQL Database
- Visual Studio Code with Java extensions

## Setup Instructions

1. Clone the repository
2. Open the project in VS Code
3. Install required VS Code extensions:

   - Extension Pack for Java
   - Spring Boot Extension Pack (optional)

4. Configure the database:

   - Create a MySQL database named `etms_db`
   - Update the database connection details in `src/main/java/com/etms/util/DatabaseUtil.java`

5. Build the project:

   ```bash
   mvn clean install
   ```

6. Run the application:
   ```bash
   mvn exec:java -Dexec.mainClass="com.etms.ETMSApplication"
   ```

## Features

- User Registration and Authentication
- Event Management
- Ticket Booking
- Payment Processing
- Sponsor Management
- Admin Dashboard
- Customer Dashboard

## Project Structure

```
src/main/java/com/etms/
├── controller/      # Business logic controllers
├── dao/            # Data Access Object interfaces
├── daoimpl/        # DAO implementations
├── exception/      # Custom exceptions
├── model/          # Entity classes
├── ui/             # User interface components
└── util/           # Utility classes
```

## Running in VS Code

1. Open the project in VS Code
2. Wait for the Java project to be loaded and indexed
3. Open `src/main/java/com/etms/ETMSApplication.java`
4. Click the "Run" button above the main method
5. Alternatively, use the Run and Debug view (Ctrl+Shift+D) to run the application

## Troubleshooting

If you encounter any issues:

1. Make sure all dependencies are properly installed
2. Verify the database connection settings
3. Check if the Java extension pack is properly loaded in VS Code
4. Ensure Maven is properly configured in your system
