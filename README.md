# E-Commerce JavaFX Application

A comprehensive desktop e-commerce application built with JavaFX and SQLite, featuring product management, category management, user authentication, and shopping cart functionality.

## Features

### 🛍️ **Product Management**
- **Add/Edit/Delete Products**: Full CRUD operations for products
- **Image Upload**: Browse and upload product images with preview
- **Category Assignment**: Assign products to categories
- **Stock Management**: Track product inventory
- **Search & Filter**: Search products by name and filter by category
- **Bulk Operations**: Manage multiple products efficiently

### 📂 **Category Management**
- **Add/Edit/Delete Categories**: Complete category management
- **Image Upload**: Upload category images with preview
- **Product Count**: View number of products in each category
- **Hierarchical Organization**: Organize products by categories

### 👤 **User Management**
- **User Registration**: New user signup with validation
- **User Authentication**: Secure login with role-based access
- **Admin Dashboard**: Comprehensive admin interface
- **Customer Dashboard**: User-friendly shopping interface
- **Profile Management**: Update user information

### 🛒 **Shopping Features**
- **Shopping Cart**: Add/remove items, update quantities
- **Order Management**: Place orders and track status
- **Order History**: View past orders and details
- **Search Products**: Find products quickly

### 🎨 **Modern UI**
- **Responsive Design**: Works on different screen sizes
- **Material Design**: Clean, modern interface
- **Image Previews**: Visual feedback for uploads
- **Form Validation**: Real-time input validation
- **Status Messages**: Clear feedback for all operations

## Technology Stack

- **Frontend**: JavaFX 17+
- **Database**: SQLite 3
- **Language**: Java 21
- **Build Tool**: Maven
- **UI Framework**: FXML with CSS styling

## Installation & Setup

### Prerequisites
- Java 21 or higher
- Maven 3.6+
- Git

### Quick Start

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd e-commerce
   ```

2. **Build the project**
   ```bash
   mvn clean compile
   ```

3. **Run the application**
   ```bash
   mvn javafx:run
   ```

### Database Setup

The application uses SQLite and will automatically create the database on first run. Sample data is included for testing.

**Demo Accounts:**
- **Admin**: `admin@ecommerce.com` / `password123`
             `abida@email.com` / `password123`
             `emon@email.com` / `password123`
- **Customer**: `customer@test.com` / `password123`

## Usage Guide

### Admin Dashboard

#### Product Management
1. **Add New Product**
   - Click "Add New Product" button
   - Fill in product details (name, description, price, stock)
   - Select a category from dropdown
   - Click "Browse" to upload product image
   - Preview image before saving
   - Click "Save" to create product

2. **Edit Product**
   - Click "Edit" button on any product row
   - Modify product information
   - Update image if needed
   - Save changes

3. **Delete Product**
   - Click "Delete" button on product row
   - Confirm deletion in dialog
   - Product will be permanently removed

4. **Search & Filter**
   - Use search box to find products by name
   - Use category filter to show products by category
   - Click "Clear" to reset filters

#### Category Management
1. **Add New Category**
   - Click "Add New Category" button
   - Enter category name and description
   - Upload category image (optional)
   - Save category

2. **Edit Category**
   - Click "Edit" on category row
   - Update category information
   - Save changes

3. **Delete Category**
   - Click "Delete" on category row
   - Confirm deletion (affects all products in category)
   - Category and associated products will be removed

### Customer Dashboard

#### Shopping
1. **Browse Products**
   - View all products in grid layout
   - Filter by category using dropdown
   - Search products by name

2. **Add to Cart**
   - Click "Add to Cart" on any product
   - View cart contents in "Shopping Cart" tab
   - Update quantities or remove items

3. **Checkout**
   - Review cart items and total
   - Click "Proceed to Checkout"
   - Complete order process

#### Order Management
1. **View Orders**
   - Go to "My Orders" tab
   - See order history with status
   - Click "View Details" for order information

2. **Profile Management**
   - Update personal information
   - Change password
   - Manage shipping addresses

## File Structure

```
e-commerce/
├── src/main/java/com/ecommerce/
│   ├── controller/          # JavaFX Controllers
│   │   ├── AdminDashboardController.java
│   │   ├── CustomerDashboardController.java
│   │   ├── ProductDialogController.java
│   │   ├── CategoryDialogController.java
│   │   └── ...
│   ├── dao/                # Data Access Objects
│   │   ├── ProductDAO.java
│   │   ├── CategoryDAO.java
│   │   ├── UserDAO.java
│   │   └── ...
│   ├── model/              # Data Models
│   │   ├── Product.java
│   │   ├── Category.java
│   │   ├── User.java
│   │   └── ...
│   └── util/               # Utility Classes
│       ├── DatabaseUtil.java
│       └── ...
├── src/main/resources/
│   ├── fxml/               # FXML Layout Files
│   │   ├── AdminDashboard.fxml
│   │   ├── CustomerDashboard.fxml
│   │   ├── ProductDialog.fxml
│   │   ├── CategoryDialog.fxml
│   │   └── ...
│   ├── css/                # Stylesheets
│   │   └── styles.css
│   └── sql/                # Database Scripts
│       ├── schema.sql
│       └── sample_data.sql
├── database/               # Database Files
│   ├── ecommerce.db
│   ├── DatabaseInitializer.java
│   └── DatabaseInitializer.class
└── pom.xml                 # Maven Configuration
``