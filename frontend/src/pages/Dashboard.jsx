import React, { useEffect, useState } from "react";
import { Chart as ChartJS } from "chart.js";
import { Pie } from "react-chartjs-2";
import axios from "axios";

export default function Dashboard() {
  const [summary, setSummary] = useState({
    totalIncome: 0,
    totalExpense: 0,
    byCategory: {},
  });
  const [error, setError] = useState(null);
  const [accounts, setAccounts] = useState([]);
  const [filters, setFilters] = useState({
    accountId: "",
    type: "", // "" for all, "income", "expense"
  });

  // Fetch accounts for filter dropdown
  const fetchAccounts = () => {
    axios
      .get("/api/accounts")
      .then((response) => {
        setAccounts(response.data);
      })
      .catch((error) => console.error("Error fetching accounts:", error));
  };

  // Fetch dashboard summary from backend with filters
  const fetchSummary = () => {
    const params = new URLSearchParams();
    if (filters.accountId) {
      params.append("accountId", filters.accountId);
    }
    if (filters.type) {
      params.append("type", filters.type);
    }

    axios
      .get(`/api/reports/summary?${params.toString()}`)
      .then((r) => {
        setSummary(
          r.data || { totalIncome: 0, totalExpense: 0, byCategory: {} }
        );
        setError(null);
      })
      .catch((err) => {
        console.error("Error fetching reports:", err);
        setError("Failed to load report data");
      });
  };

  useEffect(() => {
    fetchAccounts();
  }, []);

  useEffect(() => {
    fetchSummary();
    // Listen for custom event when transaction is added/removed
    window.addEventListener("transactionUpdated", fetchSummary);
    return () => window.removeEventListener("transactionUpdated", fetchSummary);
  }, [filters]);

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters({ ...filters, [name]: value });
  };

  const handleResetFilters = () => {
    setFilters({ accountId: "", type: "" });
  };

  const pieData = {
    labels: Object.keys(summary.byCategory || {}),
    datasets: [
      {
        label:
          filters.type === "income"
            ? "Income by Category"
            : filters.type === "expense"
            ? "Expenses by Category"
            : "Transactions by Category",
        data: Object.values(summary.byCategory || {}),
        backgroundColor: [
          "#60A5FA",
          "#F97316",
          "#34D399",
          "#F472B6",
          "#FBBF24",
        ],
        borderColor: ["#1E40AF", "#C2410C", "#059669", "#BE185D", "#92400E"],
        borderWidth: 2,
      },
    ],
  };

  const chartOptions = {
    responsive: true,
    maintainAspectRatio: true,
    plugins: {
      legend: {
        position: "bottom",
      },
    },
  };

  return (
    <div className="bg-white p-6 rounded shadow">
      <h2 className="text-xl font-semibold mb-4">Dashboard</h2>
      {error && <div className="text-red-600 mb-4">{error}</div>}

      {/* Filters */}
      <div className="mb-6 p-4 bg-gray-50 rounded border border-gray-200">
        <div className="flex justify-between items-center mb-3">
          <h3 className="font-semibold text-gray-700">Filters</h3>
          <button
            onClick={handleResetFilters}
            className="text-sm px-3 py-1 bg-gray-300 text-gray-700 rounded hover:bg-gray-400"
          >
            Reset
          </button>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-3">
          <div>
            <label className="block text-xs font-medium text-gray-600 mb-1">
              Type
            </label>
            <select
              name="type"
              value={filters.type}
              onChange={handleFilterChange}
              className="w-full px-2 py-1 text-sm border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">All Transactions</option>
              <option value="income">Income Only</option>
              <option value="expense">Expense Only</option>
            </select>
          </div>
          <div>
            <label className="block text-xs font-medium text-gray-600 mb-1">
              Account
            </label>
            <select
              name="accountId"
              value={filters.accountId}
              onChange={handleFilterChange}
              className="w-full px-2 py-1 text-sm border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">All Accounts</option>
              {accounts.map((acc) => (
                <option key={acc.id} value={acc.id}>
                  {acc.name}
                </option>
              ))}
            </select>
          </div>
        </div>
      </div>

      <div className="grid grid-cols-2 gap-4 mb-6">
        <div className="p-4 bg-green-50 rounded">
          <p className="text-sm text-gray-600">Total Income</p>
          <p className="text-2xl font-bold text-green-600">
            ${(summary.totalIncome || 0).toFixed(2)}
          </p>
        </div>
        <div className="p-4 bg-red-50 rounded">
          <p className="text-sm text-gray-600">Total Expense</p>
          <p className="text-2xl font-bold text-red-600">
            ${(summary.totalExpense || 0).toFixed(2)}
          </p>
        </div>
      </div>
      <div>
        <h3 className="text-sm font-semibold mb-3">
          {filters.type === "income"
            ? "Income by Category"
            : filters.type === "expense"
            ? "Expenses by Category"
            : "Transactions by Category"}
        </h3>
        {Object.keys(summary.byCategory || {}).length > 0 ? (
          <div style={{ maxWidth: 350, margin: "0 auto" }}>
            <Pie data={pieData} options={chartOptions} />
          </div>
        ) : (
          <p className="text-gray-400 text-center py-6">
            No data available for selected filters
          </p>
        )}
      </div>
    </div>
  );
}
