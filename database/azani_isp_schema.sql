-- Azani Internet Service Provider Information System
-- Database Schema Creation Script
-- Created by: [Polycap Bruce]
-- Date: [26th September 2026]

CREATE DATABASE IF NOT EXISTS azani_isp;
USE azani_isp;

-- 1. Institution Table
CREATE TABLE institution (
    institution_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type ENUM('Primary','Junior','Senior','College') NOT NULL,
    location VARCHAR(100),
    is_ready BOOLEAN DEFAULT FALSE,
    is_disconnected BOOLEAN DEFAULT FALSE,
    date_registered DATE
);

-- 2. Contact Person Table
CREATE TABLE contact_person (
    contact_id INT AUTO_INCREMENT PRIMARY KEY,
    institution_id INT,
    full_name VARCHAR(100),
    phone VARCHAR(15),
    email VARCHAR(100),
    id_number VARCHAR(20),
    FOREIGN KEY (institution_id) REFERENCES institution(institution_id)
);

-- 3. Registration Payment Table
CREATE TABLE registration_payment (
    reg_id INT AUTO_INCREMENT PRIMARY KEY,
    institution_id INT,
    amount DECIMAL(10,2) DEFAULT 8500.00,
    payment_method VARCHAR(30),
    payment_date DATE,
    FOREIGN KEY (institution_id) REFERENCES institution(institution_id)
);

-- 4. Installation Payment Table
CREATE TABLE installation_payment (
    install_id INT AUTO_INCREMENT PRIMARY KEY,
    institution_id INT,
    amount DECIMAL(10,2) DEFAULT 10000.00,
    payment_method VARCHAR(30),
    payment_date DATE,
    FOREIGN KEY (institution_id) REFERENCES institution(institution_id)
);

-- 5. Monthly Payment Table (Includes your assumptions)
CREATE TABLE monthly_payment (
    monthly_id INT AUTO_INCREMENT PRIMARY KEY,
    institution_id INT,
    bandwidth INT,
    amount DECIMAL(10,2),
    discount DECIMAL(10,2) DEFAULT 0,
    fine DECIMAL(10,2) DEFAULT 0,
    reconnection_fee DECIMAL(10,2) DEFAULT 0,
    month VARCHAR(20),
    year INT,
    payment_method VARCHAR(30),
    is_paid BOOLEAN DEFAULT FALSE,
    is_upgrade BOOLEAN DEFAULT FALSE,
    payment_date DATE,
    FOREIGN KEY (institution_id) REFERENCES institution(institution_id)
);

-- 6. Infrastructure Table
CREATE TABLE infrastructure (
    infra_id INT AUTO_INCREMENT PRIMARY KEY,
    institution_id INT,
    num_users INT DEFAULT 0,
    num_computers INT DEFAULT 0,
    num_lan_nodes INT DEFAULT 0,
    has_lan BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (institution_id) REFERENCES institution(institution_id)
);

-- 7. Computer Purchase Table
CREATE TABLE computer_purchase (
    purchase_id INT AUTO_INCREMENT PRIMARY KEY,
    institution_id INT,
    quantity INT,
    unit_price DECIMAL(10,2) DEFAULT 40000.00,
    total_cost DECIMAL(10,2),
    FOREIGN KEY (institution_id) REFERENCES institution(institution_id)
);

-- 8. LAN Purchase Table
CREATE TABLE lan_purchase (
    lan_id INT AUTO_INCREMENT PRIMARY KEY,
    institution_id INT,
    num_nodes INT,
    total_cost DECIMAL(10,2),
    FOREIGN KEY (institution_id) REFERENCES institution(institution_id)
);