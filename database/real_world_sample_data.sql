-- RESET ORDERS, ORDER_ITEMS, AND USERS
DELETE FROM order_items;
DELETE FROM orders;
DELETE FROM users;

-- Insert 12 customer users (IDs 4-15)
INSERT INTO users (email, password, first_name, last_name, phone, role) VALUES
('customer4@example.com', 'password', 'Customer', 'Four', '1000000004', 'CUSTOMER'),
('customer5@example.com', 'password', 'Customer', 'Five', '1000000005', 'CUSTOMER'),
('customer6@example.com', 'password', 'Customer', 'Six', '1000000006', 'CUSTOMER'),
('customer7@example.com', 'password', 'Customer', 'Seven', '1000000007', 'CUSTOMER'),
('customer8@example.com', 'password', 'Customer', 'Eight', '1000000008', 'CUSTOMER'),
('customer9@example.com', 'password', 'Customer', 'Nine', '1000000009', 'CUSTOMER'),
('customer10@example.com', 'password', 'Customer', 'Ten', '1000000010', 'CUSTOMER'),
('customer11@example.com', 'password', 'Customer', 'Eleven', '1000000011', 'CUSTOMER'),
('customer12@example.com', 'password', 'Customer', 'Twelve', '1000000012', 'CUSTOMER'),
('customer13@example.com', 'password', 'Customer', 'Thirteen', '1000000013', 'CUSTOMER'),
('customer14@example.com', 'password', 'Customer', 'Fourteen', '1000000014', 'CUSTOMER'),
('customer15@example.com', 'password', 'Customer', 'Fifteen', '1000000015', 'CUSTOMER');

-- Now insert orders using only user IDs 4-15 (update all order blocks below accordingly)
-- (You may need to update the order blocks below to use only these user IDs)

-- Insert at least one product for each empty category
-- (This will be filled after checking which categories are empty) 