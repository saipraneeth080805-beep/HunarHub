import axios from 'axios';
import { toast } from 'react-toastify';

const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests if available
const token = localStorage.getItem('token');
if (token) {
  api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
}

// Add response interceptor to handle errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status;
    const requestUrl = error.config?.url || '';
    const isUnreadCountRequest = requestUrl.includes('/notifications/my/unread-count');

    if (status === 403) {
      // Ignore unread-count auth failures during bootstrap; it should not block the UI.
      if (isUnreadCountRequest) {
        return Promise.reject(error);
      }
      toast.error('You are not authorized to access this resource');
    } else if (status === 401) {
      // Clear auth data on 401
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      delete api.defaults.headers.common['Authorization'];

      // Avoid forced redirect for non-critical unread count polling.
      if (!isUnreadCountRequest) {
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

export default api;
