-- Legacy Database Schema and Sample Data
-- OUTDATED: Using raw SQL for data initialization instead of Flyway/Liquibase

INSERT INTO categories (id, name, description, created_date) VALUES
(1, 'Electronics', 'Electronic devices and accessories', CURRENT_TIMESTAMP()),
(2, 'Clothing', 'Apparel and fashion items', CURRENT_TIMESTAMP()),
(3, 'Food', 'Food and beverages', CURRENT_TIMESTAMP()),
(4, 'Office', 'Office supplies and equipment', CURRENT_TIMESTAMP());

INSERT INTO products (id, product_name, description, sku, price, quantity, category, supplier, created_date, is_active, warehouse_location, category_id) VALUES
(1, 'Laptop Pro 15', '15-inch professional laptop', 'SKU-LP15-001', 1299.99, 50, 'Electronics', 'TechCorp', CURRENT_TIMESTAMP(), true, 'WH-A-01', 1),
(2, 'Wireless Mouse', 'Ergonomic wireless mouse', 'SKU-WM-002', 29.99, 200, 'Electronics', 'TechCorp', CURRENT_TIMESTAMP(), true, 'WH-A-02', 1),
(3, 'USB-C Hub', '7-port USB-C hub', 'SKU-UH-003', 49.99, 150, 'Electronics', 'GadgetWorld', CURRENT_TIMESTAMP(), true, 'WH-A-03', 1),
(4, 'Cotton T-Shirt', 'Premium cotton t-shirt', 'SKU-TS-004', 19.99, 500, 'Clothing', 'FashionCo', CURRENT_TIMESTAMP(), true, 'WH-B-01', 2),
(5, 'Denim Jeans', 'Classic fit denim jeans', 'SKU-DJ-005', 59.99, 300, 'Clothing', 'FashionCo', CURRENT_TIMESTAMP(), true, 'WH-B-02', 2),
(6, 'Organic Coffee', 'Fair trade organic coffee beans', 'SKU-OC-006', 14.99, 5, 'Food', 'BeanBros', CURRENT_TIMESTAMP(), true, 'WH-C-01', 3),
(7, 'Green Tea', 'Japanese green tea', 'SKU-GT-007', 8.99, 3, 'Food', 'TeaHouse', CURRENT_TIMESTAMP(), true, 'WH-C-02', 3),
(8, 'Printer Paper', 'A4 printer paper 500 sheets', 'SKU-PP-008', 7.99, 1000, 'Office', 'OfficeMax', CURRENT_TIMESTAMP(), true, 'WH-D-01', 4),
(9, 'Desk Lamp', 'LED desk lamp adjustable', 'SKU-DL-009', 34.99, 80, 'Office', 'OfficeMax', CURRENT_TIMESTAMP(), true, 'WH-D-02', 4),
(10, 'Expired Vitamins', 'Multivitamin supplements', 'SKU-EV-010', 12.99, 50, 'Food', 'HealthCo', DATEADD('DAY', -30, CURRENT_TIMESTAMP()), true, 'WH-C-03', 3);

INSERT INTO users (id, username, password, email, full_name, role, is_enabled, created_date) VALUES
(1, 'admin', 'admin123', 'admin@legacy-company.com', 'Admin User', 'ADMIN', true, CURRENT_TIMESTAMP()),
(2, 'john', 'password', 'john@legacy-company.com', 'John Doe', 'USER', true, CURRENT_TIMESTAMP()),
(3, 'jane', 'password123', 'jane@legacy-company.com', 'Jane Smith', 'USER', true, CURRENT_TIMESTAMP());
