import apiClient from "./client";

export async function signup(payload) {
  const response = await apiClient.post("/api/auth/signup", payload);
  return response.data;
}

export async function login(payload) {
  const response = await apiClient.post("/api/auth/login", payload);
  return response.data;
}

export async function createUser(payload) {
  const response = await apiClient.post("/users", payload);
  return response.data;
}

export async function generateTasks() {
  const response = await apiClient.post("/tasks/generate");
  return response.data;
}

export async function getTasks() {
  const response = await apiClient.get("/tasks");
  return response.data;
}

export async function completeTask(taskId) {
  const response = await apiClient.put(`/tasks/${taskId}/complete`);
  return response.data;
}

export async function getProgress() {
  const response = await apiClient.get("/progress");
  return response.data;
}

export async function getWeakness() {
  const response = await apiClient.get("/analysis/weakness");
  return response.data;
}

export async function getInsight() {
  const response = await apiClient.get("/insight");
  return response.data;
}
