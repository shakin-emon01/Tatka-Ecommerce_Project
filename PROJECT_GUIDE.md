# E-Commerce Project Guide

## Overview

This is a complete e-commerce web application built with Java, MySQL, HTML, CSS, JavaScript, and Bootstrap v5.3. The project demonstrates modern web development practices and includes both customer and admin functionalities.

## Features

### Customer Features
- ✅ User registration and login
- ✅ Product catalog with categories
- ✅ Product search and filtering
- ✅ Shopping cart functionality
- ✅ Checkout process
- ✅ Order history
- ✅ User profile management

### Admin Features
- ✅ Product management (CRUD operations)
- ✅ Category management
- ✅ Order management
- ✅ User management
- ✅ Sales reports

## Technology Stack

- **Frontend**: HTML5, CSS3, JavaScript, Bootstrap v5.3
- **Backend**: Java Servlets, JSP
- **Database**: MySQL 8.0+
- **Server**: Apache Tomcat 9.0+
- **Build Tool**: Maven 3.6+
- **Java Version**: JDK 8+

## Project Structure

```
e-commerce/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/ecommerce/
│   │   │       ├── controller/     # Servlet controllers
│   │   │       ├── dao/           # Data Access Objects
│   │   │       ├── model/         # Java model classes
│   │   │       ├── service/       # Business logic
│   │   │       └── util/          # Utility classes
│   │   ├── webapp/
│   │   │   ├── WEB-INF/          # Web configuration
│   │   │   ├── css/              # Custom stylesheets
│   │   │   ├── js/               # JavaScript files
│   │   │   ├── images/           # Static images
│   │   │   └── pages/            # JSP pages
│   │   └── resources/
│   │       └── database.properties
│   └── test/                     # Unit tests
├── database/
│   ├── schema.sql               # Database schema
│   └── sample_data.sql          # Sample data
│   └── ecommerce.db             # SQLite database file
├── docs/                        # Documentation
├── pom.xml                      # Maven configuration
├── README.md                    # Project overview
├── PROJECT_GUIDE.md            # This file
└── setup.bat                   # Setup script
```

## Prerequisites

Before running this project, ensure you have the following installed:

1. **Java JDK 8 or higher**
   - Download from: https://adoptium.net/
   - Set JAVA_HOME environment variable

2. **Apache Maven 3.6 or higher**
   - Download from: https://maven.apache.org/download.cgi
   - Add to PATH environment variable

3. **MySQL 8.0 or higher**
   - Download from: https://dev.mysql.com/downloads/mysql/
   - Create a database user with appropriate permissions

4. **Apache Tomcat 9.0 or higher** (optional, Maven plugin can handle this)
   - Download from: https://tomcat.apache.org/download-90.cgi

## Installation & Setup

### Step 1: Clone/Download the Project

```bash
# If using git
git clone <repository-url>
cd e-commerce

# Or download and extract the ZIP file
```

### Step 2: Database Setup

1. **Start MySQL Server**

2. **Create Database**
   ```sql
   CREATE DATABASE ecommerce_db;
   USE ecommerce_db;
   ```

3. **Run Schema Script**
   ```bash
   mysql -u root -p ecommerce_db < database/schema.sql
   ```

4. **Load Sample Data**
   ```bash
   mysql -u root -p ecommerce_db < database/sample_data.sql
   ```

5. **Update Database Configuration**
   Edit `src/main/resources/database.properties`:
   ```properties
   db.url=jdbc:mysql://localhost:3306/ecommerce_db?useSSL=false&serverTimezone=UTC
   db.username=your_username
   db.password=your_password
   ```

### Step 3: Build the Project

```bash
# Navigate to project directory
cd e-commerce

# Clean and build
mvn clean install
```

### Step 4: Run the Application

#### Option 1: Using Maven Tomcat Plugin (Recommended)
```bash
mvn tomcat7:run
```

#### Option 2: Using External Tomcat
1. Build the WAR file: `mvn package`
2. Copy `target/ecommerce.war` to Tomcat's `webapps/` directory
3. Start Tomcat server
4. Access: `http://localhost:8080/ecommerce`

