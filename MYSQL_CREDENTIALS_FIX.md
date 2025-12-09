# ⚠️ MySQL Authentication Error - FIX REQUIRED

## Current Error

```
Access denied for user 'root'@'localhost' (using password: YES)
```

The backend is trying to connect with `root:root` credentials, but MySQL is rejecting them.

---

## Solution: Update Your MySQL Password in application.yml

### Step 1: Find Your Actual MySQL Root Password

**Option A: Check Existing Workbench Connection**

1. Open **MySQL Workbench 8.0 CE**
2. Look at **Home → MySQL Connections**
3. Click on your local connection and check what password is saved
4. Note down the password (it may be empty string `""`)

**Option B: Default MySQL 8.0 Passwords to Try**
Common defaults when MySQL 8.0 is first installed:

- `root` (empty password)
- `root:password`
- `root:root`
- `root:mysql`

### Step 2: Update Backend Configuration

Edit this file: `backend/src/main/resources/application.yml`

Change line from:

```yaml
password: root
```

To your actual MySQL password. Examples:

```yaml
# If your password is "mysql"
    password: mysql

# If your password is empty
    password: ""

# If your password is "mypassword123"
    password: mypassword123
```

### Step 3: Rebuild and Restart

```bash
cd backend
mvn clean install -DskipTests=true
mvn -DskipTests=true spring-boot:run
```

---

## Alternative: Reset MySQL Root Password (If Forgotten)

**On Windows:**

1. Stop MySQL Service:

   - Open **Services** (services.msc)
   - Find **MySQL80** → Right-click → **Stop**

2. Open **Command Prompt as Administrator** and navigate to MySQL bin directory:

   ```cmd
   cd "C:\Program Files\MySQL\MySQL Server 8.0\bin"
   ```

3. Start MySQL without password validation:

   ```cmd
   mysqld --skip-grant-tables
   ```

4. In a new **Command Prompt**, connect to MySQL:

   ```cmd
   mysql -u root
   ```

5. Set a new password:

   ```sql
   FLUSH PRIVILEGES;
   ALTER USER 'root'@'localhost' IDENTIFIED BY 'newpassword';
   EXIT;
   ```

6. Restart MySQL Service:

   - Open **Services** (services.msc)
   - Find **MySQL80** → Right-click → **Start**

7. Update `application.yml` with your new password:
   ```yaml
   password: newpassword
   ```

---

## Current Backend Configuration

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/expense_tracker?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: root # ← UPDATE THIS with your actual password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.MySQLDialect
```

### Key Settings:

- **allowPublicKeyRetrieval=true**: Fixes MySQL 8.0 SHA-2 authentication
- **ddl-auto: update**: Auto-creates database schema
- **MySQLDialect**: Modern Hibernate dialect for MySQL 8.0

---

## Testing Connection

Once you update the password:

1. Rebuild: `mvn clean install -DskipTests=true`
2. Start backend: `mvn -DskipTests=true spring-boot:run`
3. Look for:
   ```
   ✅ Tomcat initialized with port 8080
   ✅ HikariPool-1 - Starting...
   ✅ ExpenseTrackerApplication - Started
   ```

If you see `ERROR` or `Access denied`, double-check your password in `application.yml`.

---

## Next Steps

Once backend starts successfully:

1. Create database (if needed):

   ```sql
   CREATE DATABASE IF NOT EXISTS expense_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. Start frontend:

   ```bash
   cd frontend
   npm install
   npm start
   ```

3. Access application at: `http://localhost:3000`
