# How to Change PostgreSQL Password

## Method 1: Using psql Command Line (Recommended)

### Step 1: Connect to PostgreSQL
Open PowerShell or Command Prompt and run:
```bash
psql -U postgres
```
(Enter your current password when prompted)

### Step 2: Change Password
Once connected, run:
```sql
ALTER USER postgres WITH PASSWORD 'your_new_password';
```
Replace `your_new_password` with your desired password.

### Step 3: Exit psql
```sql
\q
```

### Step 4: Update application.properties
Update the password in `src/main/resources/application.properties`:
```properties
spring.datasource.password=your_new_password
```

## Method 2: Using pgAdmin (GUI)

1. **Open pgAdmin 4**
2. **Connect to your PostgreSQL server** (if not already connected)
3. **Right-click on "Login/Group Roles"** → **"Create"** → **"Login/Group Role"**
   - Or find existing `postgres` user and right-click → **"Properties"**
4. **Go to "Definition" tab**
5. **Enter new password** in the "Password" field
6. **Click "Save"**

## Method 3: Using ALTER USER Command (Without Interactive Prompt)

If you want to change password without interactive login:

```bash
psql -U postgres -c "ALTER USER postgres WITH PASSWORD 'your_new_password';"
```

**Note:** This method may require you to set the `PGPASSWORD` environment variable first:
```powershell
$env:PGPASSWORD = "current_password"
psql -U postgres -c "ALTER USER postgres WITH PASSWORD 'new_password';"
```

## Method 4: Reset Password if You Forgot It

If you forgot your PostgreSQL password:

### Windows (Using Services)

1. **Stop PostgreSQL service:**
   ```powershell
   Stop-Service postgresql-x64-15
   ```
   (Replace `15` with your PostgreSQL version number)

2. **Edit pg_hba.conf file:**
   - Location: `C:\Program Files\PostgreSQL\15\data\pg_hba.conf`
   - Find line starting with: `host all all 127.0.0.1/32 scram-sha-256`
   - Change `scram-sha-256` to `trust`
   - Save the file

3. **Start PostgreSQL service:**
   ```powershell
   Start-Service postgresql-x64-15
   ```

4. **Connect without password:**
   ```bash
   psql -U postgres
   ```

5. **Change password:**
   ```sql
   ALTER USER postgres WITH PASSWORD 'new_password';
   ```

6. **Revert pg_hba.conf:**
   - Change `trust` back to `scram-sha-256`
   - Restart PostgreSQL service

## Verify Password Change

After changing the password, test the connection:

```bash
psql -U postgres
```
(Enter your new password)

If it connects successfully, your password has been changed!

## Update HunarHub Configuration

Don't forget to update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/hunarhub
spring.datasource.username=postgres
spring.datasource.password=YOUR_NEW_PASSWORD_HERE
spring.datasource.driver-class-name=org.postgresql.Driver
```

## Security Tips

1. **Use a strong password:**
   - At least 8 characters
   - Mix of uppercase, lowercase, numbers, and special characters
   - Avoid common words

2. **Don't commit passwords to Git:**
   - Consider using environment variables or Spring profiles
   - Add `application.properties` to `.gitignore` if it contains sensitive data

3. **Use environment variables (Optional):**
   ```properties
   spring.datasource.password=${DB_PASSWORD}
   ```
   Then set: `$env:DB_PASSWORD = "your_password"`

## Quick Reference

```bash
# Connect to PostgreSQL
psql -U postgres

# Change password (inside psql)
ALTER USER postgres WITH PASSWORD 'new_password';

# Exit psql
\q

# Change password (one-liner)
psql -U postgres -c "ALTER USER postgres WITH PASSWORD 'new_password';"
```
