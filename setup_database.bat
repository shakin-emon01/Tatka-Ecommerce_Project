@echo off
echo Setting up E-Commerce Database...

echo.
echo Step 1: Creating database schema...
mysql -u root -p < database/schema.sql

echo.
echo Step 2: Loading sample data...
mysql -u root -p java_ecommerce < database/sample_data.sql

echo.
echo Step 3: Verifying data...
mysql -u root -p java_ecommerce -e "SELECT COUNT(*) as user_count FROM users;"
mysql -u root -p java_ecommerce -e "SELECT COUNT(*) as product_count FROM products;"
mysql -u root -p java_ecommerce -e "SELECT COUNT(*) as category_count FROM categories;"

echo.
echo Database setup completed!
echo.
echo Demo credentials:
echo Email: customer@test.com
echo Password: password123
echo.
echo Email: admin@ecommerce.com  
echo Password: password123
echo.
pause 