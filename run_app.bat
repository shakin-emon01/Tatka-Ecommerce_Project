@echo off
echo Starting E-Commerce Application...
cd e-commerce
mvn clean install
mvn javafx:run

REM Check if target/classes exists
if not exist "target\classes" (
    echo Compiling application...
    call mvn clean compile
    if errorlevel 1 (
        echo Compilation failed!
        pause
        exit /b 1
    )
)

REM Run the application
echo Running application...
java -cp "target/classes;target/dependency/*" --module-path "target/dependency" --add-modules javafx.controls,javafx.fxml com.ecommerce.ECommerceApp

if errorlevel 1 (
    echo Application failed to start!
    pause
)

pause 