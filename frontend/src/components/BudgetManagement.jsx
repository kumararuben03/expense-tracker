import React, { useEffect, useState } from "react";
import axios from "axios";
import Swal from "sweetalert2";

export default function BudgetManagement() {
  const ITEMS_PER_PAGE = 10;
  const [budgets, setBudgets] = useState([]);
  const [filteredBudgets, setFilteredBudgets] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [loading, setLoading] = useState(false);
  const [showForm, setShowForm] = useState(false);
  const [accounts, setAccounts] = useState([]);
  const [formData, setFormData] = useState({
    category: "",
    limitAmount: 0,
    month: new Date().getMonth() + 1,
    year: new Date().getFullYear(),
    accountId: "",
  });
  const [accountFilter, setAccountFilter] = useState("");
  const [editingId, setEditingId] = useState(null);

  // Fetch all accounts for filter dropdown
  const fetchAccounts = () => {
    axios
      .get("/api/accounts")
      .then((response) => {
        setAccounts(response.data);
      })
      .catch((error) => console.error("Error fetching accounts:", error));
  };

  // Fetch budgets for current month
  const fetchBudgets = () => {
    setLoading(true);
    const month = formData.month;
    const year = formData.year;
    axios
      .get(`/api/budgets/month/${month}/year/${year}`)
      .then((response) => {
        setBudgets(response.data);
        applyAccountFilter(response.data);
      })
      .catch((error) => {
        console.error("Error fetching budgets:", error);
        Swal.fire({
          icon: "error",
          title: "Error",
          text: "Failed to load budgets",
        });
      })
      .finally(() => setLoading(false));
  };

  // Apply account filter to budgets
  const applyAccountFilter = (budgetsList, accountId = accountFilter) => {
    if (accountId) {
      const filtered = budgetsList.filter(
        (b) => b.account && b.account.id === parseInt(accountId)
      );
      setFilteredBudgets(filtered);
    } else {
      setFilteredBudgets(budgetsList);
    }
    setCurrentPage(1);
  };

  // Handle account filter change
  const handleAccountFilterChange = (e) => {
    const value = e.target.value;
    setAccountFilter(value);
    applyAccountFilter(budgets, value);
  };

  useEffect(() => {
    fetchAccounts();
    fetchBudgets();
  }, [formData.month, formData.year]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]:
        name === "limitAmount"
          ? parseFloat(value) || 0
          : name === "month" || name === "year"
          ? parseInt(value)
          : value,
    });
  };

  const validateForm = () => {
    if (!formData.category.trim()) {
      Swal.fire("Error", "Category is required", "error");
      return false;
    }
    if (formData.limitAmount <= 0) {
      Swal.fire("Error", "Budget limit must be greater than 0", "error");
      return false;
    }
    return true;
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (!validateForm()) return;
    if (!formData.accountId) {
      Swal.fire("Error", "Please select an account", "error");
      return;
    }

    const submitData = {
      category: formData.category,
      limitAmount: formData.limitAmount,
      month: formData.month,
      year: formData.year,
      account: { id: parseInt(formData.accountId) },
    };

    const request = editingId
      ? axios.put(`/api/budgets/${editingId}`, submitData)
      : axios.post("/api/budgets", submitData);

    request
      .then(() => {
        Swal.fire({
          icon: "success",
          title: editingId ? "Updated!" : "Created!",
          text: editingId
            ? "Budget updated successfully"
            : "Budget created successfully",
        });
        setFormData({
          category: "",
          limitAmount: 0,
          month: new Date().getMonth() + 1,
          year: new Date().getFullYear(),
          accountId: "",
        });
        setEditingId(null);
        setShowForm(false);
        fetchBudgets();
      })
      .catch((error) => {
        console.error("Error saving budget:", error);
        Swal.fire("Error", "Failed to save budget", "error");
      });
  };

  const handleEdit = (budget) => {
    setFormData({
      category: budget.category,
      limitAmount: budget.limitAmount,
      month: budget.month,
      year: budget.year,
      accountId: budget.account?.id || "",
    });
    setEditingId(budget.id);
    setShowForm(true);
  };

  const handleDelete = (id) => {
    Swal.fire({
      title: "Delete Budget?",
      text: "This will remove the budget constraint.",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#d33",
      cancelButtonColor: "#3085d6",
      confirmButtonText: "Yes, delete it!",
    }).then((result) => {
      if (result.isConfirmed) {
        axios
          .delete(`/api/budgets/${id}`)
          .then(() => {
            Swal.fire("Deleted!", "Budget deleted successfully", "success");
            fetchBudgets();
          })
          .catch((error) => {
            console.error("Error deleting budget:", error);
            Swal.fire("Error", "Failed to delete budget", "error");
          });
      }
    });
  };

  const handleCancel = () => {
    setShowForm(false);
    setFormData({
      category: "",
      limitAmount: 0,
      month: new Date().getMonth() + 1,
      year: new Date().getFullYear(),
    });
    setEditingId(null);
  };

  if (loading) {
    return (
      <div className="bg-white p-4 rounded shadow">Loading budgets...</div>
    );
  }

  return (
    <div className="bg-white p-4 rounded shadow">
      <div className="flex justify-between items-center mb-4">
        <h2 className="text-lg font-semibold">Budgets</h2>
        {!showForm && (
          <button
            onClick={() => setShowForm(true)}
            className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600"
          >
            + New Budget
          </button>
        )}
      </div>

      {showForm && (
        <form onSubmit={handleSubmit} className="mb-6 p-4 bg-gray-50 rounded">
          <div className="mb-4">
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Account
            </label>
            <select
              name="accountId"
              value={formData.accountId}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            >
              <option value="">Select an account</option>
              {accounts.map((acc) => (
                <option key={acc.id} value={acc.id}>
                  {acc.name}
                </option>
              ))}
            </select>
          </div>

          <div className="mb-4">
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Category
            </label>
            <select
              name="category"
              value={formData.category}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">Select a category</option>
              <option value="Utilities">Utilities</option>
              <option value="Entertainment">Entertainment</option>
              <option value="Transport">Transport</option>
              <option value="Food">Food</option>
            </select>
          </div>

          <div className="grid grid-cols-3 gap-4 mb-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Limit Amount
              </label>
              <input
                type="number"
                name="limitAmount"
                value={formData.limitAmount}
                onChange={handleChange}
                step="0.01"
                className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="0.00"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Month
              </label>
              <select
                name="month"
                value={formData.month}
                onChange={handleChange}
                className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                {Array.from({ length: 12 }, (_, i) => (
                  <option key={i + 1} value={i + 1}>
                    {new Date(2024, i).toLocaleString("default", {
                      month: "long",
                    })}
                  </option>
                ))}
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Year
              </label>
              <input
                type="number"
                name="year"
                value={formData.year}
                onChange={handleChange}
                className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                min="2020"
                max="2099"
              />
            </div>
          </div>

          <div className="flex gap-2">
            <button
              type="submit"
              className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
            >
              {editingId ? "Update" : "Create"} Budget
            </button>
            <button
              type="button"
              onClick={handleCancel}
              className="px-4 py-2 bg-gray-300 text-gray-800 rounded hover:bg-gray-400"
            >
              Cancel
            </button>
          </div>
        </form>
      )}

      <div className="mb-4">
        <p className="text-sm text-gray-600">
          Showing budgets for{" "}
          <strong>
            {new Date(2024, formData.month - 1).toLocaleString("default", {
              month: "long",
            })}{" "}
            {formData.year}
          </strong>
        </p>
      </div>

      {/* Month and Year Filter */}
      <div className="mb-6 p-4 bg-gray-50 rounded border border-gray-200">
        <div className="flex items-center gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Month:
            </label>
            <select
              name="month"
              value={formData.month}
              onChange={handleChange}
              className="px-3 py-1 text-sm border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              {Array.from({ length: 12 }, (_, i) => (
                <option key={i + 1} value={i + 1}>
                  {new Date(2024, i).toLocaleString("default", {
                    month: "long",
                  })}
                </option>
              ))}
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Year:
            </label>
            <input
              type="number"
              name="year"
              value={formData.year}
              onChange={handleChange}
              className="px-3 py-1 text-sm border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              min="2020"
              max="2099"
              style={{ width: "100px" }}
            />
          </div>
        </div>
      </div>

      {/* Account Filter */}
      <div className="mb-6 p-4 bg-gray-50 rounded border border-gray-200">
        <div className="flex items-center gap-4">
          <label className="block text-sm font-medium text-gray-700">
            Filter by Account:
          </label>
          <select
            value={accountFilter}
            onChange={handleAccountFilterChange}
            className="px-3 py-1 text-sm border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="">All Accounts</option>
            {accounts.map((acc) => (
              <option key={acc.id} value={acc.id}>
                {acc.name} ({acc.currency})
              </option>
            ))}
          </select>
          <span className="text-sm text-gray-600">
            Showing {filteredBudgets.length} budget(s)
          </span>
        </div>
      </div>

      {filteredBudgets.length === 0 ? (
        <p className="text-gray-400 text-center py-6">No budgets found</p>
      ) : (
        <div className="overflow-x-auto">
          <p className="text-sm text-gray-600 mb-3">
            Showing {(currentPage - 1) * ITEMS_PER_PAGE + 1} -{" "}
            {Math.min(currentPage * ITEMS_PER_PAGE, filteredBudgets.length)} of{" "}
            {filteredBudgets.length} budgets
          </p>
          <table className="w-full text-sm">
            <thead className="bg-gray-100 border-b">
              <tr>
                <th className="px-4 py-2 text-left">Category</th>
                <th className="px-4 py-2 text-left">Account</th>
                <th className="px-4 py-2 text-right">Limit</th>
                <th className="px-4 py-2 text-left">Period</th>
                <th className="px-4 py-2 text-center">Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredBudgets
                .slice(
                  (currentPage - 1) * ITEMS_PER_PAGE,
                  currentPage * ITEMS_PER_PAGE
                )
                .map((budget) => (
                  <tr key={budget.id} className="border-b hover:bg-gray-50">
                    <td className="px-4 py-3 font-semibold">
                      {budget.category}
                    </td>
                    <td className="px-4 py-3 text-sm">
                      {budget.account?.name || "Unknown"}
                    </td>
                    <td className="px-4 py-3 text-right">
                      <span className="bg-blue-100 text-blue-800 px-3 py-1 rounded text-xs">
                        ${budget.limitAmount.toFixed(2)}
                      </span>
                    </td>
                    <td className="px-4 py-3 text-sm text-gray-600">
                      {new Date(2024, budget.month - 1).toLocaleString(
                        "default",
                        {
                          month: "short",
                        }
                      )}{" "}
                      {budget.year}
                    </td>
                    <td className="px-4 py-3 text-center">
                      <button
                        onClick={() => handleEdit(budget)}
                        className="text-blue-500 hover:text-blue-700 mr-4"
                        title="Edit"
                      >
                        <i className="fas fa-pencil-alt"></i>
                      </button>
                      <button
                        onClick={() => handleDelete(budget.id)}
                        className="text-red-500 hover:text-red-700"
                        title="Delete"
                      >
                        <i className="fas fa-trash"></i>
                      </button>
                    </td>
                  </tr>
                ))}
            </tbody>
          </table>

          {/* Pagination Controls */}
          <div className="flex justify-between items-center mt-4">
            <div className="text-sm text-gray-600">
              Page {currentPage} of{" "}
              {Math.ceil(filteredBudgets.length / ITEMS_PER_PAGE)}
            </div>
            <div className="flex gap-2">
              <button
                onClick={() => setCurrentPage((prev) => Math.max(1, prev - 1))}
                disabled={currentPage === 1}
                className="px-3 py-1 bg-gray-300 text-gray-700 rounded hover:bg-gray-400 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                Previous
              </button>
              {Array.from({
                length: Math.ceil(filteredBudgets.length / ITEMS_PER_PAGE),
              }).map((_, i) => (
                <button
                  key={i + 1}
                  onClick={() => setCurrentPage(i + 1)}
                  className={`px-3 py-1 rounded ${
                    currentPage === i + 1
                      ? "bg-blue-500 text-white"
                      : "bg-gray-300 text-gray-700 hover:bg-gray-400"
                  }`}
                >
                  {i + 1}
                </button>
              ))}
              <button
                onClick={() =>
                  setCurrentPage((prev) =>
                    Math.min(
                      Math.ceil(filteredBudgets.length / ITEMS_PER_PAGE),
                      prev + 1
                    )
                  )
                }
                disabled={
                  currentPage ===
                  Math.ceil(filteredBudgets.length / ITEMS_PER_PAGE)
                }
                className="px-3 py-1 bg-gray-300 text-gray-700 rounded hover:bg-gray-400 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                Next
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
