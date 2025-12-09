import React, { useEffect, useState } from "react";
import axios from "axios";
import Swal from "sweetalert2";

export default function AccountManagement() {
  const ITEMS_PER_PAGE = 10;
  const [accounts, setAccounts] = useState([]);
  const [displayedAccounts, setDisplayedAccounts] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [loading, setLoading] = useState(false);
  const [showForm, setShowForm] = useState(false);
  const [sortBy, setSortBy] = useState("name");
  const [filterCurrency, setFilterCurrency] = useState("");
  const [formData, setFormData] = useState({
    name: "",
    currency: "USD",
    balance: 0,
  });
  const [editingId, setEditingId] = useState(null);

  // Fetch all accounts
  const fetchAccounts = () => {
    setLoading(true);
    axios
      .get("/api/accounts")
      .then((response) => {
        setAccounts(response.data);
        applyFiltersAndSort(response.data);
      })
      .catch((error) => {
        console.error("Error fetching accounts:", error);
        Swal.fire({
          icon: "error",
          title: "Error",
          text: "Failed to load accounts",
        });
      })
      .finally(() => setLoading(false));
  };

  // Apply filters and sorting
  const applyFiltersAndSort = (
    accountsList,
    sort = sortBy,
    currency = filterCurrency
  ) => {
    let filtered = accountsList;

    if (currency) {
      filtered = filtered.filter((acc) => acc.currency === currency);
    }

    let sorted = [...filtered];
    if (sort === "name") {
      sorted.sort((a, b) => a.name.localeCompare(b.name));
    } else if (sort === "balance-asc") {
      sorted.sort((a, b) => a.balance - b.balance);
    } else if (sort === "balance-desc") {
      sorted.sort((a, b) => b.balance - a.balance);
    } else if (sort === "currency") {
      sorted.sort((a, b) => a.currency.localeCompare(b.currency));
    }

    setDisplayedAccounts(sorted);
    setCurrentPage(1);
  };

  // Handle filter and sort changes
  const handleFilterChange = (e) => {
    const value = e.target.value;
    setFilterCurrency(value);
    applyFiltersAndSort(accounts, sortBy, value);
  };

  const handleSortChange = (e) => {
    const value = e.target.value;
    setSortBy(value);
    applyFiltersAndSort(accounts, value, filterCurrency);
  };

  const handleSaveComplete = () => {
    fetchAccounts();
    setCurrentPage(1);
  };

  useEffect(() => {
    fetchAccounts();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: name === "balance" ? parseFloat(value) || 0 : value,
    });
  };

  const validateForm = () => {
    if (!formData.name.trim()) {
      Swal.fire("Error", "Account name is required", "error");
      return false;
    }
    return true;
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (!validateForm()) return;

    const request = editingId
      ? axios.put(`/api/accounts/${editingId}`, formData)
      : axios.post("/api/accounts", formData);

    request
      .then(() => {
        Swal.fire({
          icon: "success",
          title: editingId ? "Updated!" : "Created!",
          text: editingId
            ? "Account updated successfully"
            : "Account created successfully",
        });
        setFormData({ name: "", currency: "USD", balance: 0 });
        setEditingId(null);
        setShowForm(false);
        handleSaveComplete();
      })
      .catch((error) => {
        console.error("Error saving account:", error);
        Swal.fire("Error", "Failed to save account", "error");
      });
  };

  const handleEdit = (account) => {
    setFormData(account);
    setEditingId(account.id);
    setShowForm(true);
  };

  const handleDelete = (id) => {
    Swal.fire({
      title: "Delete Account?",
      text: "This will also delete all associated transactions and budgets.",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#d33",
      cancelButtonColor: "#3085d6",
      confirmButtonText: "Yes, delete it!",
    }).then((result) => {
      if (result.isConfirmed) {
        axios
          .delete(`/api/accounts/${id}`)
          .then(() => {
            Swal.fire("Deleted!", "Account deleted successfully", "success");
            fetchAccounts();
          })
          .catch((error) => {
            console.error("Error deleting account:", error);
            Swal.fire("Error", "Failed to delete account", "error");
          });
      }
    });
  };

  const handleCancel = () => {
    setShowForm(false);
    setFormData({ name: "", currency: "USD", balance: 0 });
    setEditingId(null);
  };

  if (loading) {
    return (
      <div className="bg-white p-4 rounded shadow">Loading accounts...</div>
    );
  }

  return (
    <div className="bg-white p-4 rounded shadow">
      <div className="flex justify-between items-center mb-4">
        <h2 className="text-lg font-semibold">Accounts</h2>
        {!showForm && (
          <button
            onClick={() => setShowForm(true)}
            className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600"
          >
            + New Account
          </button>
        )}
      </div>

      {/* Filters and Sort */}
      <div className="mb-6 p-4 bg-gray-50 rounded border border-gray-200">
        <h3 className="font-semibold text-gray-700 mb-3">Filters & Sort</h3>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-3">
          <div>
            <label className="block text-xs font-medium text-gray-600 mb-1">
              Filter by Currency
            </label>
            <select
              value={filterCurrency}
              onChange={handleFilterChange}
              className="w-full px-2 py-1 text-sm border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">All Currencies</option>
              {[...new Set(accounts.map((a) => a.currency))].map((curr) => (
                <option key={curr} value={curr}>
                  {curr}
                </option>
              ))}
            </select>
          </div>
          <div>
            <label className="block text-xs font-medium text-gray-600 mb-1">
              Sort By
            </label>
            <select
              value={sortBy}
              onChange={handleSortChange}
              className="w-full px-2 py-1 text-sm border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="name">Name (A-Z)</option>
              <option value="balance-asc">Balance (Low to High)</option>
              <option value="balance-desc">Balance (High to Low)</option>
              <option value="currency">Currency (A-Z)</option>
            </select>
          </div>
          <div className="flex items-end">
            <p className="text-sm text-gray-600">
              Showing {displayedAccounts.length} of {accounts.length} accounts
            </p>
          </div>
        </div>
      </div>

      {showForm && (
        <form onSubmit={handleSubmit} className="mb-6 p-4 bg-gray-50 rounded">
          <div className="mb-4">
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Account Name
            </label>
            <input
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="e.g., Checking Account"
            />
          </div>

          <div className="grid grid-cols-2 gap-4 mb-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Currency
              </label>
              <select
                name="currency"
                value={formData.currency}
                onChange={handleChange}
                className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="USD">USD</option>
                <option value="EUR">EUR</option>
                <option value="GBP">GBP</option>
                <option value="CAD">CAD</option>
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Balance
              </label>
              <input
                type="number"
                name="balance"
                value={formData.balance}
                onChange={handleChange}
                step="0.01"
                className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="0.00"
              />
            </div>
          </div>

          <div className="flex gap-2">
            <button
              type="submit"
              className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
            >
              {editingId ? "Update" : "Create"} Account
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

      {displayedAccounts.length === 0 ? (
        <p className="text-gray-400 text-center py-6">No accounts found</p>
      ) : (
        <div className="overflow-x-auto">
          <p className="text-sm text-gray-600 mb-3">
            Showing {(currentPage - 1) * ITEMS_PER_PAGE + 1} -{" "}
            {Math.min(currentPage * ITEMS_PER_PAGE, displayedAccounts.length)}{" "}
            of {displayedAccounts.length} accounts
          </p>
          <table className="w-full text-sm">
            <thead className="bg-gray-100 border-b">
              <tr>
                <th className="px-4 py-2 text-left">Name</th>
                <th className="px-4 py-2 text-left">Currency</th>
                <th className="px-4 py-2 text-right">Balance</th>
                <th className="px-4 py-2 text-center">Actions</th>
              </tr>
            </thead>
            <tbody>
              {displayedAccounts
                .slice(
                  (currentPage - 1) * ITEMS_PER_PAGE,
                  currentPage * ITEMS_PER_PAGE
                )
                .map((account) => (
                  <tr key={account.id} className="border-b hover:bg-gray-50">
                    <td className="px-4 py-3 font-semibold">{account.name}</td>
                    <td className="px-4 py-3">{account.currency}</td>
                    <td className="px-4 py-3 text-right font-semibold">
                      <span
                        className={
                          account.balance >= 0
                            ? "text-green-600"
                            : "text-red-600"
                        }
                      >
                        {account.currency}{" "}
                        {Math.abs(account.balance).toFixed(2)}
                      </span>
                    </td>
                    <td className="px-4 py-3 text-center">
                      <button
                        onClick={() => handleEdit(account)}
                        className="text-blue-500 hover:text-blue-700 mr-4"
                        title="Edit"
                      >
                        <i className="fas fa-pencil-alt"></i>
                      </button>
                      <button
                        onClick={() => handleDelete(account.id)}
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
              {Math.ceil(displayedAccounts.length / ITEMS_PER_PAGE)}
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
                length: Math.ceil(displayedAccounts.length / ITEMS_PER_PAGE),
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
                      Math.ceil(displayedAccounts.length / ITEMS_PER_PAGE),
                      prev + 1
                    )
                  )
                }
                disabled={
                  currentPage ===
                  Math.ceil(displayedAccounts.length / ITEMS_PER_PAGE)
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
