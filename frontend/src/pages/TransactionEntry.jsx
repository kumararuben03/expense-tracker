import React, { useState, useEffect } from "react";
import axios from "axios";

export default function TransactionEntry() {
  const [form, setForm] = useState({
    description: "",
    amount: "",
    category: "",
    accountId: "",
    date: new Date().toISOString().split("T")[0],
  });
  const [status, setStatus] = useState(null);
  const [accounts, setAccounts] = useState([]);

  useEffect(() => {
    axios
      .get("/api/accounts")
      .then((response) => {
        setAccounts(response.data);
      })
      .catch((error) => console.error("Error fetching accounts:", error));
  }, []);

  function submit(e) {
    e.preventDefault();
    if (!form.accountId) {
      setStatus("Error: Please select an account");
      return;
    }
    const amountValue = parseFloat(form.amount);
    // Negative amounts for expenses, positive for income
    const amount =
      form.category && form.category.toLowerCase() !== "income"
        ? -Math.abs(amountValue)
        : amountValue;

    axios
      .post("/api/transactions", {
        description: form.description,
        amount: amount,
        category: form.category,
        account: { id: parseInt(form.accountId) },
        timestamp: form.date + "T00:00:00",
      })
      .then(() => {
        setStatus("Saved");
        setForm({
          description: "",
          amount: "",
          category: "",
          accountId: "",
          date: new Date().toISOString().split("T")[0],
        });
        // Emit event to refresh dashboard
        window.dispatchEvent(new Event("transactionUpdated"));
      })
      .catch(() => setStatus("Error"));
  }

  return (
    <div className="bg-white p-4 rounded shadow">
      <h2 className="text-lg font-semibold mb-2">Add Transaction</h2>
      <form onSubmit={submit} className="space-y-3">
        <input
          className="w-full p-2 border"
          placeholder="Description"
          value={form.description}
          onChange={(e) => setForm({ ...form, description: e.target.value })}
          required
        />
        <input
          type="number"
          className="w-full p-2 border"
          placeholder="Amount"
          step="0.01"
          value={form.amount}
          onChange={(e) => setForm({ ...form, amount: e.target.value })}
          required
        />
        <select
          className="w-full p-2 border"
          value={form.accountId}
          onChange={(e) => setForm({ ...form, accountId: e.target.value })}
          required
        >
          <option value="">Select Account</option>
          {accounts.map((acc) => (
            <option key={acc.id} value={acc.id}>
              {acc.name}
            </option>
          ))}
        </select>
        <input
          type="date"
          className="w-full p-2 border"
          value={form.date}
          onChange={(e) => setForm({ ...form, date: e.target.value })}
          required
        />
        <select
          className="w-full p-2 border"
          value={form.category}
          onChange={(e) => setForm({ ...form, category: e.target.value })}
          required
        >
          <option value="">Select Category</option>
          <option value="Income">Income</option>
          <option value="Utilities">Utilities</option>
          <option value="Entertainment">Entertainment</option>
          <option value="Transport">Transport</option>
          <option value="Food">Food</option>
        </select>
        <div>
          <button className="px-4 py-2 bg-blue-600 text-white rounded">
            Save
          </button>
        </div>
        {status && <div className="mt-2">{status}</div>}
      </form>
    </div>
  );
}
