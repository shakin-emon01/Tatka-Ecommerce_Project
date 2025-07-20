<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Products - E-Commerce Store</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary sticky-top">
        <div class="container">
            <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/">
                <i class="bi bi-shop me-2"></i>E-Store
            </a>
            
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/">Home</a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle active" href="#" role="button" data-bs-toggle="dropdown">
                            Categories
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/products?category=1">Electronics</a></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/products?category=2">Clothing</a></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/products?category=3">Books</a></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/products?category=4">Home & Garden</a></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/products?category=5">Sports & Outdoors</a></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/products?category=6">Toys & Games</a></li>
                        </ul>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="${pageContext.request.contextPath}/products">All Products</a>
                    </li>
                </ul>
                
                <!-- Search Form -->
                <form class="d-flex me-3" action="${pageContext.request.contextPath}/products" method="GET">
                    <input class="form-control me-2" type="search" name="search" placeholder="Search products..." 
                           value="${searchTerm}" aria-label="Search">
                    <button class="btn btn-outline-light" type="submit">
                        <i class="bi bi-search"></i>
                    </button>
                </form>
                
                <!-- User Menu -->
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/cart">
                            <i class="bi bi-cart3"></i> Cart
                            <span class="badge bg-danger ms-1" id="cart-count">0</span>
                        </a>
                    </li>
                    
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                                    <i class="bi bi-person-circle"></i> ${sessionScope.user.firstName}
                                </a>
                                <ul class="dropdown-menu">
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/profile">My Profile</a></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/orders">My Orders</a></li>
                                    <c:if test="${sessionScope.user.admin}">
                                        <li><hr class="dropdown-divider"></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin">Admin Panel</a></li>
                                    </c:if>
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout">Logout</a></li>
                                </ul>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/login">Login</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/register">Register</a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Page Header -->
    <section class="bg-light py-4">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-md-6">
                    <h1 class="mb-0">
                        <c:choose>
                            <c:when test="${not empty searchTerm}">
                                Search Results for "${searchTerm}"
                            </c:when>
                            <c:when test="${not empty categoryId}">
                                <c:choose>
                                    <c:when test="${categoryId == '1'}">Electronics</c:when>
                                    <c:when test="${categoryId == '2'}">Clothing</c:when>
                                    <c:when test="${categoryId == '3'}">Books</c:when>
                                    <c:when test="${categoryId == '4'}">Home & Garden</c:when>
                                    <c:when test="${categoryId == '5'}">Sports & Outdoors</c:when>
                                    <c:when test="${categoryId == '6'}">Toys & Games</c:when>
                                    <c:otherwise>Products</c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                All Products
                            </c:otherwise>
                        </c:choose>
                    </h1>
                    <p class="text-muted mb-0">
                        <c:choose>
                            <c:when test="${not empty products}">
                                ${products.size()} product<c:if test="${products.size() != 1}">s</c:if> found
                            </c:when>
                            <c:otherwise>
                                No products found
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>
                <div class="col-md-6 text-md-end">
                    <nav aria-label="breadcrumb">
                        <ol class="breadcrumb justify-content-md-end mb-0">
                            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Home</a></li>
                            <li class="breadcrumb-item active">Products</li>
                        </ol>
                    </nav>
                </div>
            </div>
        </div>
    </section>

    <!-- Products Section -->
    <section class="py-5">
        <div class="container">
            <div class="row">
                <!-- Filters Sidebar -->
                <div class="col-lg-3 mb-4">
                    <div class="card">
                        <div class="card-header">
                            <h5 class="mb-0"><i class="bi bi-funnel me-2"></i>Filters</h5>
                        </div>
                        <div class="card-body">
                            <h6 class="mb-3">Categories</h6>
                            <div class="list-group list-group-flush">
                                <a href="${pageContext.request.contextPath}/products" 
                                   class="list-group-item list-group-item-action ${empty categoryId ? 'active' : ''}">
                                    All Categories
                                </a>
                                <a href="${pageContext.request.contextPath}/products?category=1" 
                                   class="list-group-item list-group-item-action ${categoryId == '1' ? 'active' : ''}">
                                    Electronics
                                </a>
                                <a href="${pageContext.request.contextPath}/products?category=2" 
                                   class="list-group-item list-group-item-action ${categoryId == '2' ? 'active' : ''}">
                                    Clothing
                                </a>
                                <a href="${pageContext.request.contextPath}/products?category=3" 
                                   class="list-group-item list-group-item-action ${categoryId == '3' ? 'active' : ''}">
                                    Books
                                </a>
                                <a href="${pageContext.request.contextPath}/products?category=4" 
                                   class="list-group-item list-group-item-action ${categoryId == '4' ? 'active' : ''}">
                                    Home & Garden
                                </a>
                                <a href="${pageContext.request.contextPath}/products?category=5" 
                                   class="list-group-item list-group-item-action ${categoryId == '5' ? 'active' : ''}">
                                    Sports & Outdoors
                                </a>
                                <a href="${pageContext.request.contextPath}/products?category=6" 
                                   class="list-group-item list-group-item-action ${categoryId == '6' ? 'active' : ''}">
                                    Toys & Games
                                </a>
                            </div>
                            
                            <hr>
                            
                            <h6 class="mb-3">Price Range</h6>
                            <div class="mb-3">
                                <label for="minPrice" class="form-label">Min Price</label>
                                <input type="number" class="form-control" id="minPrice" placeholder="0">
                            </div>
                            <div class="mb-3">
                                <label for="maxPrice" class="form-label">Max Price</label>
                                <input type="number" class="form-control" id="maxPrice" placeholder="1000">
                            </div>
                            <button type="button" class="btn btn-primary btn-sm w-100" onclick="applyPriceFilter()">
                                Apply Filter
                            </button>
                            
                            <hr>
                            
                            <h6 class="mb-3">Availability</h6>
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" id="inStock" checked>
                                <label class="form-check-label" for="inStock">
                                    In Stock Only
                                </label>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Products Grid -->
                <div class="col-lg-9">
                    <!-- Sort Options -->
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <div class="d-flex align-items-center">
                            <label for="sortSelect" class="form-label me-2 mb-0">Sort by:</label>
                            <select class="form-select form-select-sm" id="sortSelect" style="width: auto;">
                                <option value="name">Name A-Z</option>
                                <option value="name-desc">Name Z-A</option>
                                <option value="price">Price Low to High</option>
                                <option value="price-desc">Price High to Low</option>
                                <option value="newest">Newest First</option>
                            </select>
                        </div>
                        <div class="d-flex align-items-center">
                            <button class="btn btn-outline-secondary btn-sm me-2" id="gridView">
                                <i class="bi bi-grid-3x3-gap"></i>
                            </button>
                            <button class="btn btn-outline-secondary btn-sm" id="listView">
                                <i class="bi bi-list"></i>
                            </button>
                        </div>
                    </div>
                    
                    <!-- Products -->
                    <div class="row g-4" id="products-container">
                        <c:choose>
                            <c:when test="${not empty products}">
                                <c:forEach var="product" items="${products}">
                                    <div class="col-md-6 col-lg-4">
                                        <div class="card product-card h-100">
                                            <img src="${product.imageUrl != null ? product.imageUrl : '/images/placeholder.jpg'}" 
                                                 class="card-img-top" alt="${product.name}">
                                            <div class="card-body d-flex flex-column">
                                                <h6 class="card-title product-title">${product.name}</h6>
                                                <p class="card-text product-description">${product.shortDescription}</p>
                                                <div class="mt-auto">
                                                    <div class="d-flex justify-content-between align-items-center mb-2">
                                                        <span class="product-price">$${product.price}</span>
                                                        <span class="badge ${product.stockQuantity > 0 ? 'bg-success' : 'bg-danger'}">
                                                            ${product.stockStatus}
                                                        </span>
                                                    </div>
                                                    <div class="d-grid gap-2">
                                                        <a href="${pageContext.request.contextPath}/product?id=${product.id}" 
                                                           class="btn btn-outline-primary btn-sm">View Details</a>
                                                        <c:choose>
                                                            <c:when test="${product.stockQuantity > 0}">
                                                                <button class="btn btn-primary btn-sm add-to-cart" 
                                                                        data-product-id="${product.id}" 
                                                                        data-product-name="${product.name}" 
                                                                        data-product-price="${product.price}" 
                                                                        data-product-image="${product.imageUrl != null ? product.imageUrl : '/images/placeholder.jpg'}">
                                                                    Add to Cart
                                                                </button>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <button class="btn btn-secondary btn-sm" disabled>Out of Stock</button>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="col-12 text-center py-5">
                                    <i class="bi bi-search display-1 text-muted"></i>
                                    <h4 class="mt-3">No products found</h4>
                                    <p class="text-muted">
                                        <c:choose>
                                            <c:when test="${not empty searchTerm}">
                                                No products match your search criteria.
                                            </c:when>
                                            <c:otherwise>
                                                No products available in this category.
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">
                                        View All Products
                                    </a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    
                    <!-- Pagination -->
                    <c:if test="${not empty products && products.size() > 12}">
                        <nav aria-label="Products pagination" class="mt-5">
                            <ul class="pagination justify-content-center">
                                <li class="page-item disabled">
                                    <a class="page-link" href="#" tabindex="-1">Previous</a>
                                </li>
                                <li class="page-item active"><a class="page-link" href="#">1</a></li>
                                <li class="page-item"><a class="page-link" href="#">2</a></li>
                                <li class="page-item"><a class="page-link" href="#">3</a></li>
                                <li class="page-item">
                                    <a class="page-link" href="#">Next</a>
                                </li>
                            </ul>
                        </nav>
                    </c:if>
                </div>
            </div>
        </div>
    </section>

    <!-- Footer -->
    <footer class="bg-dark text-white py-5">
        <div class="container">
            <div class="row g-4">
                <div class="col-lg-4">
                    <h5 class="mb-3">E-Store</h5>
                    <p class="text-muted">Your one-stop destination for all your shopping needs. Quality products, competitive prices, and excellent service.</p>
                    <div class="social-links">
                        <a href="#" class="text-white me-3"><i class="bi bi-facebook"></i></a>
                        <a href="#" class="text-white me-3"><i class="bi bi-twitter"></i></a>
                        <a href="#" class="text-white me-3"><i class="bi bi-instagram"></i></a>
                        <a href="#" class="text-white"><i class="bi bi-linkedin"></i></a>
                    </div>
                </div>
                <div class="col-lg-2">
                    <h6 class="mb-3">Quick Links</h6>
                    <ul class="list-unstyled">
                        <li><a href="${pageContext.request.contextPath}/products" class="text-muted text-decoration-none">Products</a></li>
                        <li><a href="${pageContext.request.contextPath}/about" class="text-muted text-decoration-none">About Us</a></li>
                        <li><a href="${pageContext.request.contextPath}/contact" class="text-muted text-decoration-none">Contact</a></li>
                        <li><a href="${pageContext.request.contextPath}/faq" class="text-muted text-decoration-none">FAQ</a></li>
                    </ul>
                </div>
                <div class="col-lg-2">
                    <h6 class="mb-3">Categories</h6>
                    <ul class="list-unstyled">
                        <li><a href="${pageContext.request.contextPath}/products?category=1" class="text-muted text-decoration-none">Electronics</a></li>
                        <li><a href="${pageContext.request.contextPath}/products?category=2" class="text-muted text-decoration-none">Clothing</a></li>
                        <li><a href="${pageContext.request.contextPath}/products?category=3" class="text-muted text-decoration-none">Books</a></li>
                        <li><a href="${pageContext.request.contextPath}/products?category=4" class="text-muted text-decoration-none">Home & Garden</a></li>
                    </ul>
                </div>
                <div class="col-lg-4">
                    <h6 class="mb-3">Newsletter</h6>
                    <p class="text-muted">Subscribe to get special offers and updates</p>
                    <form class="d-flex">
                        <input type="email" class="form-control me-2" placeholder="Your email">
                        <button type="submit" class="btn btn-primary">Subscribe</button>
                    </form>
                </div>
            </div>
            <hr class="my-4">
            <div class="row align-items-center">
                <div class="col-md-6">
                    <p class="mb-0 text-muted">&copy; 2024 E-Store. All rights reserved.</p>
                </div>
                <div class="col-md-6 text-md-end">
                    <img src="${pageContext.request.contextPath}/images/payment-methods.png" alt="Payment Methods" class="img-fluid" style="max-height: 30px;">
                </div>
            </div>
        </div>
    </footer>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Custom JS -->
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
    <script>
        // Initialize page
        document.addEventListener('DOMContentLoaded', function() {
            updateCartCount();
            setupProductFilters();
        });
        
        function setupProductFilters() {
            // Sort functionality
            document.getElementById('sortSelect').addEventListener('change', function() {
                // Implement sorting logic
                console.log('Sort by:', this.value);
            });
            
            // View toggle
            document.getElementById('gridView').addEventListener('click', function() {
                document.getElementById('products-container').className = 'row g-4';
                this.classList.add('active');
                document.getElementById('listView').classList.remove('active');
            });
            
            document.getElementById('listView').addEventListener('click', function() {
                document.getElementById('products-container').className = 'row g-4 list-view';
                this.classList.add('active');
                document.getElementById('gridView').classList.remove('active');
            });
        }
        
        function applyPriceFilter() {
            const minPrice = document.getElementById('minPrice').value;
            const maxPrice = document.getElementById('maxPrice').value;
            const inStock = document.getElementById('inStock').checked;
            
            // Implement price filtering logic
            console.log('Price filter:', { minPrice, maxPrice, inStock });
        }
    </script>
</body>
</html> 