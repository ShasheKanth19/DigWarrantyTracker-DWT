import { api } from "./api";

export const getDashboardStats = () => api.get("/ml/dashboard-stats");
export const getMLMetrics = () => api.get("/ml/metrics");
export const getPredictionForProduct = (productId) => api.get(`/ml/predictions/${productId}`);
