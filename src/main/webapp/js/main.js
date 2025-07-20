// Main JavaScript for E-Commerce Store

// Global variables
let cart = JSON.parse(localStorage.getItem('cart')) || [];
let currentUser = null;

// Initialize the application
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
});

function initializeApp() {
    updateCartCount();
    setupEventListeners();
    loadUserData();
}

// Setup event listeners
function setupEventListeners() {
    // Search form
    const searchForm = document.querySelector('form[action*="products"]');
    if (searchForm) {
        searchForm.addEventListener('submit', handleSearch);
    }

    // Add to cart buttons
    document.addEventListener('click', function(e) {
        if (e.target.classList.contains('add-to-cart')) {
            e.preventDefault();
            const productId = e.target.dataset.productId;
            const productName = e.target.dataset.productName;
            const productPrice = e.target.dataset.productPrice;
            const productImage = e.target.dataset.productImage;
            addToCart(productId, productName, productPrice, productImage);
        }
    });

    // Quantity change buttons
    document.addEventListener('click', function(e) {
        if (e.target.classList.contains('quantity-btn')) {
            e.preventDefault();
            const action = e.target.dataset.action;
            const productId = e.target.dataset.productId;
            updateQuantity(action, productId);
        }
    });

    // Remove from cart buttons
    document.addEventListener('click', function(e) {
        if (e.target.classList.contains('remove-from-cart')) {
            e.preventDefault();
            const productId = e.target.dataset.productId;
            removeFromCart(productId);
        }
    });
}

// Cart Management
function addToCart(productId, productName, productPrice, productImage) {
    const existingItem = cart.find(item => item.id === productId);
    
    if (existingItem) {
        existingItem.quantity += 1;
    } else {
        cart.push({
            id: productId,
            name: productName,
            price: parseFloat(productPrice),
            image: productImage,
            quantity: 1
        });
    }
    
    saveCart();
    updateCartCount();
    showNotification('Product added to cart!', 'success');
}

function removeFromCart(productId) {
    cart = cart.filter(item => item.id !== productId);
    saveCart();
    updateCartCount();
    updateCartDisplay();
    showNotification('Product removed from cart!', 'info');
}

function updateQuantity(action, productId) {
    const item = cart.find(item => item.id === productId);
    
    if (item) {
        if (action === 'increase') {
            item.quantity += 1;
        } else if (action === 'decrease') {
            item.quantity -= 1;
            if (item.quantity <= 0) {
                removeFromCart(productId);
                return;
            }
        }
        
        saveCart();
        updateCartCount();
        updateCartDisplay();
    }
}

function saveCart() {
    localStorage.setItem('cart', JSON.stringify(cart));
}

function updateCartCount() {
    const cartCount = document.getElementById('cart-count');
    if (cartCount) {
        const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);
        cartCount.textContent = totalItems;
        
        if (totalItems > 0) {
            cartCount.classList.add('cart-badge');
        } else {
            cartCount.classList.remove('cart-badge');
        }
    }
}

function updateCartDisplay() {
    const cartContainer = document.getElementById('cart-items');
    if (!cartContainer) return;
    
    if (cart.length === 0) {
        cartContainer.innerHTML = '<div class="text-center py-5"><i class="bi bi-cart3 display-1 text-muted"></i><p class="mt-3">Your cart is empty</p></div>';
        return;
    }
    
    let cartHTML = '';
    let total = 0;
    
    cart.forEach(item => {
        const itemTotal = item.price * item.quantity;
        total += itemTotal;
        
        cartHTML += `
            <div class="cart-item">
                <div class="row align-items-center">
                    <div class="col-md-2">
                        <img src="${item.image}" alt="${item.name}" class="cart-item-image">
                    </div>
                    <div class="col-md-4">
                        <h6 class="mb-0">${item.name}</h6>
                        <small class="text-muted">$${item.price.toFixed(2)}</small>
                    </div>
                    <div class="col-md-3">
                        <div class="input-group input-group-sm">
                            <button class="btn btn-outline-secondary quantity-btn" data-action="decrease" data-product-id="${item.id}">-</button>
                            <input type="text" class="form-control text-center" value="${item.quantity}" readonly>
                            <button class="btn btn-outline-secondary quantity-btn" data-action="increase" data-product-id="${item.id}">+</button>
                        </div>
                    </div>
                    <div class="col-md-2">
                        <span class="fw-bold">$${itemTotal.toFixed(2)}</span>
                    </div>
                    <div class="col-md-1">
                        <button class="btn btn-sm btn-outline-danger remove-from-cart" data-product-id="${item.id}">
                            <i class="bi bi-trash"></i>
                        </button>
                    </div>
                </div>
            </div>
        `;
    });
    
    cartHTML += `
        <div class="border-top pt-3">
            <div class="d-flex justify-content-between align-items-center">
                <h5 class="mb-0">Total:</h5>
                <h5 class="mb-0 text-primary">$${total.toFixed(2)}</h5>
            </div>
            <div class="mt-3">
                <a href="checkout" class="btn btn-primary w-100">Proceed to Checkout</a>
            </div>
        </div>
    `;
    
    cartContainer.innerHTML = cartHTML;
}