### Step 5: Access the Application

Open your web browser and navigate to:
```
http://localhost:8080/ecommerce
```

## Default Credentials

### Admin User
- **Email**: admin@ecommerce.com
- **Password**: password123
- **Role**: ADMIN

### Test Customer
- **Email**: customer@test.com
- **Password**: password123
- **Role**: CUSTOMER

## API Endpoints

### Customer APIs
- `GET /products` - Get all products
- `GET /products?category=1` - Get products by category
- `GET /products?search=keyword` - Search products
- `GET /product?id=1` - Get product details
- `POST /cart/add` - Add item to cart
- `GET /cart` - Get cart items
- `POST /order/create` - Create order

### Admin APIs
- `GET /admin/products` - Get all products (admin)
- `POST /admin/products` - Create product
- `PUT /admin/products/{id}` - Update product
- `DELETE /admin/products/{id}` - Delete product
- `GET /admin/orders` - Get all orders
- `PUT /admin/orders/{id}/status` - Update order status

## Development Guide

### Adding New Features

1. **Create Model Class** (if needed)
   - Add to `src/main/java/com/ecommerce/model/`
   - Include getters, setters, and helper methods

2. **Create DAO Class** (if needed)
   - Add to `src/main/java/com/ecommerce/dao/`
   - Implement database operations

3. **Create Servlet Controller**
   - Add to `src/main/java/com/ecommerce/controller/`
   - Handle HTTP requests and responses

4. **Create JSP Pages**
   - Add to `src/main/webapp/pages/`
   - Use Bootstrap for styling

5. **Add JavaScript** (if needed)
   - Add to `src/main/webapp/js/`
   - Handle client-side interactions

### Database Modifications

1. **Update Schema**
   - Modify `database/schema.sql`
   - Add migration scripts if needed

2. **Update Model Classes**
   - Add new fields and methods
   - Update DAO classes accordingly

3. **Test Changes**
   - Run unit tests
   - Test manually in browser

### Styling Guidelines

- Use Bootstrap v5.3 classes for layout and components
- Custom CSS in `src/main/webapp/css/style.css`
- Follow responsive design principles
- Use CSS variables for consistent theming

## Troubleshooting

### Common Issues

1. **Database Connection Error**
   - Check MySQL service is running
   - Verify database credentials in `database.properties`
   - Ensure database `ecommerce_db` exists

2. **Build Failures**
   - Check Java and Maven versions
   - Clean and rebuild: `mvn clean install`
   - Check for compilation errors

3. **404 Errors**
   - Verify servlet mappings in `web.xml`
   - Check file paths and names
   - Ensure Tomcat is running

4. **Session Issues**
   - Check session timeout in `web.xml`
   - Verify session management in servlets

### Debug Mode

To enable debug logging, add to `log4j.properties`:
```properties
log4j.rootLogger=DEBUG, stdout
log4j.logger.com.ecommerce=DEBUG
```

## Testing

### Unit Tests
```bash
mvn test
```

### Manual Testing Checklist

- [ ] User registration and login
- [ ] Product browsing and search
- [ ] Shopping cart functionality
- [ ] Checkout process
- [ ] Order management
- [ ] Admin panel access
- [ ] Product management (CRUD)
- [ ] Order status updates

## Deployment

### Production Deployment

1. **Build Production WAR**
   ```bash
   mvn clean package -P production
   ```

2. **Configure Production Database**
   - Use production MySQL instance
   - Update connection properties
   - Set appropriate security settings

3. **Deploy to Tomcat**
   - Copy WAR file to Tomcat webapps
   - Configure Tomcat for production
   - Set up SSL certificates

4. **Monitor Application**
   - Set up logging
   - Monitor performance
   - Regular backups

## Contributing

1. Follow Java coding conventions
2. Add comments for complex logic
3. Write unit tests for new features
4. Update documentation
5. Test thoroughly before submitting

## License

This project is for educational purposes. Feel free to use and modify as needed.

## Support

For issues and questions:
1. Check the troubleshooting section
2. Review the code comments
3. Check the README.md file
4. Create an issue in the repository

---

**Happy Coding! 🚀** 