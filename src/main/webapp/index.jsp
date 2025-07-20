<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>E-Commerce Store - Your One-Stop Shopping Destination</title>
    
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
                        <a class="nav-link active" href="${pageContext.request.contextPath}/">Home</a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
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
                        <a class="nav-link" href="${pageContext.request.contextPath}/products">All Products</a>
                    </li>
                </ul>
                
                <!-- Search Form -->
                <form class="d-flex me-3" action="${pageContext.request.contextPath}/products" method="GET">
                    <input class="form-control me-2" type="search" name="search" placeholder="Search products..." aria-label="Search">
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

    <!-- Hero Section -->
    <section class="hero-section bg-gradient-primary text-white py-5">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-lg-6">
                    <h1 class="display-4 fw-bold mb-4">Welcome to E-Store</h1>
                    <p class="lead mb-4">Discover amazing products at unbeatable prices. Shop the latest trends in electronics, fashion, books, and more!</p>
                    <div class="d-flex gap-3">
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-light btn-lg">
                            Shop Now <i class="bi bi-arrow-right ms-2"></i>
                        </a>
                        <a href="${pageContext.request.contextPath}/products?category=1" class="btn btn-outline-light btn-lg">
                            Electronics
                        </a>
                    </div>
                </div>
                <div class="col-lg-6">
                    <img src="${pageContext.request.contextPath}/images/hero-image.jpg" alt="Shopping" class="img-fluid rounded">
                </div>
            </div>
        </div>
    </section>

    <!-- Featured Categories -->
    <section class="py-5">
        <div class="container">
            <h2 class="text-center mb-5">Shop by Category</h2>
            <div class="row g-4">
                <div class="col-md-4 col-lg-2">
                    <div class="category-card text-center">
                        <div class="category-icon bg-primary text-white rounded-circle mx-auto mb-3">
                            <i class="bi bi-phone display-6"></i>
                        </div>
                        <h5>Electronics</h5>
                        <a href="${pageContext.request.contextPath}/products?category=1" class="stretched-link"></a>
                    </div>
                </div>
                <div class="col-md-4 col-lg-2">
                    <div class="category-card text-center">
                        <div class="category-icon bg-success text-white rounded-circle mx-auto mb-3">
                            <i class="bi bi-bag display-6"></i>
                        </div>
                        <h5>Clothing</h5>
                        <a href="${pageContext.request.contextPath}/products?category=2" class="stretched-link"></a>
                    </div>
                </div>
                <div class="col-md-4 col-lg-2">
                    <div class="category-card text-center">
                        <div class="category-icon bg-warning text-white rounded-circle mx-auto mb-3">
                            <i class="bi bi-book display-6"></i>
                        </div>
                        <h5>Books</h5>
                        <a href="${pageContext.request.contextPath}/products?category=3" class="stretched-link"></a>
                    </div>
                </div>
                <div class="col-md-4 col-lg-2">
                    <div class="category-card text-center">
                        <div class="category-icon bg-info text-white rounded-circle mx-auto mb-3">
                            <i class="bi bi-house display-6"></i>
                        </div>
                        <h5>Home & Garden</h5>
                        <a href="${pageContext.request.contextPath}/products?category=4" class="stretched-link"></a>
                    </div>
                </div>
                <div class="col-md-4 col-lg-2">
                    <div class="category-card text-center">
                        <div class="category-icon bg-danger text-white rounded-circle mx-auto mb-3">
                            <i class="bi bi-trophy display-6"></i>
                        </div>
                        <h5>Sports</h5>
                        <a href="${pageContext.request.contextPath}/products?category=5" class="stretched-link"></a>
                    </div>
                </div>
                <div class="col-md-4 col-lg-2">
                    <div class="category-card text-center">
                        <div class="category-icon bg-secondary text-white rounded-circle mx-auto mb-3">
                            <i class="bi bi-controller display-6"></i>
                        </div>
                        <h5>Toys & Games</h5>
                        <a href="${pageContext.request.contextPath}/products?category=6" class="stretched-link"></a>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Featured Products -->
    <section class="py-5 bg-light">
        <div class="container">
            <h2 class="text-center mb-5">Featured Products</h2>
            <div class="row g-4" id="featured-products">
                <!-- Products will be loaded here via JavaScript -->
            </div>
            <div class="text-center mt-4">
                <a href="${pageContext.request.contextPath}/products" class="btn btn-primary btn-lg">
                    View All Products
                </a>
            </div>
        </div>
    </section>

    <!-- Features Section -->
    <section class="py-5">
        <div class="container">
            <div class="row g-4">
                <div class="col-md-3 text-center">
                    <div class="feature-icon bg-primary text-white rounded-circle mx-auto mb-3">
                        <i class="bi bi-truck display-5"></i>
                    </div>
                    <h5>Free Shipping</h5>
                    <p class="text-muted">Free shipping on orders over $50</p>
                </div>
                <div class="col-md-3 text-center">
                    <div class="feature-icon bg-success text-white rounded-circle mx-auto mb-3">
                        <i class="bi bi-shield-check display-5"></i>
                    </div>
                    <h5>Secure Payment</h5>
                    <p class="text-muted">100% secure payment processing</p>
                </div>
                <div class="col-md-3 text-center">
                    <div class="feature-icon bg-warning text-white rounded-circle mx-auto mb-3">
                        <i class="bi bi-arrow-clockwise display-5"></i>
                    </div>
                    <h5>Easy Returns</h5>
                    <p class="text-muted">30-day return policy</p>
                </div>
                <div class="col-md-3 text-center">
                    <div class="feature-icon bg-info text-white rounded-circle mx-auto mb-3">
                        <i class="bi bi-headset display-5"></i>
                    </div>
                    <h5>24/7 Support</h5>
                    <p class="text-muted">Round the clock customer support</p>
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
        // Load featured products
        document.addEventListener('DOMContentLoaded', function() {
            loadFeaturedProducts();
            updateCartCount();
        });
    </script>
</body>
</html> 