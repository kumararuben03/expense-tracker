-- Clean up existing data
DELETE FROM budgets;
DELETE FROM transactions;
DELETE FROM accounts;

-- Create multiple accounts with different currencies
INSERT INTO accounts (id, name, currency, balance) VALUES 
(1, 'My Checking Account', 'USD', 2650.00),
(2, 'Savings Account', 'USD', 15000.00),
(3, 'Travel Fund', 'EUR', 5200.50),
(4, 'Business Account', 'GBP', 8750.25);

-- ============================================
-- ACCOUNT 1 (USD) - Main Account Transactions
-- ============================================
-- Income: $5000
INSERT INTO transactions (id, description, amount, category, timestamp, account_id) VALUES 
(1, 'Monthly Salary', 5000.00, 'Income', NOW(), 1);

-- Utilities: $900
INSERT INTO transactions (id, description, amount, category, timestamp, account_id) VALUES 
(2, 'Electric Bill', -450.00, 'Utilities', NOW(), 1),
(3, 'Water Bill', -250.00, 'Utilities', NOW(), 1),
(4, 'Internet Bill', -200.00, 'Utilities', NOW(), 1);

-- Entertainment: $700
INSERT INTO transactions (id, description, amount, category, timestamp, account_id) VALUES 
(5, 'Movie Tickets', -50.00, 'Entertainment', NOW(), 1),
(6, 'Concert', -350.00, 'Entertainment', NOW(), 1),
(7, 'Video Games', -300.00, 'Entertainment', NOW(), 1);

-- Transport: $500
INSERT INTO transactions (id, description, amount, category, timestamp, account_id) VALUES 
(8, 'Gas', -200.00, 'Transport', NOW(), 1),
(9, 'Car Maintenance', -200.00, 'Transport', NOW(), 1),
(10, 'Taxi', -100.00, 'Transport', NOW(), 1);

-- Food: $250
INSERT INTO transactions (id, description, amount, category, timestamp, account_id) VALUES 
(11, 'Grocery Shopping', -150.00, 'Food', NOW(), 1),
(12, 'Restaurant Dinner', -100.00, 'Food', NOW(), 1);

-- Additional transactions for variety
INSERT INTO transactions (id, description, amount, category, timestamp, account_id) VALUES 
(13, 'Freelance Work', 1500.00, 'Income', NOW(), 1),
(14, 'Gym Membership', -50.00, 'Entertainment', NOW(), 1),
(15, 'Coffee Shop', -25.00, 'Food', NOW(), 1);

-- ============================================
-- ACCOUNT 2 (USD) - Savings Account Transactions
-- ============================================
INSERT INTO transactions (id, description, amount, category, timestamp, account_id) VALUES 
(16, 'Transfer from Checking', 1000.00, 'Income', NOW(), 2),
(17, 'Interest Earned', 15.50, 'Income', NOW(), 2);

-- ============================================
-- ACCOUNT 3 (EUR) - Travel Fund Transactions
-- ============================================
INSERT INTO transactions (id, description, amount, category, timestamp, account_id) VALUES 
(18, 'Flight Booking', -850.00, 'Transport', NOW(), 3),
(19, 'Hotel Reservation', -120.00, 'Entertainment', NOW(), 3),
(20, 'Travel Insurance', -45.00, 'Utilities', NOW(), 3);

-- ============================================
-- ACCOUNT 4 (GBP) - Business Account Transactions
-- ============================================
INSERT INTO transactions (id, description, amount, category, timestamp, account_id) VALUES 
(21, 'Client Invoice Paid', 2500.00, 'Income', NOW(), 4),
(22, 'Office Supplies', -120.00, 'Utilities', NOW(), 4),
(23, 'Software License', -199.99, 'Utilities', NOW(), 4),
(24, 'Team Lunch', -85.50, 'Food', NOW(), 4);

-- ============================================
-- BUDGETS - December 2025 (Account 1)
-- ============================================
INSERT INTO budgets (id, category, limit_amount, month, year, account_id) VALUES 
(1, 'Utilities', 1000.00, 12, 2025, 1),
(2, 'Entertainment', 500.00, 12, 2025, 1),
(3, 'Transport', 600.00, 12, 2025, 1),
(4, 'Food', 400.00, 12, 2025, 1);

-- ============================================
-- BUDGETS - November 2025 (Account 1)
-- ============================================
INSERT INTO budgets (id, category, limit_amount, month, year, account_id) VALUES 
(5, 'Utilities', 950.00, 11, 2025, 1),
(6, 'Entertainment', 450.00, 11, 2025, 1),
(7, 'Transport', 550.00, 11, 2025, 1),
(8, 'Food', 350.00, 11, 2025, 1);

-- ============================================
-- BUDGETS - October 2025 (Account 1)
-- ============================================
INSERT INTO budgets (id, category, limit_amount, month, year, account_id) VALUES 
(9, 'Utilities', 900.00, 10, 2025, 1),
(10, 'Entertainment', 500.00, 10, 2025, 1),
(11, 'Transport', 600.00, 10, 2025, 1),
(12, 'Food', 400.00, 10, 2025, 1);

-- ============================================
-- BUDGETS - Account 2 (Savings)
-- ============================================
INSERT INTO budgets (id, category, limit_amount, month, year, account_id) VALUES 
(13, 'Utilities', 500.00, 12, 2025, 2),
(14, 'Entertainment', 300.00, 12, 2025, 2),
(15, 'Transport', 400.00, 12, 2025, 2),
(16, 'Food', 250.00, 12, 2025, 2);

-- ============================================
-- BUDGETS - Account 3 (Travel Fund - EUR)
-- ============================================
INSERT INTO budgets (id, category, limit_amount, month, year, account_id) VALUES 
(17, 'Transport', 2000.00, 12, 2025, 3),
(18, 'Entertainment', 1500.00, 12, 2025, 3),
(19, 'Food', 1000.00, 12, 2025, 3),
(20, 'Utilities', 500.00, 12, 2025, 3);

-- ============================================
-- BUDGETS - Account 4 (Business - GBP)
-- ============================================
INSERT INTO budgets (id, category, limit_amount, month, year, account_id) VALUES 
(21, 'Utilities', 800.00, 12, 2025, 4),
(22, 'Entertainment', 400.00, 12, 2025, 4),
(23, 'Transport', 300.00, 12, 2025, 4),
(24, 'Food', 500.00, 12, 2025, 4);

-- ============================================
-- Reset auto-increment sequences
-- ============================================
ALTER TABLE transactions AUTO_INCREMENT = 25;
ALTER TABLE accounts AUTO_INCREMENT = 5;
ALTER TABLE budgets AUTO_INCREMENT = 25;

