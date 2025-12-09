# MySQL Workbench 8.0 CE Setup Instructions

## 1. Verify MySQL Workbench 8.0 CE is Running

- Ensure MySQL Server is running on `localhost:3306`
- Default credentials configured in `backend/src/main/resources/application.yml`:
  - **Username**: `root`
  - **Password**: `Rubenyo@0306`
  - **Host**: `localhost`
  - **Port**: `3306`

## 2. Create the Database

### Option A: Using MySQL Workbench GUI

1. Open MySQL Workbench 8.0 CE
2. Connect to your local server using root credentials
3. Execute the SQL script below in a new query tab

### Option B: Using MySQL Command Line

```bash
mysql -h localhost -u root -proot -e "CREATE DATABASE IF NOT EXISTS expense_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
```

### SQL Script to Run

```sql
CREATE DATABASE IF NOT EXISTS expense_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE expense_tracker;

-- Tables will be created automatically by Hibernate (ddl-auto: update)
-- Verify with: SHOW TABLES;
```

## 3. Backend Configuration

The backend is already configured in `backend/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/expense_tracker?useSSL=false&serverTimezone=UTC
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: root
  jpa:
    hibernate:
      ddl-auto: update # Auto-creates tables on startup
```

## 4. Start the Backend

From the backend directory:

```bash
mvn -DskipTests=true spring-boot:run
```

The backend will:

- Connect to MySQL at `localhost:3306/expense_tracker`
- Auto-create necessary tables via Hibernate
- Listen on `http://localhost:8080`

## 5. Start the Frontend

From the frontend directory:

```bash
npm install
npm start
```

The frontend will run on `http://localhost:3000`

## 6. Verify Connection

- Backend logs should show: `HikariPool-1 - Starting... Connected to database`
- Frontend should load without API errors
- Navigate to `http://localhost:3000` to access the application

## Troubleshooting

### Backend fails to connect to MySQL

1. Verify MySQL Server is running (check Task Manager or services)
2. Verify credentials in `application.yml` match your MySQL setup
3. Ensure port 3306 is not blocked by firewall

### "Access denied for user 'root'@'localhost'"

- Update username/password in `application.yml` to match your MySQL installation
- Default MySQL Workbench 8.0 CE often uses `root:root` or `root:password`

### "Unknown database 'expense_tracker'"

- Create the database using the SQL script above
- Ensure Hibernate's `ddl-auto: update` setting persists
