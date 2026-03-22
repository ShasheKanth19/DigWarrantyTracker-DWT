import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api";

export const api = axios.create({
  baseURL: API_BASE_URL,
});

// USER APIs
export const getAllUsers = () => api.get("/users");
export const getUserById = (id) => api.get(`/users/${id}`);
export const addUser = (data) => api.post("/users", data);
export const updateUser = (id, data) => api.put(`/users/${id}`, data);
export const deleteUser = (id) => api.delete(`/users/${id}`);

// PRODUCT APIs
export const addProduct = (data) => api.post("/purchases", data);
export const updateProduct = (id, data) => api.put(`/purchases/${id}`, data);
export const getProductsByUser = (userId) => api.get(`/purchases/user/${userId}`);
export const getAllProducts = () => api.get("/purchases");

// NOTIFICATION APIs
export const getUserNotifications = (userId) => api.get(`/notifications/user/${userId}`);
export const getUnreadUserNotifications = (userId) => api.get(`/notifications/user/${userId}/unread`);
export const markNotificationRead = (id) => api.put(`/notifications/${id}/read`);
