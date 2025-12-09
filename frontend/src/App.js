import React, { useState } from "react";
import Dashboard from "./pages/Dashboard";
import TransactionEntry from "./pages/TransactionEntry";
import TransactionTable from "./components/TransactionTable";
import AccountManagement from "./components/AccountManagement";
import BudgetManagement from "./components/BudgetManagement";

export default function App() {
  const [activeTab, setActiveTab] = useState("dashboard");

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="container mx-auto p-6">
        <h1 className="text-2xl font-bold mb-6">Expense & Budget Tracker</h1>

        {/* Tab Navigation */}
        <div className="mb-6 border-b border-gray-200">
          <nav className="flex gap-4">
            <button
              onClick={() => setActiveTab("dashboard")}
              className={`px-4 py-2 font-medium border-b-2 transition ${
                activeTab === "dashboard"
                  ? "border-blue-500 text-blue-600"
                  : "border-transparent text-gray-600 hover:text-gray-900"
              }`}
            >
              Dashboard
            </button>
            <button
              onClick={() => setActiveTab("transactions")}
              className={`px-4 py-2 font-medium border-b-2 transition ${
                activeTab === "transactions"
                  ? "border-blue-500 text-blue-600"
                  : "border-transparent text-gray-600 hover:text-gray-900"
              }`}
            >
              Transactions
            </button>
            <button
              onClick={() => setActiveTab("accounts")}
              className={`px-4 py-2 font-medium border-b-2 transition ${
                activeTab === "accounts"
                  ? "border-blue-500 text-blue-600"
                  : "border-transparent text-gray-600 hover:text-gray-900"
              }`}
            >
              Accounts
            </button>
            <button
              onClick={() => setActiveTab("budgets")}
              className={`px-4 py-2 font-medium border-b-2 transition ${
                activeTab === "budgets"
                  ? "border-blue-500 text-blue-600"
                  : "border-transparent text-gray-600 hover:text-gray-900"
              }`}
            >
              Budgets
            </button>
          </nav>
        </div>

        {/* Dashboard Tab */}
        {activeTab === "dashboard" && (
          <div>
            <Dashboard />
          </div>
        )}

        {/* Transactions Tab */}
        {activeTab === "transactions" && (
          <div className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <TransactionEntry />
            </div>
            <TransactionTable />
          </div>
        )}

        {/* Accounts Tab */}
        {activeTab === "accounts" && <AccountManagement />}

        {/* Budgets Tab */}
        {activeTab === "budgets" && <BudgetManagement />}
      </div>
    </div>
  );
}
