import apiClient from './apiClient';

export const notificationService = {
  getNotifications: async (email) => {
    const response = await apiClient.get(email ? `/api/notifications/${email}` : '/api/notifications');
    return response?.data?.data || [];
  },

  getUnreadNotifications: async (email) => {
    const response = await apiClient.get(email ? `/api/notifications/${email}/unread` : '/api/notifications/unread');
    return response?.data?.data || [];
  },

  getUnreadCount: async (email) => {
    const response = await apiClient.get(email ? `/api/notifications/${email}/unread/count` : '/api/notifications/unread/count');
    return response?.data?.data || 0;
  },

  markAsRead: async (notificationId) => {
    const response = await apiClient.put(`/api/notifications/${notificationId}/read`);
    return response?.data?.data;
  },

  markAllAsRead: async (email) => {
    const response = await apiClient.put(email ? `/api/notifications/${email}/read-all` : '/api/notifications/read-all');
    return response?.data?.data;
  },

  deleteNotification: async (notificationId) => {
    const response = await apiClient.delete(`/api/notifications/${notificationId}`);
    return response?.data?.data;
  },
};
