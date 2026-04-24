import apiClient from "./client";

export async function createUser(payload) {
  const response = await apiClient.post("/users", payload);
  return response.data;
}

export async function generateTasks(userId) {
  const response = await apiClient.post(`/tasks/generate/${userId}`);
  return response.data;
}

export async function getTasks(userId) {
  const response = await apiClient.get(`/tasks/user/${userId}`);
  return response.data;
}

export async function getProgress(userId) {
  const response = await apiClient.get(`/progress/${userId}`);
  return response.data;
}

export async function getWeakness(userId) {
  const response = await apiClient.get(`/analysis/weakness/${userId}`);
  return response.data;
}

export async function getInsight(userId) {
  const response = await apiClient.get(`/insight/${userId}`);
  return response.data;
}
