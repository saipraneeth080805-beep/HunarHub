# PostgreSQL Setup Guide for HunarHub

## Method 1: Install PostgreSQL using Chocolatey (Easiest)

Since you already have Chocolatey installed, you can install PostgreSQL with one command:

1. **Open PowerShell as Administrator**
2. **Run the installation command:**
   ```powershell
   choco install postgresql -y
   ```
3. **After installation**, PostgreSQL will be installed and the service will start automatically
4. **Default settings:**
   - Port: 5432
   - Superuser: postgres
   - You'll be prompted to set a password during installation

## Method 2: Manual Installation

### Step 1: Download PostgreSQL

1. Visit: https://www.postgresql.org/download/windows/
2. Click "Download the installer"
3. Download PostgreSQL 15 or 16 (latest stable version)
4. Run the installer

### Step 2: Installation Wizard

1. **Choose Installation Directory**: Use default (`C:\Program Files\PostgreSQL\15`)
2. **Select Components**: 
   - ✅ PostgreSQL Server
   - ✅ pgAdmin 4 (GUI tool - recommended)
   - ✅ Command Line Tools
   - ✅ Stack Builder (optional)
3. **Data Directory**: Use default (`C:\Program Files\PostgreSQL\15\data`)
4. **Password**: Set a password for the `postgres` superuser
   - **Remember this password!** You'll need it to connect
   - Example: `postgres` (or choose a secure password)
5. **Port**: Use default `5432`
6. **Locale**: Use default (or select your locale)
7. Click "Next" and complete the installation

### Step 3: Verify Installation

1. **Open Command Prompt or PowerShell**
2. **Check if PostgreSQL is running:**
   ```powershell
   # Refresh PATH
   $env:Path = [System.Environment]::GetEnvironmentVariable("Path","Machine") + ";" + [System.Environment]::GetEnvironmentVariable("Path","User")
   
   # Check PostgreSQL version
   psql --version
   ```

3. **Or check Windows Services:**
   - Press `Win + R`, type `services.msc`
   - Look for "postgresql-x64-15" service
   - It should be "Running"

## Step 4: Create Database for HunarHub

### Option A: Using pgAdmin (GUI - Recommended)

1. **Open pgAdmin 4** (installed with PostgreSQL)
2. **Connect to Server:**
   - Right-click "Servers" → "Create" → "Server"
   - General tab:
     - Name: `Local PostgreSQL`
   - Connection tab:
     - Host: `localhost`
     - Port: `5432`
     - Username: `postgres`
     - Password: (the password you set during installation)
   - Click "Save"
3. **Create Database:**
   - Right-click "Databases" → "Create" → "Database"
   - Database name: `hunarhub`
   - Owner: `postgres`
   - Click "Save"

### Option B: Using Command Line (psql)

1. **Open Command Prompt or PowerShell**
2. **Connect to PostgreSQL:**
   ```bash
   psql -U postgres
   ```
   (Enter your password when prompted)

3. **Create the database:**
   ```sql
   CREATE DATABASE hunarhub;
   ```

4. **Verify database was created:**
   ```sql
   \l
   ```
   (You should see `hunarhub` in the list)

5. **Exit psql:**
   ```sql
   \q
   ```

### Option C: Using SQL Command Directly

```bash
psql -U postgres -c "CREATE DATABASE hunarhub;"
```

## Step 5: Update Application Configuration

Update `src/main/resources/application.properties` with your PostgreSQL credentials:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/hunarhub
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD_HERE  # Change this!
spring.datasource.driver-class-name=org.postgresql.Driver
```

**Important:** Replace `YOUR_PASSWORD_HERE` with the password you set during PostgreSQL installation.

## Step 6: Test Database Connection

1. **Start your Spring Boot application:**
   ```bash
   mvn spring-boot:run
   ```

2. **Check the console output:**
   - You should see: "Hibernate: create table..." messages
   - No connection errors
   - Application starts successfully

3. **Verify tables were created:**
   - Open pgAdmin
   - Navigate to: Servers → Local PostgreSQL → Databases → hunarhub → Schemas → public → Tables
   - You should see tables like: `users`, `entrepreneur_profiles`, `products`, `orders`, etc.

## Troubleshooting

### PostgreSQL service not running

**Start the service:**
```powershell
# Using PowerShell (as Admin)
Start-Service postgresql-x64-15

# Or using services.msc GUI
# Press Win+R → services.msc → Find PostgreSQL → Right-click → Start
```

### Connection refused error

1. **Check if PostgreSQL is running:**
   ```powershell
   Get-Service postgresql*
   ```

2. **Check if port 5432 is in use:**
   ```powershell
   netstat -an | findstr 5432
   ```

3. **Verify firewall settings** - PostgreSQL should be allowed through Windows Firewall

### Authentication failed

- Double-check your password in `application.properties`
- Try connecting with pgAdmin first to verify credentials
- Default username is `postgres`

### Database doesn't exist

- Make sure you created the `hunarhub` database
- Check database name spelling in `application.properties`

### PATH not set (psql not recognized)

After installing PostgreSQL, you may need to:
1. Close and reopen your terminal
2. Or manually add PostgreSQL bin to PATH:
   ```
   C:\Program Files\PostgreSQL\15\bin
   ```

## Quick Reference Commands

```bash
# Connect to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE hunarhub;

# List all databases
\l

# Connect to specific database
\c hunarhub

# List all tables
\dt

# Exit psql
\q

# Start PostgreSQL service (PowerShell Admin)
Start-Service postgresql-x64-15

# Stop PostgreSQL service
Stop-Service postgresql-x64-15
```

## Next Steps After Setup

1. ✅ PostgreSQL installed
2. ✅ Database `hunarhub` created
3. ✅ Updated `application.properties` with correct credentials
4. ✅ Start Spring Boot application: `mvn spring-boot:run`
5. ✅ Tables will be created automatically (thanks to `spring.jpa.hibernate.ddl-auto=update`)
6. ✅ Default categories will be created automatically (via `DataInitializer`)

Your HunarHub application is ready to use!
