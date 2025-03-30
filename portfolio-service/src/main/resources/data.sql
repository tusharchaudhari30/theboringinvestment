-- Insert Users
INSERT INTO users (email, password) VALUES
('user1@example.com', 'password123'),
('user2@example.com', 'securepass456');

-- Insert Assets (Stocks)
INSERT INTO assets (symbol, asset_name, date_of_listing, isin) VALUES
('AAPL', 'Apple Inc.', '1980-12-12', 'US0378331005'),
('GOOGL', 'Alphabet Inc.', '2004-08-19', 'US02079K3059'),
('TSLA', 'Tesla Inc.', '2010-06-29', 'US88160R1014'),
('MSFT', 'Microsoft Corp.', '1986-03-13', 'US5949181045'),
('AMZN', 'Amazon.com Inc.', '1997-05-15', 'US0231351067');

-- Insert Transactions
INSERT INTO transactions (user_id, asset_name, ticker, average, quantity, transaction_date) VALUES
(1, 'Apple Inc.', 'AAPL', 150.25, 10, '2024-03-28 10:30:00'),
(1, 'Tesla Inc.', 'TSLA', 680.50, 5, '2024-03-28 11:00:00'),
(2, 'Alphabet Inc.', 'GOOGL', 2750.00, 3, '2024-03-28 12:00:00'),
(2, 'Microsoft Corp.', 'MSFT', 305.75, 8, '2024-03-28 14:00:00');
