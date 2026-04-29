/* Utility functions for token management */

export const tokenUtils = {
  setToken: (token) => {
    localStorage.setItem('authToken', token);
  },

  getToken: () => {
    return localStorage.getItem('authToken');
  },

  removeToken: () => {
    localStorage.removeItem('authToken');
  },

  isTokenValid: () => {
    const token = localStorage.getItem('authToken');
    return !!token;
  },
};

/* Utility functions for user management */

export const userUtils = {
  setUser: (user) => {
    localStorage.setItem('user', JSON.stringify(user));
  },

  getUser: () => {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  },

  setUserRole: (role) => {
    localStorage.setItem('userRole', role);
  },

  getUserRole: () => {
    return localStorage.getItem('userRole');
  },

  clearUser: () => {
    localStorage.removeItem('user');
    localStorage.removeItem('userRole');
  },
};

/* Validation utility functions */

export const validationUtils = {
  isValidEmail: (email) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  },

  isValidPassword: (password) => {
    return password && password.length >= 8;
  },

  isValidPhone: (phone) => {
    const phoneRegex = /^[\d\s\-\+\(\)]+$/;
    return phoneRegex.test(phone);
  },
};

/* Format utility functions */

export const formatUtils = {
  formatDate: (date) => {
    return new Date(date).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
    });
  },

  formatDateTime: (date) => {
    return new Date(date).toLocaleString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  },

  formatCurrency: (amount) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    }).format(amount);
  },

  truncateText: (text, length = 100) => {
    return text && text.length > length ? text.substring(0, length) + '...' : text;
  },
};
