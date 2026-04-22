# Maven Installation Guide for Windows

## Quick Install (Chocolatey - Requires Admin)

1. Open PowerShell as Administrator
2. Run: `choco install maven -y`
3. Close and reopen your terminal
4. Verify: `mvn --version`

## Manual Installation

### Step 1: Download Maven
- Visit: https://maven.apache.org/download.cgi
- Download: `apache-maven-3.9.6-bin.zip` (or latest version)

### Step 2: Extract
- Extract to: `C:\Program Files\Apache\maven`
- Or any location you prefer (e.g., `C:\maven`)

### Step 3: Set Environment Variables

**Option A: Using GUI**
1. Press `Win + R`, type `sysdm.cpl`, press Enter
2. Go to "Advanced" tab → "Environment Variables"
3. Under "System variables", click "New":
   - Variable name: `MAVEN_HOME`
   - Variable value: `C:\Program Files\Apache\maven` (your Maven path)
4. Edit "Path" variable → Add: `%MAVEN_HOME%\bin`
5. Click OK on all dialogs

**Option B: Using PowerShell (Admin)**
```powershell
[System.Environment]::SetEnvironmentVariable("MAVEN_HOME", "C:\Program Files\Apache\maven", "Machine")
$path = [System.Environment]::GetEnvironmentVariable("Path", "Machine")
[System.Environment]::SetEnvironmentVariable("Path", "$path;%MAVEN_HOME%\bin", "Machine")
```

### Step 4: Verify Installation
1. Close and reopen your terminal
2. Run: `mvn --version`
3. You should see Maven version information

## Using IDE (Easiest Option)

### IntelliJ IDEA
- File → Open → Select your project folder
- IntelliJ will automatically detect `pom.xml` and download dependencies
- No manual Maven installation needed!

### Eclipse
- File → Import → Existing Maven Projects
- Eclipse will handle Maven automatically

## Troubleshooting

**"mvn is not recognized"**
- Make sure you closed and reopened your terminal after installation
- Verify PATH includes `%MAVEN_HOME%\bin`
- Check `MAVEN_HOME` is set correctly

**Java Not Found**
- Maven requires Java JDK
- Install Java 17 or higher
- Set `JAVA_HOME` environment variable

## Verify Java Installation
```powershell
java -version
javac -version
```

If these commands don't work, install Java JDK first.
