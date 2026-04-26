import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/v1'; // Ajustar según API Gateway

const api = axios.create({
  baseURL: API_BASE_URL,
});

// Interceptor para añadir el token JWT
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const authService = {
  login: (credentials: any) => api.post('/auth/login', credentials),
};

export const productService = {
  getAll: () => api.get('/products'),
  getById: (id: string) => api.get(`/products/${id}`),
};

export const inventoryService = {
  consume: (productId: string, quantity: number, reference: string) =>
    api.post('/movements/consume', null, {
      params: { productId, quantity, reference }
    }),
};

export default api;