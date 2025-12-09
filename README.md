# Expense & Budget Tracker

## Overview

Expense & Budget Tracker is a full-stack web application for managing personal finances. It allows users to track expenses, manage multiple accounts, set budgets, and analyze spending patterns. The project is built with a Java Spring Boot backend and a React frontend, using MySQL for persistent storage.

---

## Features & Functionalities

- **Account Management**:
  - Add, edit, delete, and view multiple accounts with different currencies and balances.
  - Sort and filter accounts by name, balance, or currency.
- **Pagination**:
  - All main data tables (Transactions, Accounts, Budgets) support pagination with 10 rows per page.
  - Pagination controls allow navigation between pages and reset to page 1 on filter/sort changes.
- **Transaction Management**:
  - Add, edit, delete, and view transactions for each account.
  - Filter transactions by account, category, and date range.
  - View transaction details in a sortable, filterable table.
- **Budget Management**:
  - Set monthly budgets per account and category.
  - Filter budgets by account, month, and year.
  - Edit and delete budgets.
- **Dashboard & Analytics**:
  - Visual summary of accounts, budgets, and recent transactions.
  - **Pie chart breakdown** of income or expenses by category, dynamically filtered by type (Income/Expense) and account.
  - **Dashboard filters**: Filter pie chart and summary by transaction type (All, Income Only, Expense Only) and by account.
  - Real-time updates when transactions are added/removed.
- **Data Initialization**:
  - Loads mock data for accounts, transactions, and budgets on startup for demo/testing.
  - Income and expense transactions are categorized (e.g., Utilities, Entertainment, Transport, Food) for accurate analytics.
- **Test Coverage**:
  - Comprehensive unit and integration tests for backend controllers and domain classes.
  - JaCoCo enabled with 100% coverage for key components (e.g., ReportsController, Account domain).

---

## Architecture

```
Frontend (React 18 + Tailwind CSS)
    |
    |  (REST API calls via Axios)
    v
Backend (Spring Boot 3.2.0, Java 23)
    |
    |  (JPA/Hibernate)
    v
Database (MySQL 8)
```

- **Frontend**: React SPA, communicates with backend via RESTful API.
- **Backend**: Spring Boot REST API, handles business logic, data validation, and persistence.
- **Database**: MySQL, schema auto-generated and seeded with mock data on startup.

---

## Technology Stack & Versions

- **Frontend**:
  - React: 18.2.0
  - Tailwind CSS: 3.4.7
  - Axios: 1.4.0
  - SweetAlert2: 11.10.4
  - FontAwesome: 6.5.1
- **Backend**:
  - Java: 23
  - Spring Boot: 3.2.0
  - Spring Data JPA, Lombok
  - MySQL Connector/J: 8.0.33
- **Database**:
  - MySQL: 8.x
- **Build Tools**:
  - Maven: 3.9.x

---

## Test & Use Cases

### Manual Test Cases

- **Account CRUD**: Add, edit, delete, and view accounts. Verify sorting and filtering.
- **Transaction CRUD**: Add, edit, delete, and view transactions. Test filters (account, category, date range).
- **Budget CRUD**: Add, edit, delete, and view budgets. Test filters (account, month, year).
- **Dashboard Filters**: Test dashboard pie chart and summary with different type (All, Income, Expense) and account filters. Verify pie chart updates and category breakdowns.
- **Data Integrity**: Ensure transactions and budgets are linked to valid accounts.
- **Error Handling**: Test error modals for failed API calls or invalid input.
- **UI Responsiveness**: Check layout and usability on different screen sizes.
- **Pagination**: Verify that Transactions, Accounts, and Budgets tables display 10 rows per page and that pagination controls work as expected (including after filtering or sorting).

### Example Use Cases

- Track monthly spending across multiple accounts and categories.
- Set and monitor budgets for specific accounts and categories.
- Analyze spending trends by filtering transactions by date, account, or category.
- Visualize income and expenses by category using the dashboard pie chart.
- Manage finances in multiple currencies.

---

## Getting Started

1. **Backend**
   - Configure MySQL and update credentials in `application.yml`.
   - Run with `mvn clean spring-boot:run` (port 8080).
2. **Frontend**
   - Install dependencies: `npm install`
   - Start dev server: `npm start` (port 3000 or 3001)
3. **Access**
   - Open [http://localhost:3001](http://localhost:3001) in your browser.

---

## Future Enhancements

- **Recurring Transactions**: Support for automatic recurring income/expense entries.
- **User Authentication**: Add user login, registration, and multi-user support.
- **Export/Import Data**: Export transactions and budgets to CSV/Excel; import from bank statements.
- **Mobile App**: Build a mobile version using React Native or Flutter.
- **Notifications**: Email or in-app alerts for budget overruns or large transactions.
- **Advanced Analytics**: Add trend analysis, savings goals, and custom reports.
- **Multi-language Support**: Internationalization for global users.
- **Dark Mode**: Optional dark theme for the UI.

---

## License

MIT
