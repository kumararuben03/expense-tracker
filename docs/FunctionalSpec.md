# Expense & Budget Tracker - Functional Specification

## Overview

Monolithic Java Spring Boot backend and React + Tailwind frontend. Backend uses MySQL (Workbench) for persistence.

## High-level modules

- Backend (`backend/`): Spring Boot application providing REST API for transactions, budgets, accounts, and monthly reports.
- Frontend (`frontend/`): React single page with Dashboard and Transaction Entry pages.

## Key Entities

- Account: id, name, currency, balance
- Transaction: id, description, amount, category, timestamp, account
- Budget: id, category, limitAmount, month, year
- MonthlyReport: id, month, year, totalIncome, totalExpense, summary

## REST Endpoints (initial)

- POST `/api/transactions` : create transaction
- GET `/api/transactions` : list transactions (optional `from` and `to` query params)
- GET `/api/reports/current` : get current month report (frontend uses this endpoint)

## Scheduler

- Monthly scheduler generates monthly reports and persists them.

## Tests & Coverage

- Unit tests: service layer using Mockito
- Controller tests: MockMvc
- Repository tests: Testcontainers with MySQL (placeholder)
- Coverage: JaCoCo configured in `pom.xml`

## Notes

- Configure `application.yml` with MySQL Workbench credentials.
- Security is present as `spring-boot-starter-security` for future auth; currently endpoints are unsecured in dev profile.
