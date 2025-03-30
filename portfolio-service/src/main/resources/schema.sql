-- Drop existing tables if they exist (to avoid conflicts)
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS assets;
DROP TABLE IF EXISTS users;

-- Create Users Table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Create Assets Table
CREATE TABLE assets (
    id SERIAL PRIMARY KEY,
    symbol VARCHAR(10) UNIQUE NOT NULL,
    asset_name VARCHAR(255) NOT NULL,
    date_of_listing DATE NOT NULL,
    isin VARCHAR(20) UNIQUE NOT NULL
);

-- Create Transactions Table
CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    asset_name VARCHAR(255) NOT NULL,
    ticker VARCHAR(10) NOT NULL,
    average DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL,
    transaction_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