// Product Loading
function loadFeaturedProducts() {
    const container = document.getElementById('featured-products');
    if (!container) return;
    
    // Show loading spinner
    container.innerHTML = '<div class="col-12 text-center"><div class="spinner-border text-primary" role="status"></div></div>';
    
    // Fetch featured products from API
    fetch('api/products/featured')
        .then(response => response.json())
        .then(products => {
            displayProducts(products, container);
        })
        .catch(error => {
            console.error('Error loading featured products:', error);
            container.innerHTML = '<div class="col-12 text-center text-muted">Failed to load products</div>';
        });
}

function displayProducts(products, container) {
    if (!products || products.length === 0) {
        container.innerHTML = '<div class="col-12 text-center text-muted">No products found</div>';
        return;
    }
    
    const productsHTML = products.map(product => `
        <div class="col-md-6 col-lg-3">
            <div class="card product-card h-100">
                <img src="${product.imageUrl || '/images/placeholder.jpg'}" class="card-img-top" alt="${product.name}">
                <div class="card-body d-flex flex-column">
                    <h6 class="card-title product-title">${product.name}</h6>
                    <p class="card-text product-description">${product.shortDescription || product.description}</p>
                    <div class="mt-auto">
                        <div class="d-flex justify-content-between align-items-center mb-2">
                            <span class="product-price">$${product.price}</span>
                            <span class="badge ${product.stockQuantity > 0 ? 'bg-success' : 'bg-danger'}">
                                ${product.stockQuantity > 0 ? 'In Stock' : 'Out of Stock'}
                            </span>
                        </div>
                        <div class="d-grid gap-2">
                            <a href="product?id=${product.id}" class="btn btn-outline-primary btn-sm">View Details</a>
                            ${product.stockQuantity > 0 ? 
                                `<button class="btn btn-primary btn-sm add-to-cart" 
                                    data-product-id="${product.id}" 
                                    data-product-name="${product.name}" 
                                    data-product-price="${product.price}" 
                                    data-product-image="${product.imageUrl || '/images/placeholder.jpg'}">
                                    Add to Cart
                                </button>` : 
                                '<button class="btn btn-secondary btn-sm" disabled>Out of Stock</button>'
                            }
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `).join('');
    
    container.innerHTML = productsHTML;
}

// Search functionality
function handleSearch(e) {
    const searchInput = e.target.querySelector('input[name="search"]');
    const searchTerm = searchInput.value.trim();
    
    if (searchTerm.length < 2) {
        e.preventDefault();
        showNotification('Please enter at least 2 characters to search', 'warning');
        return false;
    }
}

// User Management
function loadUserData() {
    const userData = localStorage.getItem('user');
    if (userData) {
        currentUser = JSON.parse(userData);
        updateUserInterface();
    }
}

function updateUserInterface() {
    if (currentUser) {
        // Update user-specific elements
        const userElements = document.querySelectorAll('[data-user-name]');
        userElements.forEach(element => {
            element.textContent = currentUser.firstName;
        });
    }
}

// Notifications
function showNotification(message, type = 'info') {
    // Create notification element
    const notification = document.createElement('div');
    notification.className = `alert alert-${type} alert-dismissible fade show position-fixed`;
    notification.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
    notification.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    // Add to page
    document.body.appendChild(notification);
    
    // Auto remove after 5 seconds
    setTimeout(() => {
        if (notification.parentNode) {
            notification.remove();
        }
    }, 5000);
}

// Form validation
function validateForm(formId) {
    const form = document.getElementById(formId);
    if (!form) return true;
    
    const inputs = form.querySelectorAll('input[required], select[required], textarea[required]');
    let isValid = true;
    
    inputs.forEach(input => {
        if (!input.value.trim()) {
            input.classList.add('is-invalid');
            isValid = false;
        } else {
            input.classList.remove('is-invalid');
        }
    });
    
    // Email validation
    const emailInputs = form.querySelectorAll('input[type="email"]');
    emailInputs.forEach(input => {
        if (input.value && !isValidEmail(input.value)) {
            input.classList.add('is-invalid');
            isValid = false;
        }
    });
    
    return isValid;
}

function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

// Utility functions
function formatPrice(price) {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD'
    }).format(price);
}

function formatDate(dateString) {
    return new Date(dateString).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });
}

function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// API helper functions
async function apiCall(url, options = {}) {
    try {
        const response = await fetch(url, {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        return await response.json();
    } catch (error) {
        console.error('API call failed:', error);
        throw error;
    }
}

// Smooth scrolling for anchor links
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
        const target = document.querySelector(this.getAttribute('href'));
        if (target) {
            target.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        }
    });
});

// Lazy loading for images
function setupLazyLoading() {
    const images = document.querySelectorAll('img[data-src]');
    const imageObserver = new IntersectionObserver((entries, observer) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const img = entry.target;
                img.src = img.dataset.src;
                img.classList.remove('lazy');
                imageObserver.unobserve(img);
            }
        });
    });
    
    images.forEach(img => imageObserver.observe(img));
}

// Initialize lazy loading
document.addEventListener('DOMContentLoaded', setupLazyLoading);

// Export functions for use in other scripts
window.ECommerceApp = {
    addToCart,
    removeFromCart,
    updateQuantity,
    updateCartCount,
    showNotification,
    validateForm,
    formatPrice,
    formatDate,
    apiCall
}; 