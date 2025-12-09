import React, { useEffect, useState } from "react";
import axios from "axios";
import Swal from "sweetalert2";
import "@fortawesome/fontawesome-free/css/all.min.css";
import EditTransactionModal from "./EditTransactionModal";

export default function TransactionTable() {
  const ITEMS_PER_PAGE = 10;
  const [transactions, setTransactions] = useState([]);
  const [filteredTransactions, setFilteredTransactions] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [loading, setLoading] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [selectedTransaction, setSelectedTransaction] = useState(null);
  const [accounts, setAccounts] = useState([]);
  const [filters, setFilters] = useState({
    accountId: "",
    category: "",
    date: "",
  });

  // Fetch all accounts for filter dropdown
  const fetchAccounts = () => {
    axios
      .get("/api/accounts")
      .then((response) => {
        setAccounts(response.data);
      })
      .catch((error) => console.error("Error fetching accounts:", error));
  };

  // Fetch all transactions
  const fetchTransactions = () => {
    setLoading(true);
    axios
      .get("/api/transactions")
      .then((response) => {
        setTransactions(response.data);
        applyFilters(response.data);
      })
      .catch((error) => {
        console.error("Error fetching transactions:", error);
        Swal.fire({
          icon: "error",
          title: "Error",
          text: "Failed to load transactions",
        });
      })
      .finally(() => setLoading(false));
  };

  // Apply filters to transactions
  const applyFilters = (transactionsList, currentFilters = filters) => {
    let filtered = transactionsList;

    if (currentFilters.accountId && filtered.length > 0) {
      filtered = filtered.filter(
        (t) => t.account && t.account.id === parseInt(currentFilters.accountId)
      );
    }

    if (currentFilters.category) {
      filtered = filtered.filter((t) => t.category === currentFilters.category);
    }

    if (currentFilters.date) {
      filtered = filtered.filter(
        (t) =>
          new Date(t.date).toLocaleDateString() ===
          new Date(currentFilters.date).toLocaleDateString()
      );
    }

    setFilteredTransactions(filtered);
    setCurrentPage(1);
  };

  // Handle filter changes
  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    const newFilters = { ...filters, [name]: value };
    setFilters(newFilters);
    applyFilters(transactions, newFilters);
  };

  // Reset filters
  const resetFilters = () => {
    setFilters({ accountId: "", category: "", date: "" });
    setFilteredTransactions(transactions);
    setCurrentPage(1);
  };

  useEffect(() => {
    fetchAccounts();
    fetchTransactions();
    // Listen for transaction updates
    window.addEventListener("transactionUpdated", fetchTransactions);
    return () =>
      window.removeEventListener("transactionUpdated", fetchTransactions);
  }, []);

  const handleEdit = (transaction) => {
    setSelectedTransaction(transaction);
    setShowEditModal(true);
  };

  const handleDelete = (id) => {
    Swal.fire({
      title: "Delete Transaction?",
      text: "This action cannot be undone.",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#d33",
      cancelButtonColor: "#3085d6",
      confirmButtonText: "Yes, delete it!",
    }).then((result) => {
      if (result.isConfirmed) {
        axios
          .delete(`/api/transactions/${id}`)
          .then(() => {
            Swal.fire({
              icon: "success",
              title: "Deleted!",
              text: "Transaction deleted successfully.",
            });
            fetchTransactions();
            window.dispatchEvent(new Event("transactionUpdated"));
          })
          .catch((error) => {
            console.error("Error deleting transaction:", error);
            Swal.fire({
              icon: "error",
              title: "Error",
              text: "Failed to delete transaction",
            });
          });
      }
    });
  };

  const handleSaveEdit = (updatedTransaction) => {
    axios
      .put(`/api/transactions/${updatedTransaction.id}`, {
        description: updatedTransaction.description,
        amount: updatedTransaction.amount,
        category: updatedTransaction.category,
      })
      .then(() => {
        Swal.fire({
          icon: "success",
          title: "Updated!",
          text: "Transaction updated successfully.",
        });
        setShowEditModal(false);
        setSelectedTransaction(null);
        fetchTransactions();
        window.dispatchEvent(new Event("transactionUpdated"));
      })
      .catch((error) => {
        console.error("Error updating transaction:", error);
        Swal.fire({
          icon: "error",
          title: "Error",
          text: "Failed to update transaction",
        });
      });
  };

  if (loading) {
    return (
      <div className="bg-white p-4 rounded shadow">Loading transactions...</div>
    );
  }

  const categories = [
    ...new Set(transactions.map((t) => t.category).filter(Boolean)),
  ];

  return (
    <div className="bg-white p-4 rounded shadow">
      <h2 className="text-lg font-semibold mb-4">Transaction Details</h2>

      {/* Filters */}
      <div className="mb-6 p-4 bg-gray-50 rounded border border-gray-200">
        <div className="flex justify-between items-center mb-3">
          <h3 className="font-semibold text-gray-700">Filters</h3>
          <button
            onClick={resetFilters}
            className="text-sm px-3 py-1 bg-gray-300 text-gray-700 rounded hover:bg-gray-400"
          >
            Reset
          </button>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-4 gap-3">
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
          <div>
            <label className="block text-xs font-medium text-gray-600 mb-1">
              Category
            </label>
            <select
              name="category"
              value={filters.category}
              onChange={handleFilterChange}
              className="w-full px-2 py-1 text-sm border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">All Categories</option>
              {categories.map((cat) => (
                <option key={cat} value={cat}>
                  {cat}
                </option>
              ))}
            </select>
          </div>
          <div>
            <label className="block text-xs font-medium text-gray-600 mb-1">
              Date
            </label>
            <input
              type="date"
              name="date"
              value={filters.date}
              onChange={handleFilterChange}
              className="w-full px-2 py-1 text-sm border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
        </div>
      </div>

      {filteredTransactions.length === 0 ? (
        <p className="text-gray-400 text-center py-6">No transactions found</p>
      ) : (
        <div className="overflow-x-auto">
          <p className="text-sm text-gray-600 mb-3">
            Showing {(currentPage - 1) * ITEMS_PER_PAGE + 1} -{" "}
            {Math.min(
              currentPage * ITEMS_PER_PAGE,
              filteredTransactions.length
            )}{" "}
            of {filteredTransactions.length} transactions
          </p>
          <table className="w-full text-sm">
            <thead className="bg-gray-100 border-b">
              <tr>
                <th className="px-4 py-2 text-left">Description</th>
                <th className="px-4 py-2 text-left">Category</th>
                <th className="px-4 py-2 text-left">Account</th>
                <th className="px-4 py-2 text-left">Date</th>
                <th className="px-4 py-2 text-right">Amount</th>
                <th className="px-4 py-2 text-center">Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredTransactions
                .slice(
                  (currentPage - 1) * ITEMS_PER_PAGE,
                  currentPage * ITEMS_PER_PAGE
                )
                .map((transaction) => (
                  <tr
                    key={transaction.id}
                    className="border-b hover:bg-gray-50"
                  >
                    <td className="px-4 py-3">{transaction.description}</td>
                    <td className="px-4 py-3">
                      <span className="inline-block bg-blue-100 text-blue-800 px-3 py-1 rounded text-xs font-semibold">
                        {transaction.category || "Uncategorized"}
                      </span>
                    </td>
                    <td className="px-4 py-3 text-sm">
                      {transaction.account?.name || "Unknown"}
                    </td>
                    <td className="px-4 py-3 text-sm text-gray-600">
                      {transaction.date
                        ? new Date(transaction.date).toLocaleDateString()
                        : "No Date"}
                    </td>
                    <td className="px-4 py-3 text-right font-semibold">
                      <span
                        className={
                          transaction.amount > 0
                            ? "text-green-600"
                            : "text-red-600"
                        }
                      >
                        {transaction.amount > 0 ? "+" : ""}$
                        {Math.abs(transaction.amount).toFixed(2)}
                      </span>
                    </td>
                    <td className="px-4 py-3 text-center">
                      <button
                        onClick={() => handleEdit(transaction)}
                        className="text-blue-500 hover:text-blue-700 mr-4"
                        title="Edit"
                      >
                        <i className="fas fa-pencil-alt"></i>
                      </button>
                      <button
                        onClick={() => handleDelete(transaction.id)}
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
              {Math.ceil(filteredTransactions.length / ITEMS_PER_PAGE)}
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
                length: Math.ceil(filteredTransactions.length / ITEMS_PER_PAGE),
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
                      Math.ceil(filteredTransactions.length / ITEMS_PER_PAGE),
                      prev + 1
                    )
                  )
                }
                disabled={
                  currentPage ===
                  Math.ceil(filteredTransactions.length / ITEMS_PER_PAGE)
                }
                className="px-3 py-1 bg-gray-300 text-gray-700 rounded hover:bg-gray-400 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                Next
              </button>
            </div>
          </div>
        </div>
      )}

      {showEditModal && selectedTransaction && (
        <EditTransactionModal
          transaction={selectedTransaction}
          onClose={() => {
            setShowEditModal(false);
            setSelectedTransaction(null);
          }}
          onSave={handleSaveEdit}
        />
      )}
    </div>
  );
}
