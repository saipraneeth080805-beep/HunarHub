# Verify HunarHub Database

## Quick Verification Commands

Since PostgreSQL requires password authentication, here are the ways to verify your database:

### Method 1: Using psql Command Line

Open PowerShell and run:

```powershell
& "C:\Program Files\PostgreSQL\18\bin\psql.exe" -U postgres
```

When prompted, enter your PostgreSQL password, then run:

```sql
\l
```

Look for `hunarhub` in the list of databases.

Or directly check:
```sql
SELECT datname FROM pg_database WHERE datname = 'hunarhub';
```

If the database exists, you'll see:
```
 datname  
----------
 hunarhub
```

### Method 2: Using pgAdmin (GUI - Easiest)

1. **Open pgAdmin 4** from Start Menu
2. **Connect to your PostgreSQL server** (enter password if prompted)
3. **Expand "Databases"** in the left sidebar
4. **Look for `hunarhub`** - it should be listed there

### Method 3: Test Connection via Spring Boot

The best way to verify is to start your Spring Boot application:

```bash
mvn spring-boot:run
```

**If the database exists and connection is correct:**
- Application will start successfully
- You'll see Hibernate creating tables in the console
- No connection errors

**If there are issues:**
- Connection refused → Database doesn't exist or wrong name
- Authentication failed → Wrong password in application.properties
- Database doesn't exist → Create it first

### Method 4: Quick SQL Check

If you know your PostgreSQL password, you can run:

```powershell
$env:PGPASSWORD = "your_password_here"
& "C:\Program Files\PostgreSQL\18\bin\psql.exe" -U postgres -c "SELECT datname FROM pg_database WHERE datname = 'hunarhub';"
```

Replace `your_password_here` with your actual PostgreSQL password.

## Expected Result

If the database exists, you should see:
```
 datname  
----------
 hunarhub
(1 row)
```

## If Database Doesn't Exist

Create it using:

```sql
CREATE DATABASE hunarhub;
```

Or via command line:
```powershell
& "C:\Program Files\PostgreSQL\18\bin\psql.exe" -U postgres -c "CREATE DATABASE hunarhub;"
```

## Next Steps After Verification

1. ✅ Database `hunarhub` exists
2. ✅ Update `application.properties` with correct password
3. ✅ Start Spring Boot: `mvn spring-boot:run`
4. ✅ Tables will be created automatically
5. ✅ Default categories will be initialized
