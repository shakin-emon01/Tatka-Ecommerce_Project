@echo off
echo ========================================
echo E-Commerce JavaFX Application Setup
echo ========================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 11 or higher
    pause
    exit /b 1
)

REM Check if Maven is installed
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven 3.6 or higher
    pause
    exit /b 1
)

echo Java and Maven are installed.
echo.

REM Check if MySQL is running (optional check)
echo Checking MySQL connection...
mysql --version >nul 2>&1
if %errorlevel% neq 0 (
    echo WARNING: MySQL client not found in PATH
    echo Please ensure MySQL server is running
    echo.
) else (
    echo MySQL client found.
    echo.
)

echo ========================================
echo Setup Options:
echo ========================================
echo 1. Clean and compile project
echo 2. Run application
echo 3. Build JAR file
echo 4. Run tests
echo 5. Setup database (requires MySQL)
echo 6. Exit
echo.

set /p choice="Enter your choice (1-6): "

if "%choice%"=="1" goto compile
if "%choice%"=="2" goto run
if "%choice%"=="3" goto build
if "%choice%"=="4" goto test
if "%choice%"=="5" goto database
if "%choice%"=="6" goto exit
goto invalid

:compile
echo.
echo Cleaning and compiling project...
mvn clean compile
if %errorlevel% neq 0 (
    echo ERROR: Compilation failed
    pause
    exit /b 1
)
echo Compilation successful!
pause
goto menu

:run
echo.
echo Running application...
mvn javafx:run
if %errorlevel% neq 0 (
    echo ERROR: Failed to run application
    pause
    exit /b 1
)
goto menu

:build
echo.
echo Building JAR file...
mvn clean package
if %errorlevel% neq 0 (
    echo ERROR: Build failed
    pause
    exit /b 1
)
echo.
echo JAR file created successfully!
echo Location: target/ecommerce-javafx.jar
echo.
echo To run the JAR file:
echo java -jar target/ecommerce-javafx.jar
echo.
pause
goto menu

:test
echo.
echo Running tests...
mvn test
if %errorlevel% neq 0 (
    echo ERROR: Tests failed
    pause
    exit /b 1
)
echo Tests completed successfully!
pause
goto menu

:database
echo.
echo ========================================
echo Database Setup
echo ========================================
echo.
echo This will help you set up the database.
echo Make sure MySQL server is running.
echo.
set /p db_name="Enter database name (default: ecommerce): "
if "%db_name%"=="" set db_name=ecommerce

set /p db_user="Enter MySQL username: "
set /p db_pass="Enter MySQL password: "

echo.
echo Creating database...
mysql -u %db_user% -p%db_pass% -e "CREATE DATABASE IF NOT EXISTS %db_name%;"
if %errorlevel% neq 0 (
    echo ERROR: Failed to create database
    pause
    exit /b 1
)

echo.
echo Running schema script...
mysql -u %db_user% -p%db_pass% %db_name% < database/schema.sql
if %errorlevel% neq 0 (
    echo ERROR: Failed to run schema script
    pause
    exit /b 1
)

echo.
echo Inserting sample data...
mysql -u %db_user% -p%db_pass% %db_name% < database/sample_data.sql
if %errorlevel% neq 0 (
    echo ERROR: Failed to insert sample data
    pause
    exit /b 1
)

echo.
echo Database setup completed successfully!
echo.
echo Please update the database connection settings in:
echo src/main/java/com/ecommerce/util/DatabaseUtil.java
echo.
pause
goto menu

:invalid
echo.
echo Invalid choice. Please enter a number between 1 and 6.
pause
goto menu

:menu
cls
echo ========================================
echo E-Commerce JavaFX Application Setup
echo ========================================
echo.
echo Setup Options:
echo 1. Clean and compile project
echo 2. Run application
echo 3. Build JAR file
echo 4. Run tests
echo 5. Setup database (requires MySQL)
echo 6. Exit
echo.
set /p choice="Enter your choice (1-6): "

if "%choice%"=="1" goto compile
if "%choice%"=="2" goto run
if "%choice%"=="3" goto build
if "%choice%"=="4" goto test
if "%choice%"=="5" goto database
if "%choice%"=="6" goto exit
goto invalid

:exit
echo.
echo Thank you for using E-Commerce JavaFX Application!
echo.
pause
exit /b 0 