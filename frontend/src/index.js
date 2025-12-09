import React from "react";
import { createRoot } from "react-dom/client";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  ArcElement,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
import axios from "axios";
import "./index.css";
import App from "./App";

// Register Chart.js components
ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  ArcElement,
  BarElement,
  Title,
  Tooltip,
  Legend
);

// Configure axios default base URL for API calls
axios.defaults.baseURL = "http://localhost:8080";

const root = createRoot(document.getElementById("root"));
root.render(<App />);
