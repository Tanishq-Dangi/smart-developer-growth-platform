import axios from "axios";

const baseURL = import.meta.env.VITE_API_BASE_URL || "/api";

const apiClient = axios.create({
  baseURL,
  headers: {
    "Content-Type": "application/json"
  }
});

export { baseURL };
export default apiClient;
